package com.emazon.mscart.infraestructure.configuration;



import com.emazon.mscart.adapters.driven.jpa.mysql.adapter.ArticleAdapter;
import com.emazon.mscart.adapters.driven.jpa.mysql.adapter.ArticleCartAdapter;
import com.emazon.mscart.adapters.driven.jpa.mysql.mapper.IArticleCartEntityMapper;
import com.emazon.mscart.adapters.driven.jpa.mysql.repository.ICartRepository;
import com.emazon.mscart.domain.api.IArticleCartServicePort;
import com.emazon.mscart.domain.api.use_case.ArticleCartUseCase;
import com.emazon.mscart.domain.spi.IArticleCartPersistencePort;
import com.emazon.mscart.domain.spi.IArticlePersistencePort;
import com.emazon.mscart.infraestructure.feign_client.StockFeignClient;
import com.emazon.mscart.infraestructure.feign_client.SupplyFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final IArticleCartEntityMapper articleCartEntityMapper;
    private final ICartRepository cartRepository;
    private final StockFeignClient stockFeignClient;
    private final SupplyFeignClient supplyFeignClient;

    @Bean
    public IArticleCartPersistencePort articleCartPersistencePort() {
        return new ArticleCartAdapter(articleCartEntityMapper, cartRepository);
    }

    @Bean
    public IArticlePersistencePort articlePersistencePort() {
        return new ArticleAdapter(stockFeignClient, supplyFeignClient);
    }

    @Bean
    public IArticleCartServicePort articleCartServicePort() {
        return new ArticleCartUseCase(articleCartPersistencePort(), articlePersistencePort());
    }
}
