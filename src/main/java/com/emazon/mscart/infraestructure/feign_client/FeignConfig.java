package com.emazon.mscart.infraestructure.feign_client;

import com.emazon.mscart.infraestructure.configuration.interceptors.FeignClientInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public FeignClientInterceptor customFeignClientInterceptor() {
        return new FeignClientInterceptor();
    }
}