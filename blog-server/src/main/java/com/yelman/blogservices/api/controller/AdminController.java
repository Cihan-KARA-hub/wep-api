package com.yelman.blogservices.api.controller;

import com.yelman.blogservices.services.AdminServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/admin/blog/")
public class AdminController {
    private final AdminServices adminServices;

    public AdminController(AdminServices adminServices) {
        this.adminServices = adminServices;
    }
    @PostMapping("accept/{id}")
    public ResponseEntity<Void> acceptBlog(@PathVariable long id) {
        return adminServices.acceptBlog(id);
    }
    @PostMapping("/reject/{id}")
    public ResponseEntity<Void> rejectBlog(@PathVariable long id) {
        return adminServices.rejectBlog(id);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable long id) {
        return adminServices.deleteBlog(id);
    }
}
