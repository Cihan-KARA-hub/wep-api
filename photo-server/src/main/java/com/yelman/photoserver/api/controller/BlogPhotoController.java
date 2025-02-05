package com.yelman.photoserver.api.controller;

import com.yelman.photoserver.api.dto.ClaimsDto;
import com.yelman.photoserver.api.dto.Role;
import com.yelman.photoserver.config.JwtAuthenticationFilter;
import com.yelman.photoserver.model.BlogPhotos;
import com.yelman.photoserver.services.BlogPhotoServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/photo/blog")
public class BlogPhotoController {

    private final BlogPhotoServices photoService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public BlogPhotoController(BlogPhotoServices photoService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.photoService = photoService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Fotoğraf ekleme
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @PostMapping("/upload/{blogId}/{name}")
    public ResponseEntity<BlogPhotos> uploadPhoto(@RequestParam("file") MultipartFile file,
                                                  @PathVariable Long blogId, @PathVariable String name, HttpServletRequest req) throws IOException {
        ClaimsDto dto = decode(req);
        assert dto != null;
        return photoService.addPhoto(file, blogId, name, dto.getId(), dto.getRole());

    }

    // Tüm fotoğrafları listeleme
    @GetMapping("/list/all")
    public List<BlogPhotos> getAllPhotos() {
        return photoService.getAllPhotos();
    }


    // Fotoğraf silme
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BlogPhotos> deletePhoto(@PathVariable int id, HttpServletRequest req) {
        ClaimsDto dto = decode(req);
        assert dto != null;
        return photoService.deletePhoto(id, dto.getId());

    }

    @GetMapping("/list/image/{photoId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long photoId) {
        return photoService.getById(photoId);
    }

    @GetMapping("/list")
    public ResponseEntity<List<BlogPhotos>> listBlogPhoto(@RequestBody List<Long> photos) {
        return photoService.listBlogPhoto(photos);
    }
    @GetMapping("/list/blogId")
    public ResponseEntity<List<BlogPhotos>> listBlogPhotoByBlogId(@RequestBody Long blogId) {
        return  photoService.listBlogPhotoByBlogId(blogId);
    }

    private ClaimsDto decode(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            Map<String, Object> claimsMap = jwtAuthenticationFilter.getClaims(token);
            long id = (long) claimsMap.get("id");
            Role roles = (Role) claimsMap.get("roles");
            ClaimsDto dto = new ClaimsDto();
            dto.setId(id);
            dto.setRole(roles);
            return dto;
        }
        return null;
    }
}
