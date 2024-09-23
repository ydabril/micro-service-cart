package com.emazon.mscart.domain.spi;

import com.emazon.mscart.domain.model.ArticleCart;

import java.util.List;
import java.util.Optional;

public interface IArticleCartPersistencePort {
    void addArticleCart(ArticleCart articleCart);
    Optional<ArticleCart> findByUserIdAndArticleId(Long userId, Long articleId);
    void updateArticleCart(ArticleCart articleCart);

    List<ArticleCart> findByUserId(Long userId);
    void updateAll(List<ArticleCart> articleCarts);
}
