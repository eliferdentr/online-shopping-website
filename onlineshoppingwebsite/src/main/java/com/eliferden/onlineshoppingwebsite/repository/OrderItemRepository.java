package com.eliferden.onlineshoppingwebsite.repository;

import com.eliferden.onlineshoppingwebsite.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
