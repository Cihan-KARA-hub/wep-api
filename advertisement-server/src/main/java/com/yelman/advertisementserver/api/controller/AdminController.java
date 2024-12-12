package com.yelman.advertisementserver.api.controller;

import com.yelman.advertisementserver.api.dto.UserStoreDto;
import com.yelman.advertisementserver.model.enums.ActiveEnum;
import com.yelman.advertisementserver.services.AdminServices;

import com.yelman.advertisementserver.utils.ExcelServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/")
public class AdminController {

    private final ExcelServices excelServices;
    private final AdminServices adminServices;


    //kategory yükleme exceli
    @PostMapping(value = "/upload-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("Dosya eksik veya boş.");
            }
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.endsWith(".xlsx")) {
                return ResponseEntity.badRequest().body("Geçersiz dosya türü. Lütfen .xlsx uzantılı bir dosya yükleyin.");
            }
            excelServices.processExcelFile(file);
            return ResponseEntity.ok("Dosya başarıyla işlendi ve veritabanına kaydedildi.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Dosya işlenirken bir hata oluştu: " + e.getMessage());
        }
    }

    //magzayı aktif etme
    @PutMapping("/store-active/{id}")
    public ResponseEntity<HttpEntity<String>> storeActive(@PathVariable long id) {
        return adminServices.getStoreAccept(id);
    }

    //bekeleme durumundaki ilanları  akif et
    @PostMapping("/advertisement-active/{id}")
    public ResponseEntity<HttpEntity<String>> update(@PathVariable long id) {
        return adminServices.getAdvertisementAccept(id);
    }



}
