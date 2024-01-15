package com.sukanta.springbootecom.service;

import java.util.List;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.model.Category;
import com.sukanta.springbootecom.repository.categoryRepository;

@Service
public class categoryService {
    private final categoryRepository categoryRepository;

    public categoryService(com.sukanta.springbootecom.repository.categoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category request, String userId) throws Exception {
        Category category = Category.builder().name(request.getName()).description(request.getDescription())
                .createdBy(userId).build();

        if (category != null) {
            return categoryRepository.save(category);
        } else {
            throw new Exception("Category create failed");
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByUserId(String userId) {
        return categoryRepository.findByCreatedBy(userId);
    }

    public Category update(@NonNull String categoryId, String userId, Category request) throws Exception {
        Category category = categoryRepository.findById(categoryId).get();
        assert category != null;
        if (!Objects.equals(category.getCreatedBy(), userId)) {
            throw new Exception(Constant.UPDATE_AUTHORIZATION_FAILED);
        } else {
            category.setName(request.getName());
            category.setDescription(request.getDescription());

            return categoryRepository.save(category);
        }
    }

    public void delete(@NonNull String categoryId, String userId) throws Exception {
        Category category = categoryRepository.findById(categoryId).get();
        assert category != null;
        if (!Objects.equals(category.getCreatedBy(), userId)) {
            throw new Exception(Constant.UPDATE_AUTHORIZATION_FAILED);
        } else {
            categoryRepository.deleteById(categoryId);
        }
    }
}
