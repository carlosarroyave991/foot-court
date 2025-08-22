package com.foorcourt.domain.usecase;

import com.foorcourt.domain.exception.DuplicateResourceException;
import com.foorcourt.domain.exception.NotFoundException;
import com.foorcourt.domain.exception.ValidationException;
import com.foorcourt.domain.model.RestaurantModel;
import com.foorcourt.domain.model.feignclient.UserModel;
import com.foorcourt.domain.spi.IRestaurantPersistencePort;
import com.foorcourt.domain.spi.IUserFeignClientPort;
import com.foorcourt.domain.util.NitValidationService;
import com.foorcourt.domain.util.PhoneValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistence;
    
    @Mock
    private NitValidationService nitValidationService;
    
    @Mock
    private PhoneValidationService phoneValidationService;
    
    @Mock
    private IUserFeignClientPort userFeignClientPort;
    
    @InjectMocks
    private RestaurantUseCase restaurantUseCase;
    
    private RestaurantModel restaurantModel;
    private UserModel userModel;
    
    @BeforeEach
    void setUp() {
        restaurantModel = new RestaurantModel();
        restaurantModel.setId(1L);
        restaurantModel.setName("Test Restaurant");
        restaurantModel.setAddress("Test Address");
        restaurantModel.setOwnerId(1L);
        restaurantModel.setPhone("+573001234567");
        restaurantModel.setLogoUrl("http://test.com/logo.jpg");
        restaurantModel.setNit("123456789");
        
        userModel = new UserModel();
        userModel.setId(1L);
        userModel.setName("Test Owner");
        userModel.setEmail("owner@test.com");
    }
    
    @Test
    void save_ShouldCreateRestaurant_WhenValidData() {
        when(userFeignClientPort.getUserById(1L)).thenReturn(userModel);
        when(restaurantPersistence.findByNit("123456789")).thenReturn(Optional.empty());
        when(nitValidationService.isValidNit("123456789")).thenReturn(true);
        when(phoneValidationService.isValidPhone("+573001234567")).thenReturn(true);
        when(restaurantPersistence.save(any(RestaurantModel.class))).thenReturn(restaurantModel);
        
        RestaurantModel result = restaurantUseCase.save(restaurantModel);
        
        assertNotNull(result);
        assertEquals("Test Restaurant", result.getName());
        assertEquals("123456789", result.getNit());
        verify(restaurantPersistence).save(any(RestaurantModel.class));
    }
    
    @Test
    void save_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userFeignClientPort.getUserById(1L)).thenReturn(null);
        
        assertThrows(NotFoundException.class, () -> restaurantUseCase.save(restaurantModel));
        verify(restaurantPersistence, never()).save(any(RestaurantModel.class));
    }
    
    @Test
    void save_ShouldThrowDuplicateResourceException_WhenNitAlreadyExists() {
        RestaurantModel existingRestaurant = new RestaurantModel();
        existingRestaurant.setNit("123456789");
        
        when(userFeignClientPort.getUserById(1L)).thenReturn(userModel);
        when(restaurantPersistence.findByNit("123456789")).thenReturn(Optional.of(existingRestaurant));
        
        assertThrows(DuplicateResourceException.class, () -> restaurantUseCase.save(restaurantModel));
        verify(restaurantPersistence, never()).save(any(RestaurantModel.class));
    }
    
    @Test
    void save_ShouldThrowValidationException_WhenNitIsInvalid() {
        when(userFeignClientPort.getUserById(1L)).thenReturn(userModel);
        when(restaurantPersistence.findByNit("123456789")).thenReturn(Optional.empty());
        when(nitValidationService.isValidNit("123456789")).thenReturn(false);
        
        assertThrows(ValidationException.class, () -> restaurantUseCase.save(restaurantModel));
        verify(restaurantPersistence, never()).save(any(RestaurantModel.class));
    }
    
    @Test
    void save_ShouldThrowValidationException_WhenPhoneIsInvalid() {
        when(userFeignClientPort.getUserById(1L)).thenReturn(userModel);
        when(restaurantPersistence.findByNit("123456789")).thenReturn(Optional.empty());
        when(nitValidationService.isValidNit("123456789")).thenReturn(true);
        when(phoneValidationService.isValidPhone("+573001234567")).thenReturn(false);
        
        assertThrows(ValidationException.class, () -> restaurantUseCase.save(restaurantModel));
        verify(restaurantPersistence, never()).save(any(RestaurantModel.class));
    }
    
    @Test
    void getAll_ShouldReturnSortedRestaurants_WhenCalled() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RestaurantModel> restaurantPage = new PageImpl<>(List.of(restaurantModel));
        
        when(restaurantPersistence.findAll(any(Pageable.class))).thenReturn(restaurantPage);
        
        Page<RestaurantModel> result = restaurantUseCase.getAll(pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Restaurant", result.getContent().get(0).getName());
        verify(restaurantPersistence).findAll(any(Pageable.class));
    }
    
    @Test
    void getAll_ShouldApplySortByName_WhenCalled() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RestaurantModel> restaurantPage = new PageImpl<>(List.of(restaurantModel));
        
        when(restaurantPersistence.findAll(any(Pageable.class))).thenReturn(restaurantPage);
        
        restaurantUseCase.getAll(pageable);
        
        verify(restaurantPersistence).findAll(argThat(p -> 
            p.getSort().getOrderFor("name") != null && 
            p.getSort().getOrderFor("name").isAscending()
        ));
    }
    
    @Test
    void save_ShouldValidateAllFields_InCorrectOrder() {
        when(userFeignClientPort.getUserById(1L)).thenReturn(userModel);
        when(restaurantPersistence.findByNit(anyString())).thenReturn(Optional.empty());
        when(nitValidationService.isValidNit(anyString())).thenReturn(true);
        when(phoneValidationService.isValidPhone(anyString())).thenReturn(true);
        when(restaurantPersistence.save(any(RestaurantModel.class))).thenReturn(restaurantModel);
        
        restaurantUseCase.save(restaurantModel);
        
        verify(userFeignClientPort).getUserById(1L);
        verify(restaurantPersistence).findByNit("123456789");
        verify(nitValidationService).isValidNit("123456789");
        verify(phoneValidationService).isValidPhone("+573001234567");
        verify(restaurantPersistence).save(restaurantModel);
    }
}