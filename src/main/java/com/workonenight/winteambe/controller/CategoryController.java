package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.dto.CategoryDTO;
import com.workonenight.winteambe.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/list/all")
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }
    @GetMapping(value = "/list/{id}")
    public CategoryDTO getCategoryById(@PathVariable("id") String id) {
        return categoryService.getCategoryById(id);
    }
    @PostMapping(value = "/create")
    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO) { return categoryService.createCategory(categoryDTO);}
    @PostMapping(value = "/update")
    public CategoryDTO updateCategory(@RequestBody CategoryDTO categoryDTO) {return categoryService.updateCategory(categoryDTO);}



}
