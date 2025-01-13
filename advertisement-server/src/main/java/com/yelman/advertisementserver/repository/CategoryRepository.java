package com.yelman.advertisementserver.repository;

import com.querydsl.core.types.dsl.NumberPath;
import com.yelman.advertisementserver.model.Category;

import com.yelman.advertisementserver.model.QCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>,
        QuerydslPredicateExecutor<Category>,
        QuerydslBinderCustomizer<QCategory> {
    default void customize(QuerydslBindings bindings, QCategory categoryModel) {
        bindings.bind(categoryModel.id).first((NumberPath<Long> path, Long value) -> path.eq(value));
    }

    Category findByName(String name);
}
