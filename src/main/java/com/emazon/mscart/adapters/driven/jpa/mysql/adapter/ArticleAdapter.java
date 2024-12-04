package com.emazon.mscart.adapters.driven.jpa.mysql.adapter;

import com.emazon.mscart.domain.model.Article;
import com.emazon.mscart.domain.model.ArticleFilter;
import com.emazon.mscart.domain.model.Pagination;
import com.emazon.mscart.domain.spi.IArticlePersistencePort;
import com.emazon.mscart.infraestructure.feign_client.StockFeignClient;
import com.emazon.mscart.infraestructure.feign_client.SupplyFeignClient;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public class ArticleAdapter implements IArticlePersistencePort {
    private final StockFeignClient stockFeignClient;
    private final SupplyFeignClient supplyFeignClient;
    @Override
    public Optional<Article> getArticleById(Long articleId) {
        return stockFeignClient.getArticleById(articleId);
    }

    @Override
    public LocalDate getEstimatedNextDate(Long articleId) {
        return supplyFeignClient.getEstimatedDate(articleId);
    }

    @Override
    public Pagination<Article> getArticlesCart(ArticleFilter articleFilter) {
        return stockFeignClient.getFilteredArticlesById(articleFilter);
    }
}
