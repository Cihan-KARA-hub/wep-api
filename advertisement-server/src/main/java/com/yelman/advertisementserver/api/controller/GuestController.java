package com.yelman.advertisementserver.api.controller;

import com.yelman.advertisementserver.api.dto.AdvertisementDto;
import com.yelman.advertisementserver.api.dto.CommentDto;
import com.yelman.advertisementserver.api.dto.QuestionsDto;
import com.yelman.advertisementserver.model.Advertisement;
import com.yelman.advertisementserver.model.CategoryModel;
import com.yelman.advertisementserver.model.enums.AdvertisementOrdersEnum;
import com.yelman.advertisementserver.model.enums.SellerTypeEnum;
import com.yelman.advertisementserver.model.enums.StateEnum;
import com.yelman.advertisementserver.services.AdvertisementService;
import com.yelman.advertisementserver.services.CategoryServices;
import com.yelman.advertisementserver.services.CommentServices;
import com.yelman.advertisementserver.services.QuestionsServices;
import com.yelman.advertisementserver.services.dinamic.QueryServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/guest")
public class GuestController {
    private final CategoryServices categoryServices;
    private final QueryServices queryServices;
    private final AdvertisementService advertisementService;
    private final CommentServices commentServices;
    private final QuestionsServices questionsServices;

    public GuestController(final CategoryServices categoryServices, QueryServices queryServices, AdvertisementService advertisementService, CommentServices commentServices, QuestionsServices questionsServices) {
        this.categoryServices = categoryServices;
        this.queryServices = queryServices;
        this.advertisementService = advertisementService;
        this.commentServices = commentServices;
        this.questionsServices = questionsServices;
    }

    @GetMapping("/categories")
    public List<CategoryModel> getAll() {
        return categoryServices.getParentCategories();
    }

    @GetMapping("/{subCategory}")
    public List<CategoryModel> getSubCategories(@PathVariable String subCategory) {
        return categoryServices.getSubCategories(subCategory);
    }

    @GetMapping("/all")
    public Advertisement getAllAdvertisements() {
        return advertisementService.getAllAdvertisements(5);
    }

    // verilen katagorideki ürünleri istenilen şekilde sırala
    @GetMapping("/{category}/{order}")
    public ResponseEntity<Page<AdvertisementDto>> getDenemesByCategory(@PathVariable long category,
                                                                       @PathVariable AdvertisementOrdersEnum order,
                                                                       @RequestParam(defaultValue = "1") int page,
                                                                       @RequestParam(defaultValue = "1") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return queryServices.getAdvertisements(category, order, pageable);
    }

    //istenilen fiyar aralıgına göre sırala
    @GetMapping("/{categoryId}/{minPrice}/{maxPrice}")
    public ResponseEntity<Page<AdvertisementDto>> getCategoryIdMinMaxPriceBetween(
            @PathVariable long categoryId,
            @PathVariable BigDecimal maxPrice,
            @PathVariable BigDecimal minPrice,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return queryServices.getCategoryIdMinMaxPriceBetween(categoryId, minPrice, maxPrice, pageable);
    }

    @GetMapping("/state")
    public ResponseEntity<Page<AdvertisementDto>> findByStateAndIsActive(@RequestParam StateEnum state,
                                                                         @RequestParam(defaultValue = "1") int page,
                                                                         @RequestParam(defaultValue = "1") int size) {

        return advertisementService.findByStateAndIsActive(state, page, size);
    }

    @GetMapping("/seller")
    public ResponseEntity<Page<AdvertisementDto>> findBySellerTypeAndIsActive(@RequestParam SellerTypeEnum seller,
                                                                              @RequestParam(defaultValue = "1") int page,
                                                                              @RequestParam(defaultValue = "1") int size) {

        return advertisementService.findBySellerTypeAndIsActive(seller, size, page);
    }

    //has role user olacak
    @GetMapping("/inc-rate/{advertisementId}/{rate}")
    public ResponseEntity<Void> rateIncrease(@PathVariable long advertisementId, @PathVariable int rate) {
        return advertisementService.rateProduct(advertisementId, rate);
    }

    //yorumları getirir
    @GetMapping("/comments")
    ResponseEntity<Page<CommentDto>> getComment(@RequestParam long advertisementId,
                                                @RequestParam(defaultValue = "5") int page,
                                                @RequestParam(defaultValue = "5") int size) {
        return commentServices.getComment(advertisementId, page, size);
    }

    @GetMapping("/questions")
    public ResponseEntity<Page<QuestionsDto>> getParentQuestion(@RequestParam long advertisementId,
                                                                @RequestParam(defaultValue = "5") int page,
                                                                @RequestParam(defaultValue = "5") int size) {
        return questionsServices.getParentQuestion(advertisementId, page, size);
    }


}
