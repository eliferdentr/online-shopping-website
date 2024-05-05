package com.eliferden.onlineshoppingwebsite.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "image_url", nullable = true)
    private String imageUrl;

    @ManyToOne (fetch = FetchType.EAGER)
        @JoinColumn(name = "category_id")
    private  Category category;

    public Product() {
    }

    public Product(String name, String description, BigDecimal price, Integer stock, String imageUrl, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.category = category;
    }


}
