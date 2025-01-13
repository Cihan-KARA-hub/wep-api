package com.yelman.photoserver.api.controller;

import com.yelman.photoserver.model.AdvertisementPhoto;
import com.yelman.photoserver.services.AdvertisementServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/advertisement")
public class AdvertisementController {
    private final AdvertisementServices photoService;

    public AdvertisementController(AdvertisementServices photoService) {
        this.photoService = photoService;
    }

    // Fotoğraf ekleme
    @PostMapping("/upload/{blogId}/{name}")
    public ResponseEntity<AdvertisementPhoto> uploadPhoto(@RequestParam("file") MultipartFile file,
                                                          @PathVariable Long blogId, @PathVariable String name) throws IOException {
        return photoService.addPhoto(file, blogId, name);

    }

    // Tüm fotoğrafları listeleme
    @GetMapping
    public List<AdvertisementPhoto> getAllPhotos() {
        return photoService.getAllPhotos();
    }


    // Fotoğraf silme
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable int id) {
        photoService.deletePhoto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/image/{photoId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long photoId) {
        return photoService.getById(photoId);
    }

    @GetMapping("/list")
    public ResponseEntity<List<AdvertisementPhoto>> listBlogPhoto(@RequestBody List<Long> photos) {
        return photoService.listBlogPhoto(photos);
    }
}
