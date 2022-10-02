package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.dto.CategoryDTO;
import com.workonenight.winteambe.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Get all categories
     * @return List<CategoryDTO> List of categories
     */
    @GetMapping(value = "/list/all")
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * Get category by id
     * @param id Category id
     * @return CategoryDTO Category
     */
    @GetMapping(value = "/list/{id}")
    public CategoryDTO getCategoryById(@PathVariable("id") String id) {
        return categoryService.getCategoryById(id);
    }

    /**
     * Create category
     * @param categoryDTO CategoryDTO
     * @return CategoryDTO Category
     */
    @PostMapping(value = "/create")
    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.createCategory(categoryDTO);
    }

    /**
     * Update category
     * @param categoryDTO CategoryDTO
     * @return CategoryDTO Category
     */
    @PostMapping(value = "/update")
    public CategoryDTO updateCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(categoryDTO);
    }


}
