package com.yelman.blogservices.api.controller;

import com.yelman.blogservices.model.blog.Comments;
import com.yelman.blogservices.services.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController

@RequestMapping("/blog-comment/")
public class BlogCommentController {

    private final CommentService commentService;

    public BlogCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasAnyRole('OWNER','SUBSCRIBE')")
    @PostMapping("create-comment")
    public ResponseEntity<HttpStatus> saveComment(@RequestBody Comments comments) {
        return commentService.saveComment(comments);
    }
    @PreAuthorize("hasAnyRole('OWNER','SUBSCRIBE')")
    @PutMapping("update-comment")
    public ResponseEntity<HttpStatus> updateComment(@RequestBody long id, String content, String username) {
        return commentService.updateComment(id, content, username);
    }
    @PreAuthorize("hasAnyRole('OWNER','SUBSCRIBE')")
    @DeleteMapping("{username}/{blogId}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable long blogId, String username) {
        return commentService.deleteComment(blogId, username);
    }

}
