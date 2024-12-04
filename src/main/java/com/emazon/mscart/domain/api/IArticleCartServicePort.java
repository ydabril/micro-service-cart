package com.emazon.mscart.domain.api;

import com.emazon.mscart.domain.model.*;

public interface IArticleCartServicePort {
    void addArticleCart(ArticleCart articleCart);
    void deleteArticleCart(Long id);

    Pagination<Article> getArticlesCart(ArticleFilter articleFilter);
}
