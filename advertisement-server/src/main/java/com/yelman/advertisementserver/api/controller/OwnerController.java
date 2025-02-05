package com.yelman.advertisementserver.api.controller;

import com.yelman.advertisementserver.api.dto.UserStoreDto;
import com.yelman.advertisementserver.model.UserStore;
import com.yelman.advertisementserver.services.AdvertisementService;
import com.yelman.advertisementserver.services.UserStoreServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/advertisement/owner/")
public class OwnerController {
    private final UserStoreServices userStoreServices;
    private final AdvertisementService advertisementService;

    public OwnerController(UserStoreServices userStoreServices, AdvertisementService advertisementService) {
        this.userStoreServices = userStoreServices;
        this.advertisementService = advertisementService;
    }
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("store-create")
    public ResponseEntity<UserStore> createOwnerStore(@RequestBody UserStoreDto dto) {
        return userStoreServices.CreateUserStore(dto);
    }
    //stok düşürme işlemi
    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("stock/{adId}/{stock}")
    public ResponseEntity<String> getUserStockDis(@PathVariable Long adId,@PathVariable int stock) {
        return advertisementService.getUserStockDis(adId,stock);
    }
    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("stock-icr/{adId}/{stock}")
    public ResponseEntity<String> getUserStockIcremnent(@PathVariable Long adId,@PathVariable int stock) {
        return advertisementService.getUserStockIcrement(adId,stock);
    }
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("update")
    public ResponseEntity<HttpStatus> updateUserStore(@RequestBody UserStoreDto dto) {
        return  userStoreServices.UpdateUserStore(dto);
    }
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @DeleteMapping("delete/{storeId}/{userId}")
    public ResponseEntity<HttpStatus> deleteUserStore(@PathVariable Long storeId, @PathVariable Long userId) {
        return  userStoreServices.deleteUserStore(storeId,userId);
    }

}
