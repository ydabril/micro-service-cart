package com.emazon.mscart.infraestructure.feign_client;

import com.emazon.mscart.domain.model.Article;
import com.emazon.mscart.infraestructure.Constants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;


@FeignClient(name = Constants.STOCK_SERVICE_NAME, url = Constants.STOCK_SERVICE_URL)
public interface StockFeignClient {

    @GetMapping("/article/{articleId}")
    Optional<Article> getArticleById(@PathVariable Long articleId);
}