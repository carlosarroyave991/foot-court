package com.foorcourt.infraestructure.configuration.feigclient;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.foorcourt.infraestructure.out.feignclient")
public class FeignConfig {
    
    @Autowired
    private FeignClientInterceptor feignClientInterceptor;

    /**
     * Registra el interceptor como bean
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return feignClientInterceptor;
    }
}