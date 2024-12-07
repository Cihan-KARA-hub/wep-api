package com.yelman.advertisementserver.services;

import com.yelman.advertisementserver.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryServices {
    private final CategoryRepository categoryRepository;

    public CategoryServices(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


}
