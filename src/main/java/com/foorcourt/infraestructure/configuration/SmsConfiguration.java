package com.foorcourt.infraestructure.configuration;

import com.foorcourt.domain.spi.ISmsFeignClientPort;
import com.foorcourt.infraestructure.out.feignclient.SmsFeignClient;
import com.foorcourt.infraestructure.out.feignclient.SmsFeignClientAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SmsConfiguration {
    
    @Bean
    public ISmsFeignClientPort smsFeignClientPort(SmsFeignClient smsFeignClient) {
        return new SmsFeignClientAdapter(smsFeignClient);
    }
}