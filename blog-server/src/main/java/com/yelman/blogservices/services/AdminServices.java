package com.yelman.blogservices.services;

import com.yelman.blogservices.model.ActiveEnum;
import com.yelman.blogservices.model.Blogs;
import com.yelman.blogservices.repository.BlogRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminServices {

    private final BlogRepository blogRepository;


    // beklemede olan bir makaleyi adminin onaylaması
    @Transactional
    public ResponseEntity<Void> acceptBlog(long blogId) {
        Optional<Blogs> blog = blogRepository.findById(blogId);
        if (blog.isPresent()) {
            if (blog.get().getIsActive() == ActiveEnum.WAITING || blog.get().getIsActive() == ActiveEnum.REJECTED) {
                blog.get().setIsActive(ActiveEnum.ACTIVE);
                blogRepository.save(blog.get());
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
    @Transactional
    public ResponseEntity<Void> rejectBlog(long blogId) {
        Optional<Blogs> blog = blogRepository.findById(blogId);
        if (blog.isPresent()) {
            if (blog.get().getIsActive() == ActiveEnum.ACTIVE || blog.get().getIsActive() == ActiveEnum.WAITING) {
                blog.get().setIsActive(ActiveEnum.REJECTED);
                blogRepository.save(blog.get());
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Void> deleteBlog(long blogId) {
        Optional<Blogs> blog = blogRepository.findById(blogId);
        if (blog.isPresent()) {
            blogRepository.delete(blog.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
