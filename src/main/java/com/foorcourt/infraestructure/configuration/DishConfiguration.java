package com.foorcourt.infraestructure.configuration;

import com.foorcourt.domain.api.IDishServicePort;
import com.foorcourt.domain.spi.ICategoryPersistencePort;
import com.foorcourt.domain.spi.IDishPersistencePort;
import com.foorcourt.domain.spi.IRestaurantPersistencePort;
import com.foorcourt.domain.usecase.DishUseCase;
import com.foorcourt.domain.util.DishValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Crea beans de la capa de dominio: los casos de uso no tienen
 * anotaciones spring, asi que es necesario crearlos manualmente
 */
@Configuration
public class DishConfiguration {
    /**
     * Conecto las implementaciones de infraestructura con los puertos de dominio
     */
    @Bean
    public IDishServicePort dishServicePort(
            IDishPersistencePort dishPersistencePort,
            IRestaurantPersistencePort restaurantPersistencePort,
            ICategoryPersistencePort categoryPersistencePort,
            DishValidationService dishValidationService){
        return new DishUseCase(
                dishPersistencePort,
                restaurantPersistencePort,
                categoryPersistencePort,
                dishValidationService
        );
    }
}