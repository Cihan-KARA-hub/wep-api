package com.yelman.blogservices.api.controller;

import com.yelman.blogservices.api.dto.BlogDto;
import com.yelman.blogservices.api.dto.CategoryDto;
import com.yelman.blogservices.api.dto.SubCategoryDto;
import com.yelman.blogservices.model.Blogs;
import com.yelman.blogservices.model.enums.ActiveEnum;
import com.yelman.blogservices.services.AdminServices;
import com.yelman.blogservices.services.filter.DinamicBlogServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/blog/")
public class AdminController {
    private final AdminServices adminServices;
    private final DinamicBlogServices dinamicBlogServices;

    public AdminController(AdminServices adminServices, DinamicBlogServices dinamicBlogServices) {
        this.adminServices = adminServices;
        this.dinamicBlogServices = dinamicBlogServices;
    }

    @PostMapping("accept/{id}")
    public ResponseEntity<Void> acceptBlog(@PathVariable long id) {
        return adminServices.acceptBlog(id);
    }

    @PostMapping("reject/{id}")
    public ResponseEntity<Void> rejectBlog(@PathVariable long id) {
        return adminServices.rejectBlog(id);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable long id) {
        return adminServices.deleteBlog(id);
    }

    @PostMapping("category")
    public ResponseEntity<Void> categoryCreate(@RequestBody CategoryDto dto) {
        return adminServices.createCategory(dto);
    }

    @PostMapping("sub-category")
    public ResponseEntity<Void> categoryCreate(@RequestBody SubCategoryDto dto) {

        return adminServices.subCategory(dto);
    }

    @DeleteMapping("category-delete/{id}")
    public ResponseEntity<Void> categoryDelete(@PathVariable long id) {
        return adminServices.DeleteCategory(id);
    }

    @GetMapping("{isActive}/")
    public ResponseEntity<List<Blogs>> getIsActiveBlogs(@PathVariable ActiveEnum isActive) {
        return  dinamicBlogServices.getIsActiveBlogs(isActive);
    }


}
