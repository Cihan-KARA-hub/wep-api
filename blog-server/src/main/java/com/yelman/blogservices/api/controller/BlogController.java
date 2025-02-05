package com.yelman.blogservices.api.controller;

import com.yelman.blogservices.api.dto.BlogDto;
import com.yelman.blogservices.api.dto.BlogPatchDto;
import com.yelman.blogservices.services.BlogServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController

@RequestMapping("/blog/")
public class BlogController {
    private final BlogServices blogServices;

    public BlogController(BlogServices blogServices) {
        this.blogServices = blogServices;
    }
    @PreAuthorize("hasAnyRole('OWNER','SUBSCRIBE')")
    @PostMapping("create")
    public ResponseEntity<HttpStatus> createBlog(@RequestBody BlogDto blogDto
    ) throws IOException {
        return blogServices.createBlog(blogDto);
    }
    @PreAuthorize("hasAnyRole('OWNER','SUBSCRIBE')")
    @DeleteMapping("delete/{blogId}/{userId}")
    public ResponseEntity<HttpStatus> deleteBlog(@PathVariable long blogId,@PathVariable long userId) {
        return blogServices.deleteBlog(blogId,userId);
    }
    //TODO  patch yapısı kullanıldı bunu dene
    @PreAuthorize("hasAnyRole('OWNER','SUBSCRIBE')")
    @PatchMapping("update/{blogId}/{userId}")
    public ResponseEntity<HttpStatus> updateBlog(
            @RequestBody BlogPatchDto blogUpdateDto,
            @PathVariable long blogId,
            @PathVariable long userId) {
        return  blogServices.patchBlog(blogId,userId,blogUpdateDto);
    }

}
