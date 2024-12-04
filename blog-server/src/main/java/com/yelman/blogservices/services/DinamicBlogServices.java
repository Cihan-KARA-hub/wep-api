package com.yelman.blogservices.services;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yelman.blogservices.model.Blogs;
import com.yelman.blogservices.model.QBlogs;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DinamicBlogServices {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Blogs> getProductsByCategory(String category) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QBlogs product = QBlogs.blogs;

        BooleanExpression categoryFilter = product.content.eq(category);
        return queryFactory.selectFrom(product)
                .where(categoryFilter)
                .fetch();
    }
}
