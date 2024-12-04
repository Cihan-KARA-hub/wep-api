package com.yelman.blogservices.repository;

import com.querydsl.core.types.dsl.StringPath;
import com.yelman.blogservices.model.Blogs;
import com.yelman.blogservices.model.QBlogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;


@Repository
public interface BlogRepository extends JpaRepository<Blogs, Long>,QuerydslPredicateExecutor<Blogs>, QuerydslBinderCustomizer<QBlogs> {

    @Override
    default void customize(QuerydslBindings bindings, QBlogs blogs) {
        bindings.bind(blogs.content).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
