package com.foorcourt.infraestructure.configuration;

import com.foorcourt.domain.api.IDishServicePort;
import com.foorcourt.domain.api.IOrderServicePort;
import com.foorcourt.domain.api.IRestaurantServicePort;
import com.foorcourt.domain.spi.IOrderPersistencePort;
import com.foorcourt.domain.spi.IUserFeignClientPort;
import com.foorcourt.domain.usecase.OrderUseCase;
import com.foorcourt.domain.util.OrderStatusValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OrderConfiguration {

    @Bean
    public IOrderServicePort orderServicePort(IDishServicePort dishServicePort,
                                            IRestaurantServicePort restaurantServicePort,
                                            IUserFeignClientPort userFeignClientPort,
                                            IOrderPersistencePort orderPersistencePort,
                                            OrderStatusValidator orderStatusValidator) {
        return new OrderUseCase(dishServicePort, restaurantServicePort, userFeignClientPort, orderPersistencePort, orderStatusValidator);
    }
}