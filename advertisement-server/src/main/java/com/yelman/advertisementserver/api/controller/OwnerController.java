package com.yelman.advertisementserver.api.controller;

import com.yelman.advertisementserver.api.dto.UserStoreDto;
import com.yelman.advertisementserver.model.UserStore;
import com.yelman.advertisementserver.services.AdvertisementService;
import com.yelman.advertisementserver.services.UserStoreServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
public class OwnerController {
    private final UserStoreServices userStoreServices;
    private final AdvertisementService advertisementService;

    public OwnerController(UserStoreServices userStoreServices, AdvertisementService advertisementService) {
        this.userStoreServices = userStoreServices;
        this.advertisementService = advertisementService;
    }

    @PostMapping("/store-create")
    public ResponseEntity<UserStore> createOwnerStore(@RequestBody UserStoreDto dto) {
        return userStoreServices.CreateUserStore(dto);
    }

    @PutMapping("/stock/{adId}")
    public ResponseEntity<String> getUserStockDis(@PathVariable Long adId) {
        return advertisementService.getUserStockDis(adId);
    }

}
