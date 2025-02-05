package com.yelman.shopingserver.api.controller;

import com.yelman.shopingserver.utils.ExcelServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/shopping/categories-manager")
public class CategoryManagerController {
    public final ExcelServices excelServices;
    public CategoryManagerController(ExcelServices excelServices) {
        this.excelServices = excelServices;
    }
    @PreAuthorize("hasRole('ADMIN')")
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
}
