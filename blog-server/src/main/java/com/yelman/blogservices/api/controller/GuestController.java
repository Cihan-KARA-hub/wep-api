package com.yelman.blogservices.api.controller;


import com.yelman.blogservices.api.dto.BlogDto;
import com.yelman.blogservices.model.blog.Blogs;
import com.yelman.blogservices.model.blog.Comments;
import com.yelman.blogservices.model.enums.ShortLangEnum;
import com.yelman.blogservices.services.BlogServices;
import com.yelman.blogservices.services.CommentService;
import com.yelman.blogservices.services.filter.DinamicBlogServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog/guest/")
public class GuestController {
    private final DinamicBlogServices dinamicBlogServices;
    private final BlogServices blogServices;
    private final CommentService commentServices;

    public GuestController(DinamicBlogServices dinamicBlogServices, BlogServices blogServices, CommentService commentServices) {
        this.dinamicBlogServices = dinamicBlogServices;
        this.blogServices = blogServices;
        this.commentServices = commentServices;
    }

    @GetMapping("/dynamic-query/{categoryId}")
    public ResponseEntity<Page<BlogDto>> getDynamicQuery(
            @PathVariable(required = false) Long categoryId,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) ShortLangEnum lang,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dinamicBlogServices.getDynamicQuery(categoryId, authorId, lang, page, size));
    }

    @GetMapping("asc")
    public ResponseEntity<Page<Blogs>> getBlogsByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dinamicBlogServices.getNewEstBlogs(page, size));

    }

    @GetMapping("all/{userName}")
    public ResponseEntity<Page<BlogDto>> getAllBlogs(@PathVariable String userName,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(blogServices.getAllUserBlog(userName, pageable));
    }

    @GetMapping("{blogId}")
    public ResponseEntity<Page<Comments>> getBlogIdComments(
            @PathVariable long blogId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return commentServices.getBlogsIdComments(blogId, page, size);
    }

    @PostMapping("{id}")
    public ResponseEntity<Void> incBlog(@PathVariable long id) {
        return blogServices.incrementReadCount(id);
    }
}



