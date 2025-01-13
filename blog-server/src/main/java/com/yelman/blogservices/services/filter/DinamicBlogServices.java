package com.yelman.blogservices.services.filter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yelman.blogservices.api.dto.BlogDto;
import com.yelman.blogservices.api.mapper.BlogMapper;
import com.yelman.blogservices.model.blog.Blogs;
import com.yelman.blogservices.model.blog.QBlogs;

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
    private  final BlogMapper blogMapper;

    public DinamicBlogServices(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }


    @Transactional
    public ResponseEntity<Page<BlogDto>> getDynamicQuery(Long categoryId, Long authorId, ShortLangEnum language, int page, int size) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QBlogs blogs = QBlogs.blogs;

        Pageable pageable = PageRequest.of(page, size);

        BooleanBuilder query = new BooleanBuilder();
        if (authorId != null) {
            query.and(blogs.author.id.eq(authorId));
        }
        if (categoryId != null) {
            query.and(blogs.category.id.eq(categoryId));
        }
        query.and(blogs.shortLang.eq(language != null ? language : ShortLangEnum.Tr));
        query.and(blogs.isActive.eq(ActiveEnum.ACTIVE));

        QueryResults<Blogs> results = queryFactory.selectFrom(blogs)
                .where(query)
                .orderBy(blogs.shortLang.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Blogs> blogList = results.getResults();
        long total = results.getTotal();

        if (blogList.isEmpty()) {
            return ResponseEntity.ok(Page.empty());
        }

        // Fotoğraf bilgisi olmadan sadece blogları içeren liste oluşturuluyor
        List<BlogDto> combinedList = blogList.stream()
                .map(blog -> {
                    BlogDto dto = new BlogDto();
                    blogMapper.mapDto(blog);
                    return dto; // Fotoğraf kısmı kaldırıldı
                })
                .toList();

        Page<BlogDto> pageResult = new PageImpl<>(combinedList, pageable, total);

        return ResponseEntity.ok(pageResult);
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
