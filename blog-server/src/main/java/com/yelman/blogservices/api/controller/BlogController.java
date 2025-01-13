package com.yelman.blogservices.api.controller;

import com.yelman.blogservices.api.dto.BlogDto;
import com.yelman.blogservices.services.BlogServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/blog/")
public class BlogController {
    private final BlogServices blogServices;

    public BlogController(BlogServices blogServices) {
        this.blogServices = blogServices;
    }

    @PostMapping("create")
    public ResponseEntity<HttpStatus> createBlog(@RequestBody BlogDto blogDto
                                            ) throws IOException {
        return blogServices.createBlog(blogDto);
    }

    @PostMapping("{id}")
    public ResponseEntity<Void> incBlog(@PathVariable long id) {
        return blogServices.incrementReadCount(id);
    }

}
