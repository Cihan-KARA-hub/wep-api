package com.yelman.advertisementserver.utils.excel;

import org.springframework.web.multipart.MultipartFile;

public interface IExcelServices {
    void processExcelFile(MultipartFile file) throws Exception;
}
