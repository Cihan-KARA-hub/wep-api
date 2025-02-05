package com.yelman.advertisementserver.api.controller;

import com.yelman.advertisementserver.api.dto.AdvertisementDto;
import com.yelman.advertisementserver.api.dto.AdvertisementDtoSecond;
import com.yelman.advertisementserver.api.dto.AdvertisementUpdateDto;
import com.yelman.advertisementserver.services.AdvertisementService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/advertisement/owner/")
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    public AdvertisementController(final AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;

    }

    //owner olmalı
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("create")
    public ResponseEntity<String> create(@RequestBody final AdvertisementDtoSecond advertisement) {
        return ResponseEntity.ok(advertisementService.addAdvertisement(advertisement));
    }

    // delete
    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> delete(@PathVariable final Long id) {
        return ResponseEntity.ok(advertisementService.deleteById(id));
    }

    // update
    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("update/{userId}")
    public ResponseEntity<String> update(
            @PathVariable final Long userId,
            @RequestBody AdvertisementUpdateDto advertisement) {
        return ResponseEntity.ok(advertisementService.update(userId, advertisement));
    }

    // kendi ürünlerini listeler
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("list/{id}")
    public ResponseEntity<Page<AdvertisementDto>> list(@PathVariable long id,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "1") int size) {
        return ResponseEntity.ok(advertisementService.getStoreAdvertisement(id, page, size));
    }


}
