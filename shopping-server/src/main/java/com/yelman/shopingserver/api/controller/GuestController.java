package com.yelman.shopingserver.api.controller;

import com.yelman.shopingserver.api.dto.ProductDto;
import com.yelman.shopingserver.api.dto.ShopCommentDto;
import com.yelman.shopingserver.api.dto.UserStoreDto;
import com.yelman.shopingserver.model.enums.CurrencyEnum;
import com.yelman.shopingserver.model.enums.OrdersEnum;
import com.yelman.shopingserver.model.enums.SellerTypeEnum;
import com.yelman.shopingserver.sevice.CommentServices;
import com.yelman.shopingserver.sevice.ProductServices;
import com.yelman.shopingserver.sevice.QueryServices;
import com.yelman.shopingserver.sevice.StoreServices;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@RestController
@RequestMapping("/shopping/guest")
public class GuestController {

    private final QueryServices queryServices;
    private  final CommentServices commentService;
    private final ProductServices productServices;
    private  final StoreServices storeServices;

    public GuestController(QueryServices queryServices, CommentServices commentService, ProductServices productServices, StoreServices storeServices) {
        this.queryServices = queryServices;
        this.commentService = commentService;
        this.productServices = productServices;
        this.storeServices = storeServices;
    }

    @GetMapping("/comments")
    public ResponseEntity<Page<ShopCommentDto>> getParentComment(@RequestParam long productId,
                                                                 @RequestParam(defaultValue = "4") int size,
                                                                 @RequestParam(defaultValue = "1") int page) {
        return commentService.getParentComment(productId, page, size);
    }

    @GetMapping("/sub-comments")
    public ResponseEntity<Page<ShopCommentDto>> getSubComment(@RequestParam long parentId,
                                                              @RequestParam(defaultValue = "4") int size,
                                                              @RequestParam(defaultValue = "1") int page) {
        return commentService.getSubComment(parentId, page, size);
    }

    @GetMapping("/search")
    public List<ProductDto> search(@RequestParam String keyword) {
        return queryServices.searchProducts(keyword);
    }

    // anasayfada verilen tasarıma göre  genel bir filtreleme yaptım
    @GetMapping("/filter")
    public ResponseEntity<Page<ProductDto>> getCategoryAndBradAndRateAndOrder(@RequestParam(required = false) Long category,
                                                                              @RequestParam(required = false) String brand,
                                                                              @RequestParam(required = false) Integer rate,
                                                                              @RequestParam(required = false) OrdersEnum ordersEnum,
                                                                              @RequestParam(defaultValue = "2") int size,
                                                                              @RequestParam(defaultValue = "2") int page) {
        return ResponseEntity.ok(queryServices.getCategoryAndBradAndRateAndOrder(category, brand, rate, ordersEnum, page, size));

    }

    // verilen kategorideki fiyat aralıgını verir .
    @GetMapping("/between")
    public ResponseEntity<Page<ProductDto>> getCategoryIdMinMaxPriceBetween(@RequestParam(required = false) Long categoryId,
                                                                            @RequestParam(defaultValue = "10", required = false) BigDecimal minPrice,
                                                                            @RequestParam(required = false) BigDecimal maxPrice,
                                                                            @RequestParam(defaultValue = "10") int size,
                                                                            @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(queryServices.getCategoryIdMinMaxPriceBetween(categoryId, minPrice, maxPrice, pageable));

    }

    // para birimine göre listele
    @GetMapping("/currency/{currencyEnum}")
    public ResponseEntity<Page<ProductDto>> getCurrency(@PathVariable CurrencyEnum currencyEnum,
                                                        @RequestParam(defaultValue = "1") int size,
                                                        @RequestParam(defaultValue = "1") int page) {
        return productServices.getCurrency(currencyEnum, page, size);
    }

    // id'si verilen magzanın  ürülerini sıralar
    @GetMapping("/store/{storeId}")
    public ResponseEntity<Page<ProductDto>> getStoreList(@PathVariable long storeId,
                                                         @RequestParam(defaultValue = "1") int size,
                                                         @RequestParam(defaultValue = "1") int page) {
        return productServices.getStoreList(size, page, storeId);
    }
    //satıcı tipine göre arama
    @GetMapping("/seller")
    public ResponseEntity<Page<ProductDto>> findBySellerTypeAndIsActive(@RequestParam SellerTypeEnum sellerType,
                                                                        @RequestParam(defaultValue = "2") int size,
                                                                        @RequestParam(defaultValue = "2") int page) {
        return ResponseEntity.ok(productServices.findBySellerTypeAndIsActive(sellerType, size, page));
    }
    // verilen ülke il ilçe şehirlere göre arama yapılır

    @GetMapping("/location")
    public ResponseEntity<Page<UserStoreDto>> getStoreCountryAndCity(
            @RequestParam(defaultValue = "Turkey") String country,
            @RequestParam String city,
            @RequestParam String district,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size) {
        return ResponseEntity.ok(queryServices.getCountOrCityOrDistrict(country, city, district, page, size));
    }
    //kategorilerine göre listele
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<UserStoreDto>> GetUserStoreIsCategory(@PathVariable String category,
                                                                     @RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "1") int size) {
        return storeServices.GetUserStoreIsCategory(category, size, page);
    }

}
