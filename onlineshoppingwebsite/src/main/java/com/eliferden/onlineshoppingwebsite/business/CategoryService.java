package com.eliferden.onlineshoppingwebsite.business;

import com.eliferden.onlineshoppingwebsite.dto.CategoryCreateDTO;
import com.eliferden.onlineshoppingwebsite.dto.CategoryUpdateDTO;
import com.eliferden.onlineshoppingwebsite.entities.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryCreateDTO categoryCreateDTO);
    Category updateCategory(Long categoryId, CategoryUpdateDTO categoryUpdateDTO);
    void deleteCategory(Long categoryId);
    Category getCategoryById(Long categoryId);
    List<Category> listAllCategories();

}
