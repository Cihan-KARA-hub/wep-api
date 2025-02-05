package com.yelman.blogservices.services;

import com.yelman.blogservices.model.blog.Comments;
import com.yelman.blogservices.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public ResponseEntity<HttpStatus> saveComment(Comments comment) {
        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        commentRepository.save(comment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Transactional
    public ResponseEntity<HttpStatus> deleteComment(long id, String username) {
       final  Comments comments;
        comments = getCommentId(id);
        if (comments == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!Objects.equals(username, comments.getUser().getUsername())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        boolean ids = commentRepository.deleteById(id);
        if (ids) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @Transactional
    public ResponseEntity<HttpStatus> updateComment(long id, String content, String username) {
      final  Comments comment = getCommentId(id);
        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!Objects.equals(username, comment.getUser().getUsername())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        comment.setContent(content);
        comment.setUpdatedAt(OffsetDateTime.now());
        commentRepository.save(comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<Page<Comments>> getBlogsIdComments(long blogId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comments> comments = commentRepository.findByBlog_Id(blogId, pageable);
        if (comments.getTotalElements() > 0) {
            return ResponseEntity.ok(comments);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    private Comments getCommentId(long id){
      return  commentRepository.findById(id).orElse(null);
    }
}
