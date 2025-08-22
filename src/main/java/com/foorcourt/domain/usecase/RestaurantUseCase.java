package com.foorcourt.domain.usecase;

import com.foorcourt.domain.api.IRestaurantServicePort;
import com.foorcourt.domain.exception.BusinessException;
import com.foorcourt.domain.exception.DuplicateResourceException;
import com.foorcourt.domain.exception.NotFoundException;
import com.foorcourt.domain.exception.ValidationException;
import com.foorcourt.domain.model.RestaurantModel;
import com.foorcourt.domain.model.feignclient.UserModel;
import com.foorcourt.domain.spi.IRestaurantPersistencePort;
import com.foorcourt.domain.util.NitValidationService;
import com.foorcourt.domain.util.PhoneValidationService;
import com.foorcourt.domain.spi.IUserFeignClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static com.foorcourt.domain.exception.error.CommonErrorCode.*;

@RequiredArgsConstructor
public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistence;
    private final NitValidationService nitValidationService;
    private final PhoneValidationService phoneValidationService;
    private final IUserFeignClientPort userFeignClientPort;

    @Override
    public RestaurantModel save(RestaurantModel model) {
        Optional<UserModel> userModel = Optional.ofNullable(userFeignClientPort.getUserById(model.getOwnerId()));
        if (userModel.isEmpty()) throw new NotFoundException(USER_NOT_FOUND);
        Optional<RestaurantModel> nitFound = restaurantPersistence.findByNit(model.getNit());
        if (nitFound.isPresent()) throw new DuplicateResourceException(NIT_ALREADY_EXISTS);
        if (!nitValidationService.isValidNit(model.getNit())) throw new ValidationException(INVALID_NIT);
        if (!phoneValidationService.isValidPhone(model.getPhone())) throw new ValidationException(INVALID_PHONE);
        restaurantPersistence.save(model);

        return model;
    }

    @Override
    public Page<RestaurantModel> getAll(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("name").ascending()
        );
        return restaurantPersistence.findAll(sortedPageable);
    }

    @Override
    public Optional<RestaurantModel> findById(Long id) {
        Optional<RestaurantModel> restaurantModel = restaurantPersistence.findById(id);
        if (restaurantModel.isEmpty()) throw new BusinessException(ID_NOT_FOUND);
        return restaurantModel;
    }
}
