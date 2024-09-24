package com.emazon.mscart.domain.api;

import com.emazon.mscart.domain.model.ArticleCart;

public interface IArticleCartServicePort {
    void addArticleCart(ArticleCart articleCart, Long userId);
    void deleteArticleCart(Long id);
}
