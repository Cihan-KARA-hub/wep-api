package com.yelman.advertisementserver.api.controller;

import com.yelman.advertisementserver.api.dto.AdvertisementDto;
import com.yelman.advertisementserver.model.Advertisement;
import com.yelman.advertisementserver.model.enums.AdvertisementOrdersEnum;
import com.yelman.advertisementserver.services.AdvertisementService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Advertisement")
public class AdvertisementController {
    private final AdvertisementService advertisementService;
    public AdvertisementController(final AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;

    }
    //owner olmalı
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody final AdvertisementDto advertisement) {
        return  advertisementService.addAdvertisement(advertisement);
    }






}
