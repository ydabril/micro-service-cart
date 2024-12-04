package com.emazon.mscart.domain.api.use_case;

import com.emazon.mscart.domain.api.IArticleCartServicePort;
import com.emazon.mscart.domain.exception.ArticleNotFoundException;
import com.emazon.mscart.domain.exception.CategoryLimitExceededException;
import com.emazon.mscart.domain.exception.OutOfStockException;
import com.emazon.mscart.domain.model.*;
import com.emazon.mscart.domain.spi.IArticleCartPersistencePort;
import com.emazon.mscart.domain.spi.IArticlePersistencePort;
import com.emazon.mscart.domain.util.DomainConstants;
import com.emazon.mscart.domain.util.OutOfStockResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ArticleCartUseCase  implements IArticleCartServicePort {
    private final IArticleCartPersistencePort articleCartPersistencePort;
    private final IArticlePersistencePort articlePersistencePort;

    public ArticleCartUseCase(IArticleCartPersistencePort articleCartPersistencePort, IArticlePersistencePort articlePersistencePort) {
        this.articleCartPersistencePort = articleCartPersistencePort;
        this.articlePersistencePort = articlePersistencePort;
    }
    @Override
    public void addArticleCart(ArticleCart articleCart) {
        Long userId = articleCartPersistencePort.getUserId();
        Article article = getValidatedArticle(articleCart.getArticleId());
        String nextRestockDate = articlePersistencePort.getEstimatedNextDate(articleCart.getArticleId()).toString();

        validateStock(articleCart, article, nextRestockDate);

        ArticleCart updatedArticleCart = createArticleCart(articleCart, userId, article, nextRestockDate);

        saveOrUpdateArticleCart(updatedArticleCart);
        saveUpdateDate(userId);
    }

    @Override
    public void deleteArticleCart(Long id) {
        Long userId = articleCartPersistencePort.getUserId();
        articleCartPersistencePort.deleteArticleCart(id);
        saveUpdateDate(userId);
    }

    @Override
    public Pagination<Article> getArticlesCart(ArticleFilter articleFilter) {
        Long userId = articleCartPersistencePort.getUserId();

        List<Long> articleIds = articleCartPersistencePort.getArticleIdsByUser(userId);
        articleFilter.setArticleIds(articleIds);

        Pagination<Article> pagination = articlePersistencePort.getArticlesCart(articleFilter);
        articleFilter.setPage(0);
        articleFilter.setSize(100);
        Pagination<Article> paginationAll = articlePersistencePort.getArticlesCart(articleFilter);

        List<ArticleCart> cartArticles = articleCartPersistencePort.findByUserId(userId);

        calculateTotalPriceAndUpdateArticles(pagination, cartArticles);
        Long totalPrice = calculateTotalPrice(paginationAll, cartArticles);

        pagination.setTotalPrice(totalPrice);

        return pagination;
    }


    private Long calculateTotalPrice(Pagination<Article> pagination, List<ArticleCart> cartArticles) {
        Long totalPrice = DomainConstants.TOTAL_PRICE_DEFAULT;

        for (Article article : pagination.getList()) {
            Optional<ArticleCart> articleCartOptional = findArticleInCart(article, cartArticles);

            if (articleCartOptional.isPresent()) {
                ArticleCart articleCart = articleCartOptional.get();

                totalPrice += article.getPrice().multiply(BigDecimal.valueOf(articleCart.getQuantity())).longValue();
            }
        }

        return totalPrice;
    }
    private void calculateTotalPriceAndUpdateArticles(Pagination<Article> pagination, List<ArticleCart> cartArticles) {
        for (Article article : pagination.getList()) {
            Optional<ArticleCart> articleCartOptional = findArticleInCart(article, cartArticles);

            if (articleCartOptional.isPresent()) {
                ArticleCart articleCart = articleCartOptional.get();

                updateArticleStockAndQuantity(article, articleCart);

                if (article.getStock() == DomainConstants.NOT_STOCK_NUMBER) {
                    article.setRestockNextDate(articlePersistencePort.getEstimatedNextDate(article.getId()));
                }
            }
        }
    }

    private Optional<ArticleCart> findArticleInCart(Article article, List<ArticleCart> cartArticles) {
        return cartArticles.stream()
                .filter(cart -> cart.getArticleId().equals(article.getId()))
                .findFirst();
    }

    private void updateArticleStockAndQuantity(Article article, ArticleCart articleCart) {
        article.setStock(article.getQuantity());
        article.setQuantity(articleCart.getQuantity());
        article.setCartId(articleCart.getId());
    }

    private Article getValidatedArticle(Long articleId) {
        Optional<Article> articleOptional = articlePersistencePort.getArticleById(articleId);

        if (articleOptional.isEmpty()) {
            throw new ArticleNotFoundException();
        }

        return articleOptional.get();
    }

    private void validateStock(ArticleCart articleCart, Article article, String nextRestockDate) {
        if (articleCart.getQuantity() > article.getQuantity()) {
            throw new OutOfStockException(new OutOfStockResponse(DomainConstants.NOT_STOCK_MESSAGE, DomainConstants.ESTIMATED_STOCK_MESSAGE + nextRestockDate));
        }
    }

    private ArticleCart createArticleCart(ArticleCart articleCart, Long userId, Article article, String nextRestockDate) {
        Optional<ArticleCart> existingArticleCart = articleCartPersistencePort.findByUserIdAndArticleId(userId, articleCart.getArticleId());

        if (existingArticleCart.isPresent()) {
            ArticleCart existingCart = existingArticleCart.get();
            Long newQuantity = existingCart.getQuantity() + articleCart.getQuantity();

            if (newQuantity > article.getQuantity()) {
                throw new OutOfStockException(
                    new OutOfStockResponse(
                           DomainConstants.NOT_STOCK_MESSAGE,
         DomainConstants.ESTIMATED_STOCK_MESSAGE + nextRestockDate
                    )
                );
            }
            existingCart.setQuantity(newQuantity);
            existingCart.setUpdateDate(LocalDate.now());
            return existingCart;
        } else {
            articleCart.setUserId(userId);
            articleCart.setRegistrationDate(LocalDate.now());
            articleCart.setUpdateDate(LocalDate.now());
            validateCategoryLimit(userId, article);
            return articleCart;
        }
    }

    private void saveOrUpdateArticleCart(ArticleCart articleCart) {
        articleCartPersistencePort.addArticleCart(articleCart);
    }
    private void saveUpdateDate(Long userId) {
        List<ArticleCart> userCarts = articleCartPersistencePort.findByUserId(userId);

        LocalDate currentDate = LocalDate.now();
        for (ArticleCart cart : userCarts) {
            cart.setUpdateDate(currentDate);
        }

        articleCartPersistencePort.updateAll(userCarts);
    }

    private void validateCategoryLimit(Long userId, Article article) {
        List<ArticleCart> userCart = articleCartPersistencePort.findByUserId(userId);

        Map<Long, Integer> categoryCountMap = new HashMap<>();

        for (ArticleCart cartItem : userCart) {
            Article cartArticle = articlePersistencePort.getArticleById(cartItem.getArticleId())
                    .orElseThrow(ArticleNotFoundException::new);

            for (Category category : cartArticle.getCategories()) {
                categoryCountMap.put(
                     category.getId(),
                     categoryCountMap.getOrDefault(category.getId(),
                     DomainConstants.DEFAULT_COUNT_CATEGORIES) + DomainConstants.COUNT_CATEGORIES
                );
            }
        }

        for (Category category : article.getCategories()) {
            int count = categoryCountMap.getOrDefault(category.getId(), DomainConstants.DEFAULT_COUNT_CATEGORIES);
            if (count >= DomainConstants.LIMIT_CATEGORIES_COUNT) {
                throw new CategoryLimitExceededException();
            }
        }
    }
}
