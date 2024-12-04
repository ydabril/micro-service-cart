package com.emazon.mscart.infraestructure.feign_client;

import com.emazon.mscart.domain.model.Article;
import com.emazon.mscart.domain.model.ArticleFilter;
import com.emazon.mscart.domain.model.Pagination;
import com.emazon.mscart.infraestructure.Constants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@FeignClient(name = Constants.STOCK_SERVICE_NAME, url = Constants.STOCK_SERVICE_URL, configuration = FeignConfig.class)
public interface StockFeignClient {

    @GetMapping("/article/{articleId}")
    Optional<Article> getArticleById(@PathVariable Long articleId);

    @PostMapping("/article/getPaginatedArticlesById")
    Pagination<Article> getFilteredArticlesById(@RequestBody ArticleFilter articleFilterRequest);
}