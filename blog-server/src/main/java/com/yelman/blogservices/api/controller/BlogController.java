package com.yelman.blogservices.api.controller;

import com.yelman.blogservices.api.dto.BlogDto;
import com.yelman.blogservices.services.BlogServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth/subscribe/blog/")
public class BlogController {
    private final BlogServices blogServices;

    public BlogController(BlogServices blogServices) {
        this.blogServices = blogServices;
    }

    @PostMapping("create")
    public ResponseEntity<String> createBlog(@RequestBody BlogDto blogDto) {
        return blogServices.createBlog(blogDto);
    }

    @PostMapping("{id}")
    public ResponseEntity<Void> incBlog(@PathVariable long id) {
        return blogServices.incrementReadCount(id);
    }
}
