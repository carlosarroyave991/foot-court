package com.foorcourt.infraestructure.out.jpa.repository;

import com.foorcourt.infraestructure.out.jpa.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    
    List<OrderEntity> findByClientIdAndStatusIn(Long clientId, List<String> statuses);
    
    Page<OrderEntity> findByStatus(String status, Pageable pageable);
    
    @Query("SELECT DISTINCT o FROM OrderEntity o LEFT JOIN FETCH o.ordersDishes od LEFT JOIN FETCH od.dish WHERE o.status = :status AND o.restaurant.id = :restaurantId")
    Page<OrderEntity> findByStatusAndRestaurantIdWithDishes(@Param("status") String status, @Param("restaurantId") Long restaurantId, Pageable pageable);
    
    @Modifying
    @Query("UPDATE OrderEntity o SET o.chefId = :chefId, o.status = :status WHERE o.id = :orderId")
    void updateOrderAssignment(@Param("orderId") Long orderId, @Param("chefId") Long chefId, @Param("status") String status);
    
    @Modifying
    @Query("UPDATE OrderEntity o SET o.securityCode = :securityCode WHERE o.id = :orderId")
    void updateSecurityCode(@Param("orderId") Long orderId, @Param("securityCode") String securityCode);
    
    @Modifying
    @Query("UPDATE OrderEntity o SET o.status = :status WHERE o.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") String status);

}