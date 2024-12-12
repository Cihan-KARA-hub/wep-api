package com.yelman.advertisementserver.services;

import com.yelman.advertisementserver.model.CategoryModel;
import com.yelman.advertisementserver.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServices {
    private final CategoryRepository categoryRepository;

    public CategoryServices(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryModel> getParentCategories() {
        List<CategoryModel> model = categoryRepository.findAll();
        List<CategoryModel> categories = new ArrayList<>();
        for (CategoryModel category : model) {
            if (category.getParentId() == 0) {
                categories.add(category);
            }
        }
        return categories;
    }
    public List<CategoryModel> getSubCategories(String category) {
        CategoryModel model1=categoryRepository.findByName(category);
        List<CategoryModel> model = categoryRepository.findAll();
        List<CategoryModel> categories = new ArrayList<>();
        for (CategoryModel category1 : model) {
            if (category1.getParentId() == model1.getId()) {
                categories.add(category1);
            }
        }
        return categories;
    }


}
