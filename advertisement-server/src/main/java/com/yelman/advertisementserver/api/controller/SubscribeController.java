package com.yelman.advertisementserver.api.controller;

import com.yelman.advertisementserver.api.dto.CommentDto;
import com.yelman.advertisementserver.api.dto.OfferDto;
import com.yelman.advertisementserver.api.dto.QuestionsDto;
import com.yelman.advertisementserver.services.CommentServices;
import com.yelman.advertisementserver.services.OfferServices;
import com.yelman.advertisementserver.services.QuestionsServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscribe")
public class SubscribeController {

    private final CommentServices commentServices;
    private final QuestionsServices questionsServices;
    private final OfferServices offerServices;

    public SubscribeController(CommentServices commentService, QuestionsServices questionsServices, OfferServices offerServices) {
        this.commentServices = commentService;
        this.questionsServices = questionsServices;
        this.offerServices = offerServices;
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

    @DeleteMapping("/delete-questions/{userId}/{questionId}")
    ResponseEntity<HttpStatus> deleteQuestions(@PathVariable long userId, @PathVariable long questionId) {
        return questionsServices.deleteQuestion(questionId, userId);
    }

    //sorulara olan cevaplar
    @PostMapping("/sub-questions/{parentQuestionId}")
    ResponseEntity<HttpStatus> subQuestions(@RequestBody QuestionsDto dto, @PathVariable Long parentQuestionId) {
        return questionsServices.answerQuestion(parentQuestionId, dto);
    }
    //Teklif gönder
    @PostMapping("/offer")
    ResponseEntity<HttpStatus> offer(@RequestBody OfferDto dto) {
        return offerServices.postOffer(dto);
    }
}
