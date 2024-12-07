package com.yelman.blogservices.repository;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.yelman.blogservices.model.Blogs;
import com.yelman.blogservices.model.Category;
import com.yelman.blogservices.model.QBlogs;
import com.yelman.blogservices.model.enums.ActiveEnum;
import com.yelman.blogservices.model.enums.ShortLangEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BlogRepository extends JpaRepository<Blogs, Long>,
        QuerydslPredicateExecutor<Blogs>,
        QuerydslBinderCustomizer<QBlogs> ,
        PagingAndSortingRepository<Blogs, Long> {

    @Override
    default void customize(QuerydslBindings bindings, QBlogs blogs) {
        bindings.bind(blogs.content).first(StringExpression::containsIgnoreCase);
    }
    Page<Blogs> findByAuthor_Username(String username,
                                      Pageable pageable);

    List<Blogs> findByIsActive(ActiveEnum active);
}
