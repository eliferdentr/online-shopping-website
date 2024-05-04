package com.eliferden.onlineshoppingwebsite.repository;

import com.eliferden.onlineshoppingwebsite.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
