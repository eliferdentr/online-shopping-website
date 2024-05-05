package com.eliferden.onlineshoppingwebsite.business;

import com.eliferden.onlineshoppingwebsite.dto.ProductCreateDTO;
import com.eliferden.onlineshoppingwebsite.dto.ProductUpdateDTO;
import com.eliferden.onlineshoppingwebsite.entities.Category;
import com.eliferden.onlineshoppingwebsite.entities.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductCreateDTO productCreateDTO);
    Product updateProduct(Long productId, ProductUpdateDTO productUpdateDTO);
    void deleteProduct(Long productId);
    Product getProductById(Long productId);
    List<Product> listAllProducts();
    boolean checkIfNameExists (String name);
    Category getCategory (Long id);

}

