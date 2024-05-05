package com.eliferden.onlineshoppingwebsite.repository;

import com.eliferden.onlineshoppingwebsite.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
