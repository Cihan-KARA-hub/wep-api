package com.yelman.photoserver.services;

import com.yelman.photoserver.model.AdvertisementPhoto;
import com.yelman.photoserver.repository.AdvertisementRepository;
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
public class AdvertisementServices {

    private final AdvertisementRepository photoRepository;

    public AdvertisementServices(AdvertisementRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Transactional
    public ResponseEntity<AdvertisementPhoto> addPhoto(MultipartFile file, Long blogId, String name) throws IOException {
        byte[] imageData = file.getBytes();
        AdvertisementPhoto photo = new AdvertisementPhoto();
        photo.setPhotoData(imageData);
        photo.setAdvertisementId(blogId);
        photo.setPhotoName(name);
        AdvertisementPhoto savedPhoto = photoRepository.save(photo);
        System.out.println("Fotoğraf kaydedildi: ");
        return ResponseEntity.ok(savedPhoto);
    }

    public List<AdvertisementPhoto> getAllPhotos() {
        return photoRepository.findAll();
    }

    public void deletePhoto(long id) {
        photoRepository.deleteById(id);
    }

    public ResponseEntity<byte[]> getById(long photoId) {
        Optional<AdvertisementPhoto> photo = photoRepository.findById(photoId);

        if (photo.isPresent()) {
            byte[] imageData = photo.get().getPhotoData();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Veya IMAGE_PNG
                    .body(imageData);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fotoğraf bulunamadı");
        }
    }

    @Transactional
    public ResponseEntity<List<AdvertisementPhoto>> listBlogPhoto(List<Long> id) {
        List<AdvertisementPhoto> photos = photoRepository.findAllById(id);
        if (photos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(photos);
    }

}
