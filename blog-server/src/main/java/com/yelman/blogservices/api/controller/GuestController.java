package com.yelman.blogservices.api.controller;


import com.yelman.blogservices.api.dto.PageDto;
import com.yelman.blogservices.model.Blogs;
import com.yelman.blogservices.model.enums.ShortLangEnum;
import com.yelman.blogservices.services.BlogServices;
import com.yelman.blogservices.services.filter.DinamicBlogServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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


    @GetMapping("/category/{categoryName}")
    public ResponseEntity<Page<Blogs>> getBlogsByCategory(
            @PathVariable String categoryName,
            @PageableDefault(size = 10, page = 5, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Blogs> blogs = dinamicBlogServices.getBlogsByCategoryName(categoryName, pageable);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("{categoryName}")
    public Page<Blogs> getBlogsByCategory(
            @PathVariable String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return dinamicBlogServices.getBlogsByCategoryName(categoryName, pageable);
    }

    @GetMapping("{categoryName}/{lang}")
    public ResponseEntity<Page<Blogs>> getBlogsByCategoryAndLang(
            @PathVariable String categoryName,
            @PathVariable ShortLangEnum lang,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return dinamicBlogServices.getCategoryAndLanguage(categoryName, lang, page, size);
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

    @GetMapping("all-blogs")
    public Page<Blogs> getAllBlogs(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return blogServices.getBlogs(page, size);
    }

    @GetMapping("{category}/")
    public Page<Blogs> getCategoryAndLangBlogs(@PathVariable String category, @RequestBody PageDto pageDto) {
        return blogServices.getCategoryNameAndLanguage(category, pageDto.getPage(), pageDto.getSize());
    }

}



