package com.yelman.blogservices.services;

import com.yelman.blogservices.model.Blogs;
import com.yelman.blogservices.model.Category;
import com.yelman.blogservices.model.enums.ActiveEnum;
import com.yelman.blogservices.repository.BlogRepository;
import com.yelman.blogservices.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminServices {

    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;


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

    //blog aktifligini iptal etme
    @Transactional
    public ResponseEntity<Void> rejectBlog(long blogId) {
        Optional<Blogs> blog = blogRepository.findById(blogId);
        if (blog.isPresent()) {
            if (blog.get().getIsActive() == ActiveEnum.ACTIVE || blog.get().getIsActive() == ActiveEnum.WAITING) {
                blog.get().setIsActive(ActiveEnum.REJECTED);
                blogRepository.save(blog.get());
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    //blog silme
    public ResponseEntity<Void> deleteBlog(long blogId) {
        Optional<Blogs> blog = blogRepository.findById(blogId);
        if (blog.isPresent()) {
            blogRepository.delete(blog.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // kategori ekleme
    public ResponseEntity<Void> createCategory(String name) {
        Category entity = new Category();
        entity.setName(name);
        entity.setParentId(0);
        categoryRepository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //kategori silme
    public ResponseEntity<Void> DeleteCategory(long id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    //alt kategori ekleme
    public ResponseEntity<Void> subCategory(long parentId, String name) {
        Category cat = categoryRepository.findById(parentId);
        if (cat == null || cat.getParentId() != 0) {
            return ResponseEntity.notFound().build();
        }
        Category newCategory = new Category();
        newCategory.setName(name);
        newCategory.setParentId(parentId);

        categoryRepository.save(newCategory);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // belemede olan blokları getir
    public ResponseEntity<HttpStatus> getWaitingBlogs(ActiveEnum activeEnum) {
        List<Blogs> waitingBlogs = blogRepository.findByIsActive(activeEnum);
        if (waitingBlogs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
