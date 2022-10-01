package com.workonenight.winteambe.service;

import com.workonenight.winteambe.dto.CategoryDTO;
import com.workonenight.winteambe.entity.Category;
import com.workonenight.winteambe.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(Category::toDTO).collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(String id) {
        return categoryRepository.findById(id).orElseThrow().toDTO();
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = categoryDTO.toEntity();
        return categoryRepository.save(category).toDTO();
    }

    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryDTO.getId()).orElse(null);
        if (category != null) {
            category = category.toUpdateEntity(categoryDTO);
            return categoryRepository.save(category).toDTO();
        }
        return null;
    }

}
