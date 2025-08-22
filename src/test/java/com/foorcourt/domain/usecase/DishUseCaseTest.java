package com.foorcourt.domain.usecase;

import com.foorcourt.domain.exception.NotFoundException;
import com.foorcourt.domain.exception.ValidationException;
import com.foorcourt.domain.model.CategoryModel;
import com.foorcourt.domain.model.DishModel;
import com.foorcourt.domain.model.RestaurantModel;
import com.foorcourt.domain.model.simplemodel.CategorySimpleModel;
import com.foorcourt.domain.model.simplemodel.RestaurantSimpleModel;
import com.foorcourt.domain.spi.ICategoryPersistencePort;
import com.foorcourt.domain.spi.IDishPersistencePort;
import com.foorcourt.domain.spi.IRestaurantPersistencePort;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishService;
    
    @Mock
    private IRestaurantPersistencePort restaurantService;
    
    @Mock
    private ICategoryPersistencePort categoryService;
    
    @InjectMocks
    private DishUseCase dishUseCase;
    
    private DishModel dishModel;
    private RestaurantModel restaurantModel;
    private CategoryModel categoryModel;
    
    @BeforeEach
    void setUp() {
        restaurantModel = new RestaurantModel();
        restaurantModel.setId(1L);
        restaurantModel.setName("Test Restaurant");
        
        categoryModel = new CategoryModel();
        categoryModel.setId(1L);
        categoryModel.setName("Test Category");
        
        dishModel = new DishModel();
        dishModel.setId(1L);
        dishModel.setName("Test Dish");
        dishModel.setDescription("Test Description");
        dishModel.setPrice(BigDecimal.valueOf(10.50));
        dishModel.setUrlImage("http://test.com/image.jpg");
        dishModel.setActive(true);
        
        RestaurantSimpleModel restaurant = new RestaurantSimpleModel();
        restaurant.setId(1L);
        dishModel.setRestaurant(restaurant);
        
        CategorySimpleModel category = new CategorySimpleModel();
        category.setId(1L);
        dishModel.setCategory(category);
    }
    
    @Test
    void save_ShouldCreateDish_WhenValidData() {
        when(restaurantService.findById(1L)).thenReturn(Optional.of(restaurantModel));
        when(categoryService.findById(1L)).thenReturn(Optional.of(categoryModel));
        when(dishService.save(any(DishModel.class))).thenReturn(dishModel);
        
        DishModel result = dishUseCase.save(dishModel);
        
        assertNotNull(result);
        assertEquals("Test Dish", result.getName());
        assertTrue(result.getActive());
        verify(dishService).save(any(DishModel.class));
    }
    
    @Test
    void save_ShouldThrowNotFoundException_WhenRestaurantNotFound() {
        when(restaurantService.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(NotFoundException.class, () -> dishUseCase.save(dishModel));
        verify(dishService, never()).save(any(DishModel.class));
    }
    
    @Test
    void save_ShouldThrowNotFoundException_WhenCategoryNotFound() {
        when(restaurantService.findById(1L)).thenReturn(Optional.of(restaurantModel));
        when(categoryService.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(NotFoundException.class, () -> dishUseCase.save(dishModel));
        verify(dishService, never()).save(any(DishModel.class));
    }
    
    @Test
    void save_ShouldThrowValidationException_WhenNameIsNull() {
        dishModel.setName(null);
        
        assertThrows(ValidationException.class, () -> dishUseCase.save(dishModel));
        verify(dishService, never()).save(any(DishModel.class));
    }
    
    @Test
    void update_ShouldUpdateDish_WhenValidData() {
        DishModel existingDish = new DishModel();
        existingDish.setId(1L);
        existingDish.setName("Old Name");
        existingDish.setActive(true);
        
        DishModel updateData = new DishModel();
        updateData.setId(1L);
        updateData.setDescription("New Description");
        updateData.setPrice(BigDecimal.valueOf(15.00));
        
        when(dishService.findById(1L)).thenReturn(Optional.of(existingDish));
        when(dishService.save(any(DishModel.class))).thenReturn(existingDish);
        
        DishModel result = dishUseCase.update(updateData);
        
        assertNotNull(result);
        verify(dishService).save(any(DishModel.class));
    }
    
    @Test
    void update_ShouldThrowNotFoundException_WhenDishNotFound() {
        when(dishService.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(NotFoundException.class, () -> dishUseCase.update(dishModel));
        verify(dishService, never()).save(any(DishModel.class));
    }
    
    @Test
    void changeStatusDish_ShouldChangeStatus_WhenValidId() {
        DishModel existingDish = new DishModel();
        existingDish.setId(1L);
        existingDish.setActive(true);
        
        DishModel statusChange = new DishModel();
        statusChange.setId(1L);
        statusChange.setActive(false);
        
        when(dishService.findById(1L)).thenReturn(Optional.of(existingDish));
        when(dishService.save(any(DishModel.class))).thenReturn(existingDish);
        
        DishModel result = dishUseCase.changeStatusDish(statusChange);
        
        assertNotNull(result);
        verify(dishService).save(any(DishModel.class));
    }
    
    @Test
    void findAllDishesByRestaurantId_ShouldReturnDishes_WhenValidRestaurant() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<DishModel> dishPage = new PageImpl<>(List.of(dishModel));
        
        when(restaurantService.findById(1L)).thenReturn(Optional.of(restaurantModel));
        when(categoryService.findById(1L)).thenReturn(Optional.of(categoryModel));
        when(dishService.findAllDishesByRestaurantId(pageable, 1L, 1L)).thenReturn(dishPage);
        
        Page<DishModel> result = dishUseCase.findAllDishesByRestaurantId(pageable, 1L, 1L);
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(dishService).findAllDishesByRestaurantId(pageable, 1L, 1L);
    }
    
    @Test
    void findAllDishesByRestaurantId_ShouldThrowValidationException_WhenRestaurantIdIsNull() {
        Pageable pageable = PageRequest.of(0, 10);
        
        assertThrows(ValidationException.class, 
            () -> dishUseCase.findAllDishesByRestaurantId(pageable, null, 1L));
        verify(restaurantService, never()).findById(anyLong());
    }
}