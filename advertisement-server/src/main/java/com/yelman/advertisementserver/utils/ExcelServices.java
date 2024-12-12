package com.yelman.advertisementserver.utils;

import com.yelman.advertisementserver.model.CategoryModel;
import com.yelman.advertisementserver.repository.CategoryRepository;
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

            CategoryModel lastParentCategory = null; // En son kaydedilen ana kategori

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // İlk satırı atla (başlık satırı)

                Cell cell1 = row.getCell(0); // Ana kategori
                Cell cell2 = row.getCell(1); // Alt kategori

                if (cell1 != null && cell1.getStringCellValue() != null && !cell1.getStringCellValue().isEmpty()) {
                    // Ana kategori varsa, yeni bir ana kategori oluştur
                    CategoryModel parentCategory = new CategoryModel();
                    parentCategory.setName(cell1.getStringCellValue());
                    parentCategory.setParentId(0); // Ana kategorilerin parentId'si 0
                    lastParentCategory = categoryRepository.save(parentCategory);
                }

                if (cell2 != null && cell2.getStringCellValue() != null && !cell2.getStringCellValue().isEmpty() && lastParentCategory != null) {
                    // Alt kategori varsa, en son kaydedilen ana kategoriye bağla
                    CategoryModel subCategory = new CategoryModel();
                    subCategory.setName(cell2.getStringCellValue());
                    subCategory.setParentId(lastParentCategory.getId()); // Alt kategoriyi ana kategoriye bağla
                    categoryRepository.save(subCategory);
                }
            }
        }
    }

}
