package com.foorcourt.infraestructure.configuration;

import com.foorcourt.domain.api.IRestaurantServicePort;
import com.foorcourt.domain.spi.IRestaurantPersistencePort;
import com.foorcourt.domain.spi.IUserFeignClientPort;
import com.foorcourt.domain.usecase.RestaurantUseCase;
import com.foorcourt.domain.util.NitValidationService;
import com.foorcourt.domain.util.PhoneValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Crea beans de la capa de dominio: los casos de uso no tienen
 * anotaciones spring, asi que es necesario crearlos manualmente
 */
@Configuration
public class RestaurantConfiguration {
    /**
     * Conecto las implementaciones de infraestructura con los pruertos de dominio
     */
    @Bean
    public IRestaurantServicePort restaurantServicePort(
            IRestaurantPersistencePort restaurantPersistencePort,
            NitValidationService nitValidationService,
            PhoneValidationService phoneValidationService,
            IUserFeignClientPort userFeignClientPort) {
        return new RestaurantUseCase(
                restaurantPersistencePort,
                nitValidationService,
                phoneValidationService,
                userFeignClientPort
        );
    }
    
    @Bean
    public NitValidationService nitValidationService() {
        return new NitValidationService();
    }
    
    @Bean
    public PhoneValidationService phoneValidationService() {
        return new PhoneValidationService();
    }
}