package com.yelman.shopingserver.utils;


import com.yelman.shopingserver.model.Category;
import com.yelman.shopingserver.repository.CategoryRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Service
public class ExcelServices implements IExcelServices {
    private final CategoryRepository categoryRepository;

    public ExcelServices(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Override
    public void processExcelFile(MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            Category lastParentCategory = null; // En son kaydedilen ana kategori

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // İlk satırı atla (başlık satırı)

                Cell cell1 = row.getCell(0); // Ana kategori
                Cell cell2 = row.getCell(1); // Alt kategori

                if (cell1 != null && cell1.getStringCellValue() != null && !cell1.getStringCellValue().isEmpty()) {
                    // Ana kategori varsa, yeni bir ana kategori oluştur
                    Category parentCategory = new Category();
                    parentCategory.setName(cell1.getStringCellValue());
                    parentCategory.setParentId(0); // Ana kategorilerin parentId'si 0
                    lastParentCategory = categoryRepository.save(parentCategory);
                }

                if (cell2 != null && cell2.getStringCellValue() != null && !cell2.getStringCellValue().isEmpty() && lastParentCategory != null) {
                    // Alt kategori varsa, en son kaydedilen ana kategoriye bağla
                    Category subCategory = new Category();
                    subCategory.setName(cell2.getStringCellValue());
                    subCategory.setParentId(lastParentCategory.getId()); // Alt kategoriyi ana kategoriye bağla
                    categoryRepository.save(subCategory);
                }
            }
        }
    }

}
