package com.yelman.shopingserver.api.controller;

import com.yelman.shopingserver.api.dto.ShopCommentDto;
import com.yelman.shopingserver.sevice.CommentServices;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopping/comment")
public class CommentController {
    private final CommentServices commentService;

    public CommentController(CommentServices commentService) {
        this.commentService = commentService;
    }
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('SUBSCRIBE','OWNER')")
    public ResponseEntity<Void> addComment(@RequestBody ShopCommentDto comment) {
        return commentService.addComment(comment);
    }
    @PreAuthorize("hasAnyRole('SUBSCRIBE','OWNER')")
    @PostMapping("/sub-comment")
    public ResponseEntity<Void> addSubComment(@RequestBody ShopCommentDto comment) {
        return commentService.addSubComment(comment);
    }
    //alt yprumlar da silinir
    @PreAuthorize("hasAnyRole('SUBSCRIBE','OWNER','ADMIN')")
    @DeleteMapping("/delete/{comment}/{userId}")
    public ResponseEntity<Void> deleteParentComment(@PathVariable long comment,@PathVariable long userId) {
        return commentService.deleteParentComment(comment, userId);
    }
    @PreAuthorize("hasAnyRole('SUBSCRIBE','OWNER','ADMIN')")
    @DeleteMapping("/sub-delete/{comment}/{userId}")
    public ResponseEntity<Void> deleteSubComment(@PathVariable long comment,@PathVariable long userId) {
        return commentService.deleteSubComment(comment, userId);
    }
    @PreAuthorize("hasAnyRole('SUBSCRIBE','OWNER','ADMIN')")
    @PutMapping("/update/{commentId}")
    public ResponseEntity<Void> UpdateComment(@RequestParam long userId,
                                              @RequestParam String content,
                                              @PathVariable long commentId) {
        return commentService.updateComment(userId,content,commentId);
    }


}
