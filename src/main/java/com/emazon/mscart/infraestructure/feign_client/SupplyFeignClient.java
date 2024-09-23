package com.emazon.mscart.infraestructure.feign_client;

import com.emazon.mscart.domain.model.Article;
import com.emazon.mscart.infraestructure.Constants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.Optional;

@FeignClient(name = Constants.SUPPLY_SERVICE_NAME, url = Constants.SUPPLY_SERVICE_URL)
public interface SupplyFeignClient {
    @GetMapping("/transaction/next-date-restock/{articleId}")
    LocalDate getEstimatedDate(@PathVariable Long articleId);
}
