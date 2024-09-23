package com.emazon.mscart.domain.spi;

import com.emazon.mscart.domain.model.Article;

import java.time.LocalDate;
import java.util.Optional;

public interface IArticlePersistencePort {
    Optional<Article> getArticleById(Long articleId);
    LocalDate getEstimatedNextDate(Long articleId);
}
