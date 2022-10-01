package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.service.CategoryService;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


}
