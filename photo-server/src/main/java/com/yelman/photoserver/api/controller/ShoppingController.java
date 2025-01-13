package com.yelman.photoserver.api.controller;

import com.yelman.photoserver.model.ShoppingPhoto;
import com.yelman.photoserver.services.ShoppingServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/shopping")
public class ShoppingController {
    private final ShoppingServices photoService;

    public ShoppingController(ShoppingServices photoService) {
        this.photoService = photoService;
    }

    // Fotoğraf ekleme
    @PostMapping("/upload/{blogId}/{name}")
    public ResponseEntity<ShoppingPhoto> uploadPhoto(@RequestParam("file") MultipartFile file,
                                                     @PathVariable Long blogId, @PathVariable String name) throws IOException {
        return photoService.addPhoto(file, blogId, name);

    }

    // Tüm fotoğrafları listeleme
    @GetMapping
    public List<ShoppingPhoto> getAllPhotos() {
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
    public ResponseEntity<List<ShoppingPhoto>> listBlogPhoto(@RequestBody List<Long> photos) {
        return photoService.listBlogPhoto(photos);
    }
}
