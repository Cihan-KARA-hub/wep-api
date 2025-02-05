package com.yelman.blogservices.repository;

import com.yelman.blogservices.model.blog.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comments,Long>, PagingAndSortingRepository<Comments, Long> {
    Page<Comments> findByBlog_Id(long blogId, Pageable pageable);
    boolean deleteById(long id);
}
