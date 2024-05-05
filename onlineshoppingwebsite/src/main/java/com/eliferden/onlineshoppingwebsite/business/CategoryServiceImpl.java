package com.eliferden.onlineshoppingwebsite.business;

import com.eliferden.onlineshoppingwebsite.dto.CategoryCreateDTO;
import com.eliferden.onlineshoppingwebsite.dto.CategoryUpdateDTO;
import com.eliferden.onlineshoppingwebsite.entities.Category;
import com.eliferden.onlineshoppingwebsite.exceptions.ErrorMessagesForCategory;
import com.eliferden.onlineshoppingwebsite.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Category createCategory(CategoryCreateDTO categoryCreateDTO) {
        validateCreateCategoryDTO(categoryCreateDTO);
        if (categoryRepository.existsByName(categoryCreateDTO.getName())) {
            throw new IllegalArgumentException(String.format(ErrorMessagesForCategory.CATEGORY_NAME_TAKEN, categoryCreateDTO.getName()));
        }
        Category category = new Category();
        category.setName(categoryCreateDTO.getName());
        category.setDescription(categoryCreateDTO.getDescription());
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Long categoryId, CategoryUpdateDTO categoryUpdateDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessagesForCategory.CATEGORY_NOT_FOUND_WITH_ID, categoryId)));
        validateUpdateCategoryDTO(categoryUpdateDTO, category);

        if (categoryUpdateDTO.getName() != null && !categoryUpdateDTO.getName().equals(category.getName())) {
            category.setName(categoryUpdateDTO.getName());
        }
        category.setDescription(categoryUpdateDTO.getDescription());
        return categoryRepository.save(category);
    }


    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException(String.format(ErrorMessagesForCategory.CATEGORY_NOT_FOUND_WITH_ID, categoryId));
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessagesForCategory.CATEGORY_NOT_FOUND_WITH_ID, categoryId)));
    }

    @Override
    public List<Category> listAllCategories() {
        return categoryRepository.findAll();
    }
    private void validateCreateCategoryDTO(CategoryCreateDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorMessagesForCategory.CATEGORY_NAME_EMPTY);
        }
    }
    private void validateUpdateCategoryDTO(CategoryUpdateDTO dto, Category category) {
        if (dto.getName() != null && dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorMessagesForCategory.CATEGORY_NAME_EMPTY);
        }

        if (dto.getName() != null && !dto.getName().equals(category.getName())) {
            if (categoryRepository.existsByName(dto.getName())) {
                throw new IllegalArgumentException(String.format(ErrorMessagesForCategory.CATEGORY_NAME_TAKEN, dto.getName()));
            }
        }

    }




}
