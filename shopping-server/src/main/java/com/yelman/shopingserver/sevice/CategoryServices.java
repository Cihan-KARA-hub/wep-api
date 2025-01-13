package com.yelman.shopingserver.sevice;


import com.yelman.shopingserver.model.Category;
import com.yelman.shopingserver.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServices {
    private final CategoryRepository categoryRepository;

    public CategoryServices(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getParentCategories() {
        List<Category> model = categoryRepository.findAll();
        List<Category> categories = new ArrayList<>();
        for (Category category : model) {
            if (category.getParentId() == 0) {
                categories.add(category);
            }
        }
        return categories;
    }
    public List<Category> getSubCategories(String category) {
        Category model1=categoryRepository.findByName(category);
        List<Category> model = categoryRepository.findAll();
        List<Category> categories = new ArrayList<>();
        for (Category category1 : model) {
            if (category1.getParentId() == model1.getId()) {
                categories.add(category1);
            }
        }
        return categories;
    }


}
