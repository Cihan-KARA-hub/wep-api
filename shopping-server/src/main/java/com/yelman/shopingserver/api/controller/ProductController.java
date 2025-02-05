package com.yelman.shopingserver.api.controller;

import com.yelman.shopingserver.api.dto.ProductDto;
import com.yelman.shopingserver.sevice.ProductServices;
import com.yelman.shopingserver.sevice.QueryServices;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


// ürünlerle alakalı controller'ım
@RestController
@RequestMapping("/shopping/product")
public class ProductController {
    private final ProductServices productServices;

    public ProductController(ProductServices productServices) {
        this.productServices = productServices;

    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/create")
    public ResponseEntity<String> addAdvertisement(@RequestBody ProductDto ad) {
        return productServices.addAdvertisement(ad);
    }

    // stok düşürme
    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/deicrement/{advertisementId}/{deicrement}")
    public ResponseEntity<String> getUserStockDeIcrement(@PathVariable long advertisementId, @PathVariable int deicrement) {
        return productServices.getUserStockDeIcrement(advertisementId, deicrement);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/icrement/{advertisementId}/{size}")
    public ResponseEntity<String> getUserStockIncrement(@PathVariable long advertisementId, @PathVariable int size) {
        return productServices.getUserStockIcrement(advertisementId, size);
    }

    // kullanıcı yıldız verme
    @PreAuthorize("hasRole('SUBSCRIBE')")
    @PutMapping("/rate")
    public ResponseEntity<Void> rateProduct(@RequestParam long advertisementId, @RequestParam int rate) {
        return productServices.rateProduct(advertisementId, rate);
    }


    // herhangi bir ürünü silme
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProduct(@RequestParam long id, @RequestParam long userId) {
        return productServices.deleteProduct(id, userId);
    }
}
