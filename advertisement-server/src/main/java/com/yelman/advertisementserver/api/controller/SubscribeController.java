package com.yelman.advertisementserver.api.controller;

import com.yelman.advertisementserver.api.dto.CommentDto;
import com.yelman.advertisementserver.api.dto.OfferDto;
import com.yelman.advertisementserver.api.dto.QuestionsDto;
import com.yelman.advertisementserver.services.AdvertisementService;
import com.yelman.advertisementserver.services.CommentServices;
import com.yelman.advertisementserver.services.OfferServices;
import com.yelman.advertisementserver.services.QuestionsServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
    @RequestMapping("/advertisement/subscribe/")
public class SubscribeController {

    private final CommentServices commentServices;
    private final QuestionsServices questionsServices;
    private final OfferServices offerServices;
    private final AdvertisementService advertisementService;

    public SubscribeController(CommentServices commentService, QuestionsServices questionsServices, OfferServices offerServices, AdvertisementService advertisementService) {
        this.commentServices = commentService;
        this.questionsServices = questionsServices;
        this.offerServices = offerServices;
        this.advertisementService = advertisementService;
    }

    @PreAuthorize("hasRole('SUBSCRIBE')")
    @PostMapping("create-comment")
    public ResponseEntity<HttpStatus> createComment(@RequestBody CommentDto comment) {
        return commentServices.createComment(comment);
    }

    @PreAuthorize("hasRole('SUBSCRIBE')")
    @DeleteMapping("{commentId}/{userId}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable Long commentId, @PathVariable Long userId) {
        return commentServices.deleteComment(commentId, userId);
    }

    @PreAuthorize("hasRole('SUBSCRIBE')")
    @PutMapping("update/{userId}/{blogId}")
    public ResponseEntity<HttpStatus> updateComment(@PathVariable long userId, @PathVariable long blogId, @RequestBody String comment) {
        return commentServices.updateComment(blogId, comment, userId);
    }

    //sık sorulan ana sorular  sorulan sorular
    @PreAuthorize("hasRole('SUBSCRIBE')")
    @PostMapping("create-questions")
    ResponseEntity<HttpStatus> addQuestion(@RequestBody QuestionsDto dto) {
        return questionsServices.addQuestion(dto);
    }

    @PreAuthorize("hasRole('SUBSCRIBE')")
    @DeleteMapping("delete-questions/{userId}/{questionId}")
    ResponseEntity<HttpStatus> deleteQuestions(@PathVariable long userId, @PathVariable long questionId) {
        return questionsServices.deleteQuestion(questionId, userId);
    }

    //sorulara olan cevaplar
    @PreAuthorize("hasRole('SUBSCRIBE')")
    @PostMapping("sub-questions/{parentQuestionId}")
    ResponseEntity<HttpStatus> subQuestions(@RequestBody QuestionsDto dto, @PathVariable Long parentQuestionId) {
        return questionsServices.answerQuestion(parentQuestionId, dto);
    }

    //Teklif gönder
    @PreAuthorize("hasRole('SUBSCRIBE')")
    @PostMapping("offer")
    ResponseEntity<HttpStatus> offer(@RequestBody OfferDto dto) {
        return offerServices.postOffer(dto);
    }

    //has role user olacak
    @PreAuthorize("hasRole('SUBSCRIBE')")
    @GetMapping("inc-rate/{advertisementId}/{rate}")
    public void rateIncrease(@PathVariable long advertisementId, @PathVariable int rate) {
        advertisementService.rateProduct(advertisementId, rate);
    }
}
