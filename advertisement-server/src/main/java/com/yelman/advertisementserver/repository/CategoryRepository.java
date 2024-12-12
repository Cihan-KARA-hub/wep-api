package com.yelman.advertisementserver.repository;

import com.querydsl.core.types.dsl.NumberPath;
import com.yelman.advertisementserver.model.CategoryModel;
import com.yelman.advertisementserver.model.QCategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, Long>,
        QuerydslPredicateExecutor<CategoryModel>,
        QuerydslBinderCustomizer<QCategoryModel> {
    default void customize(QuerydslBindings bindings, QCategoryModel categoryModel) {
        bindings.bind(categoryModel.id).first((NumberPath<Long> path, Long value) -> path.eq(value));
    }

    CategoryModel findByName(String name);
}
