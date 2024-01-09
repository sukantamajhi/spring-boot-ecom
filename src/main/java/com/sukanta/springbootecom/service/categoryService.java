package com.sukanta.springbootecom.service;

import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.model.Category;
import com.sukanta.springbootecom.repository.categoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class categoryService {
    private final categoryRepository categoryRepository;

    public categoryService(com.sukanta.springbootecom.repository.categoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category request, String userId) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdBy(userId)
                .build();

        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByUserId(String userId) {
        return categoryRepository.findByCreatedBy(userId);
    }

    public Category update(String categoryId, String userId, Category request) throws Exception {

        Category category = categoryRepository.findById(categoryId).orElse(null);
        assert category != null;
        if (!Objects.equals(category.getCreatedBy(), userId)) {
            throw new Exception(Constant.UPDATE_AUTHORIZATION_FAILED);
        } else {
            category.setName(request.getName());
            category.setDescription(request.getDescription());

            return categoryRepository.save(category);
        }
    }
}
