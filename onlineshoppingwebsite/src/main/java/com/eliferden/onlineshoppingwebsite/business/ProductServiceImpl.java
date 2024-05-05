package com.eliferden.onlineshoppingwebsite.business;

import com.eliferden.onlineshoppingwebsite.dto.ProductCreateDTO;
import com.eliferden.onlineshoppingwebsite.dto.ProductUpdateDTO;
import com.eliferden.onlineshoppingwebsite.entities.Category;
import com.eliferden.onlineshoppingwebsite.entities.Product;
import com.eliferden.onlineshoppingwebsite.exceptions.ErrorMessagesForProduct;
import com.eliferden.onlineshoppingwebsite.exceptions.ErrorMessagesForCategory;
import com.eliferden.onlineshoppingwebsite.repository.CategoryRepository;
import com.eliferden.onlineshoppingwebsite.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    @Transactional
    public Product createProduct(ProductCreateDTO productCreateDTO) {
        validateProductDTO(productCreateDTO);
        Category category = getCategory(productCreateDTO.getCategoryId());
        if (checkIfNameExists(productCreateDTO.getName())) throw new IllegalArgumentException(String.format(ErrorMessagesForProduct.PRODUCT_NAME_TAKEN, productCreateDTO.getName()));
        Product product = new Product(productCreateDTO.getName(), productCreateDTO.getDescription(), productCreateDTO.getPrice(), productCreateDTO.getStock(), productCreateDTO.getImageUrl(), category);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(Long productId, ProductUpdateDTO productUpdateDTO) {
        Product product = getProductById(productId);
        validateProductUpdateDTO(productUpdateDTO);

        if (productUpdateDTO.getName() != null && !product.getName().equals(productUpdateDTO.getName())) {
            if (checkIfNameExists(productUpdateDTO.getName())) {
                throw new IllegalArgumentException(String.format(ErrorMessagesForProduct.PRODUCT_NAME_TAKEN, productUpdateDTO.getName()));
            }
            product.setName(productUpdateDTO.getName());
        }

        if (productUpdateDTO.getDescription() != null) product.setDescription(productUpdateDTO.getDescription());
        if (productUpdateDTO.getPrice() != null) product.setPrice(productUpdateDTO.getPrice());
        if (productUpdateDTO.getStock() != null) product.setStock(productUpdateDTO.getStock());
        if (productUpdateDTO.getImageUrl() != null) product.setImageUrl(productUpdateDTO.getImageUrl());

        if (productUpdateDTO.getCategoryId() != null && (product.getCategory() == null || !product.getCategory().getId().equals(productUpdateDTO.getCategoryId()))) {
            Category category = getCategory(productUpdateDTO.getCategoryId());
            product.setCategory(category);
        }


        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException(String.format(ErrorMessagesForProduct.PRODUCT_NOT_FOUND_WITH_ID, productId));
        }
        productRepository.deleteById(productId);

    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessagesForProduct.PRODUCT_NOT_FOUND_WITH_ID, productId)));

    }

    @Override
    public List<Product> listAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public  boolean checkIfNameExists (String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessagesForCategory.CATEGORY_NOT_FOUND_WITH_ID, id)));
    }

    private void validateProductDTO(ProductCreateDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty())
            throw new IllegalArgumentException(ErrorMessagesForProduct.PRODUCT_NAME_EMPTY);
        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException(ErrorMessagesForProduct.PRICE_VALUE_INVALID);
        if (dto.getStock() < 0)
            throw new IllegalArgumentException(ErrorMessagesForProduct.STOCK_VALUE_INVALID);
        if (dto.getCategoryId() == null)
            throw new IllegalArgumentException(String.format(ErrorMessagesForCategory.CATEGORY_NOT_FOUND_WITH_ID, dto.getCategoryId()));
    }

    // Helper method to validate ProductUpdateDTO
    private void validateProductUpdateDTO(ProductUpdateDTO dto) {
        if (dto.getName() != null && dto.getName().trim().isEmpty())
            throw new IllegalArgumentException(ErrorMessagesForProduct.PRODUCT_NAME_EMPTY);
        if (dto.getPrice() != null && dto.getPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException(ErrorMessagesForProduct.PRICE_VALUE_INVALID);
        if (dto.getStock() != null && dto.getStock() < 0)
            throw new IllegalArgumentException(ErrorMessagesForProduct.STOCK_VALUE_INVALID);
    }





}
