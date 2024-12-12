package com.yelman.advertisementserver.api.controller;

import com.yelman.advertisementserver.api.dto.CommentDto;
import com.yelman.advertisementserver.api.dto.QuestionsDto;
import com.yelman.advertisementserver.services.CommentServices;
import com.yelman.advertisementserver.services.QuestionsServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscribe")
public class SubscribeController {

    private final CommentServices commentServices;
    private final QuestionsServices questionsServices;

    public SubscribeController(CommentServices commentService, QuestionsServices questionsServices) {
        this.commentServices = commentService;
        this.questionsServices = questionsServices;
    }

    @PostMapping("/create-comment")
    public ResponseEntity<HttpStatus> createComment(@RequestBody CommentDto comment) {
        return commentServices.createComment(comment);
    }

    @DeleteMapping("/{commentId}/{userId}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable Long commentId, @PathVariable Long userId) {
        return commentServices.deleteComment(commentId, userId);
    }

    //sık sorulan ana sorular  sorulan sorular
    @PostMapping("/create-questions")
    ResponseEntity<HttpStatus> addQuestion(@RequestBody QuestionsDto dto) {
        return questionsServices.addQuestion(dto);
    }

    @DeleteMapping("/questions/{userId}/{questionId}")
    ResponseEntity<HttpStatus> deleteQuestions(@PathVariable Long userId, @PathVariable Long questionId) {
        return questionsServices.deleteQuestion(questionId, userId);
    }

    //sorulara olan cevaplar
    @PostMapping("/sub-questions/{questionId}")
    ResponseEntity<HttpStatus> subQuestions(@RequestBody QuestionsDto dto, @PathVariable Long questionId) {
        return questionsServices.answerQuestion(questionId, dto);
    }
}
