package com.yelman.photoserver.services;


import com.yelman.photoserver.api.dto.Role;
import com.yelman.photoserver.model.BlogPhotos;
import com.yelman.photoserver.repository.BlogPhotosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BlogPhotoServices {

    private final BlogPhotosRepository photoRepository;

    public BlogPhotoServices(BlogPhotosRepository photoRepository) {
        this.photoRepository = photoRepository;
    }


    // Fotoğraf ekleme
    @Transactional
    public ResponseEntity<BlogPhotos> addPhoto(MultipartFile file, Long blogId, String name, long userId, Role role) throws IOException {
        if (role == Role.ROLE_ADMIN || role == Role.ROLE_OWNER) {
            byte[] imageData = file.getBytes();
            BlogPhotos photo = new BlogPhotos();
            photo.setImageData(imageData);
            photo.setBlogId(blogId);
            photo.setUserId(userId);
            photo.setBlogName(name);
            BlogPhotos savedPhoto = photoRepository.save(photo);
            System.out.println("Fotoğraf kaydedildi: ");
            return ResponseEntity.ok(savedPhoto);
        }
        return ResponseEntity.notFound().build();
    }

    // Tüm fotoğrafları listele
    public List<BlogPhotos> getAllPhotos() {
        return photoRepository.findAll();
    }


    // Fotoğrafı silme
    public ResponseEntity<BlogPhotos> deletePhoto(long id, long userId) {
        return ResponseEntity.ok(photoRepository.findByIdAndUserId(id, userId));
    }

    public ResponseEntity<byte[]> getById(long photoId) {
        Optional<BlogPhotos> photo = photoRepository.findById(photoId);

        if (photo.isPresent()) {
            byte[] imageData = photo.get().getImageData();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Veya IMAGE_PNG
                    .body(imageData);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fotoğraf bulunamadı");
        }
    }

    @Transactional
    public ResponseEntity<List<BlogPhotos>> listBlogPhoto(List<Long> id) {
        List<BlogPhotos> photos = photoRepository.findAllById(id);
        if (photos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(photos);
    }

    public ResponseEntity<List<BlogPhotos>> listBlogPhotoByBlogId(Long blogId) {
        List<BlogPhotos> photo = photoRepository.findByBlogId(blogId
        );
        if (photo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(photo);
    }
}
