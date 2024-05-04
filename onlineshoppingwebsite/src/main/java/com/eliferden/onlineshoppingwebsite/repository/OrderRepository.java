package com.eliferden.onlineshoppingwebsite.repository;

import com.eliferden.onlineshoppingwebsite.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
