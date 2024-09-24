package com.emazon.mscart.domain.api.use_case;

import com.emazon.mscart.domain.api.IArticleCartServicePort;
import com.emazon.mscart.domain.exception.ArticleNotFoundException;
import com.emazon.mscart.domain.exception.CategoryLimitExceededException;
import com.emazon.mscart.domain.exception.OutOfStockException;
import com.emazon.mscart.domain.model.Article;
import com.emazon.mscart.domain.model.ArticleCart;
import com.emazon.mscart.domain.model.Category;
import com.emazon.mscart.domain.spi.IArticleCartPersistencePort;
import com.emazon.mscart.domain.spi.IArticlePersistencePort;
import com.emazon.mscart.domain.util.DomainConstants;
import com.emazon.mscart.domain.util.OutOfStockResponse;

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
    public void addArticleCart(ArticleCart articleCart, Long userId) {
        Article article = getValidatedArticle(articleCart.getArticleId());
        String nextRestockDate = articlePersistencePort.getEstimatedNextDate(articleCart.getArticleId()).toString();

        validateStock(articleCart, article, nextRestockDate);

        ArticleCart updatedArticleCart = createArticleCart(articleCart, userId, article, nextRestockDate);

        saveOrUpdateArticleCart(updatedArticleCart);
        saveUpdateDate(userId);
    }

    @Override
    public void deleteArticleCart(Long id) {
        Optional<ArticleCart> articleCartOptional = articleCartPersistencePort.findById(id);

        if (articleCartOptional.isEmpty()) {
            throw new ArticleNotFoundException();
        }

        articleCartPersistencePort.deleteArticleCart(id);
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
