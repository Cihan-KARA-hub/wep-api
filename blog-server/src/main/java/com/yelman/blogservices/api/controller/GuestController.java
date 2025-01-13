package com.yelman.blogservices.api.controller;


import com.yelman.blogservices.api.dto.BlogDto;
import com.yelman.blogservices.api.dto.PageDto;
import com.yelman.blogservices.model.blog.Blogs;
import com.yelman.blogservices.model.enums.ShortLangEnum;
import com.yelman.blogservices.services.BlogServices;
import com.yelman.blogservices.services.filter.DinamicBlogServices;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("guest/blog/")
public class GuestController {
    private final DinamicBlogServices dinamicBlogServices;
    private final BlogServices blogServices;

    public GuestController(DinamicBlogServices dinamicBlogServices, BlogServices blogServices) {
        this.dinamicBlogServices = dinamicBlogServices;
        this.blogServices = blogServices;
    }



    @GetMapping("{categoryId}")
    public ResponseEntity<Page<BlogDto>> getDynamicQuery(
            @PathVariable(required = false)  Long categoryId,
            @RequestParam(required = false)  Long authorId,
            @RequestParam(required = false) ShortLangEnum lang,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return dinamicBlogServices.getDynamicQuery(categoryId,authorId,lang,page,size);
    }

    @GetMapping("asc")
    public ResponseEntity<Page<Blogs>> getBlogsByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return dinamicBlogServices.getNewEstBlogs(page, size);

    }

    @GetMapping("all/{userName}")
    public Page<Blogs> getAllBlogs(@PathVariable String userName, @RequestBody PageDto pageDto) {
        return blogServices.getAllUserBlog(userName, pageDto.getPage(), pageDto.getSize());
    }




}



