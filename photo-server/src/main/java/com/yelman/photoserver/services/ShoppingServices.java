package com.yelman.photoserver.services;

import com.yelman.photoserver.model.BlogPhotos;
import com.yelman.photoserver.model.ShoppingPhoto;
import com.yelman.photoserver.repository.ShoppingRepository;
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
public class ShoppingServices {
    private final ShoppingRepository photoRepository;

    public ShoppingServices(ShoppingRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Transactional
    public ResponseEntity<ShoppingPhoto> addPhoto(MultipartFile file, Long blogId, String name) throws IOException {
        byte[] imageData = file.getBytes();
        ShoppingPhoto photo = new ShoppingPhoto();
        photo.setImageData(imageData);
        photo.setShoppingId(blogId);
        photo.setBlog_name(name);
        ShoppingPhoto savedPhoto = photoRepository.save(photo);
        System.out.println("Fotoğraf kaydedildi: " );
        return ResponseEntity.ok(savedPhoto);
    }

    // Tüm fotoğrafları listele
    public List<ShoppingPhoto> getAllPhotos() {
        return photoRepository.findAll();
    }



    // Fotoğrafı silme
    public void deletePhoto(long id) {
        photoRepository.deleteById(id);
    }
    public ResponseEntity<byte[]>  getById(long photoId) {
        Optional<ShoppingPhoto> photo = photoRepository.findById(photoId);

        if (photo.isPresent()) {
            byte[] imageData = photo.get().getImageData();
            return  ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Veya IMAGE_PNG
                    .body(imageData);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fotoğraf bulunamadı");
        }
    }
    @Transactional
    public  ResponseEntity<List<ShoppingPhoto>> listBlogPhoto(List<Long> id)  {
        List<ShoppingPhoto> photos = photoRepository.findAllById(id);
        if(photos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(photos);
    }

}

