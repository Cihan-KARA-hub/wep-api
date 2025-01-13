package com.yelman.blogservices.services;

import com.yelman.blogservices.repository.blog.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryServices {
    private final CategoryRepository categoryRepository;
    public CategoryServices(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }




}
