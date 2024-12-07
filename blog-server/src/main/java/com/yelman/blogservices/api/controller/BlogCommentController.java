package com.yelman.blogservices.api.controller;

import com.yelman.blogservices.model.Comments;
import com.yelman.blogservices.services.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/blog-comment")
public class BlogCommentController {

    private final CommentService commentService;

    public BlogCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<Page<Comments>> getBlogIdComments(
            @PathVariable long blogId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return commentService.getBlogsIdComments(blogId, page, size);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> saveComment(@RequestBody Comments comments) {
        return commentService.saveComment(comments);
    }

    @PutMapping()
    public ResponseEntity<HttpStatus> updateComment(@RequestBody long id, String content, String username) {
        return commentService.updateComment(id, content, username);
    }

    @DeleteMapping("{username}/{blogId}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable long blogId, String username) {
        return commentService.deleteComment(blogId, username);
    }
}
