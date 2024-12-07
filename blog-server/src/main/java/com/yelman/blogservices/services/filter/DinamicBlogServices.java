package com.yelman.blogservices.services.filter;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yelman.blogservices.model.Blogs;
import com.yelman.blogservices.model.QBlogs;
import com.yelman.blogservices.model.enums.ActiveEnum;
import com.yelman.blogservices.model.enums.ShortLangEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DinamicBlogServices {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Page<Blogs> getBlogsByCategoryName(String categoryName, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QBlogs blogs = QBlogs.blogs;
        long total = queryFactory.select(blogs.count())
                .from(blogs)
                .where(blogs.category.name.eq(categoryName).and(blogs.isActive.eq(ActiveEnum.ACTIVE)))
                .fetchCount();
        List<Blogs> blogList = queryFactory.selectFrom(blogs)
                .where(blogs.category.name.eq(categoryName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(blogList, pageable, total);
    }

    @Transactional
    public ResponseEntity<Page<Blogs>> getCategoryAndLanguage(String category, ShortLangEnum language, int page, int size) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QBlogs blogs = QBlogs.blogs;
        Pageable pageable = PageRequest.of(page, size);
        List<Blogs> products = queryFactory.selectFrom(blogs)
                .where(blogs.category.name.eq(category)
                        .and(blogs.shortLang.eq(ShortLangEnum.Tr))
                        .and(blogs.isActive.eq(ActiveEnum.ACTIVE)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(blogs.shortLang.asc())
                .fetch();
        if (blogs.id == null) return ResponseEntity.ok().build();
        long total = queryFactory.selectFrom(blogs)
                .where(blogs.category.name.eq(category)
                        .and(blogs.shortLang.eq(language)))
                .fetchCount();

        return ResponseEntity.ok(new PageImpl<>(products, pageable, total));
    }

    // aktif basif veya  yayından kaldırılmış olan blogları listele
    @Transactional
    public ResponseEntity<List<Blogs>> getIsActiveBlogs(ActiveEnum isActive) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QBlogs blogs = QBlogs.blogs;
        List<Blogs> blogs1 = queryFactory.selectFrom(blogs)
                .where(blogs.isActive.eq(isActive))
                .orderBy(blogs.shortLang.asc())
                .fetch();
        if (!blogs1.isEmpty()) return ResponseEntity.ok(blogs1);
        else return ResponseEntity.ok().build();
    }

    //en yeni bloglar
    @Transactional
    public ResponseEntity<Page<Blogs>> getNewEstBlogs(int page, int size) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QBlogs blogs = QBlogs.blogs;
        Pageable pageable = PageRequest.of(page, size);
        List<Blogs> blogs1 = queryFactory.selectFrom(blogs)
                .where(blogs.isActive.eq(ActiveEnum.ACTIVE))
                .orderBy(blogs.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory.select(blogs.count())
                .from(blogs)
                .where(blogs.isActive.eq(ActiveEnum.ACTIVE))
                .fetchOne();
        Page<Blogs> pageResult = new PageImpl<>(blogs1, pageable, total);
        return ResponseEntity.ok(pageResult);


    }

}
