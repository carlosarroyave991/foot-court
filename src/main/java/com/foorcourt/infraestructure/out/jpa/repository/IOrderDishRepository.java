package com.foorcourt.infraestructure.out.jpa.repository;

import com.foorcourt.infraestructure.out.jpa.entity.OrderDishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderDishRepository extends JpaRepository<OrderDishEntity, Long> {
}