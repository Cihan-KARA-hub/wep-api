package com.yelman.shopingserver.api.controller;

import com.yelman.shopingserver.api.dto.ProductDto;
import com.yelman.shopingserver.model.enums.CurrencyEnum;
import com.yelman.shopingserver.model.enums.OrdersEnum;
import com.yelman.shopingserver.model.enums.SellerTypeEnum;
import com.yelman.shopingserver.sevice.ProductServices;
import com.yelman.shopingserver.sevice.QueryServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/shopping/product")
public class ProductController {
    private final ProductServices productServices;
    private final QueryServices queryServices;

    public ProductController(ProductServices productServices, QueryServices queryServices) {
        this.productServices = productServices;
        this.queryServices = queryServices;
    }

    @PostMapping("/create")
    public ResponseEntity<String> addAdvertisement(@RequestBody ProductDto ad) {
        return productServices.addAdvertisement(ad);
    }

    //satıcı tipine göre arama
    @GetMapping("/seller")
    public ResponseEntity<Page<ProductDto>> findBySellerTypeAndIsActive(@RequestParam SellerTypeEnum sellerType,
                                                                        @RequestParam(defaultValue = "2") int size,
                                                                        @RequestParam(defaultValue = "2") int page) {
        return productServices.findBySellerTypeAndIsActive(sellerType, size, page);
    }

    // stok düşürme
    @PutMapping("/deicrement/{advertisementId}/{deicrement}")
    public ResponseEntity<String> getUserStockDeIcrement(@PathVariable long advertisementId, @PathVariable int deicrement) {
        return productServices.getUserStockDeIcrement(advertisementId, deicrement);
    }

    @PutMapping("/icrement/{advertisementId}/{size}")
    public ResponseEntity<String> getUserStockIcrement(@PathVariable long advertisementId, @PathVariable int size) {
        return productServices.getUserStockIcrement(advertisementId, size);
    }

    // kullanıcı yıldız verme
    @PutMapping("/rate")
    public ResponseEntity<Void> rateProduct(@RequestParam long advertisementId, @RequestParam int rate) {
        return productServices.rateProduct(advertisementId, rate);
    }

    @GetMapping("/search")
    public List<ProductDto> search(@RequestParam String keyword) {
        return queryServices.searchProducts(keyword);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ProductDto>> getCategoryAndBradAndRateAndOrder(@RequestParam(required = false) Long category,
                                                                              @RequestParam(required = false) String brand,
                                                                              @RequestParam(required = false) Integer rate,
                                                                              @RequestParam(required = false) OrdersEnum ordersEnum,
                                                                              @RequestParam(defaultValue = "2") int size,
                                                                              @RequestParam(defaultValue = "2") int page) {
        return queryServices.getCategoryAndBradAndRateAndOrder(category, brand, rate, ordersEnum, page, size);

    }

    // verilen kategorideki fiyat aralıgını verir .
    @GetMapping("/between")
    public ResponseEntity<Page<ProductDto>> getCategoryIdMinMaxPriceBetween(@RequestParam(required = false) Long categoryId,
                                                                            @RequestParam(defaultValue = "10", required = false) BigDecimal minPrice,
                                                                            @RequestParam(required = false) BigDecimal maxPrice,
                                                                            @RequestParam(defaultValue = "10") int size,
                                                                            @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, size);
        return queryServices.getCategoryIdMinMaxPriceBetween(categoryId, minPrice, maxPrice, pageable);

    }

    // para birimine göre listele
    @GetMapping("currency/{currencyEnum}")
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


}
