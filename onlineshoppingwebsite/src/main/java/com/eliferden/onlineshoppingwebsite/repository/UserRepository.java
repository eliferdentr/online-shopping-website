package com.eliferden.onlineshoppingwebsite.repository;

import com.eliferden.onlineshoppingwebsite.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String userName);

    boolean existsByEmail(String email);
}
