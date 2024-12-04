package com.emazon.mscart.domain.spi;

import com.emazon.mscart.domain.model.Article;
import com.emazon.mscart.domain.model.ArticleFilter;
import com.emazon.mscart.domain.model.Pagination;

import java.time.LocalDate;
import java.util.Optional;

public interface IArticlePersistencePort {
    Optional<Article> getArticleById(Long articleId);
    LocalDate getEstimatedNextDate(Long articleId);

    Pagination<Article> getArticlesCart(ArticleFilter articleFilter);
}
