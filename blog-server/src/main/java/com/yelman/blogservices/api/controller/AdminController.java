package com.yelman.blogservices.api.controller;

import com.yelman.blogservices.model.blog.Blogs;
import com.yelman.blogservices.model.enums.ActiveEnum;
import com.yelman.blogservices.services.AdminServices;
import com.yelman.blogservices.services.filter.DinamicBlogServices;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog/admin/")
public class AdminController {
    private final AdminServices adminServices;
    private final DinamicBlogServices dinamicBlogServices;

    public AdminController(AdminServices adminServices, DinamicBlogServices dinamicBlogServices) {
        this.adminServices = adminServices;
        this.dinamicBlogServices = dinamicBlogServices;
    }
    // blog status enable - disabled
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("accept/{id}")
    public ResponseEntity<Void> acceptBlog(@PathVariable long id) {
        return adminServices.acceptBlog(id);
    }
    // blog status enable - disabled
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("reject/{id}")
    public ResponseEntity<Void> rejectBlog(@PathVariable long id) {
        return adminServices.rejectBlog(id);
    }
    // blog status enable - disabled
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable long id) {
        return adminServices.deleteBlog(id);
    }
    //categoryleri ekleyebilir
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("category")
    public ResponseEntity<Void> categoryCreate(@RequestParam String name ) {
        return adminServices.createCategory(name);
    }
    //categoryleri ekleyebilir
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("sub-category")
    public ResponseEntity<Void> categoryCreate(@RequestParam long parentId,String name ) {

        return adminServices.subCategory(parentId,name);
    }
    //categoryleri silebilir
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("category-delete/{id}")
    public ResponseEntity<Void> categoryDelete(@PathVariable long id) {
        return adminServices.DeleteCategory(id);
    }

    @GetMapping("{isActive}/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Blogs>> getIsActiveBlogs(@PathVariable ActiveEnum isActive) {
        return  dinamicBlogServices.getIsActiveBlogs(isActive);
    }



}
