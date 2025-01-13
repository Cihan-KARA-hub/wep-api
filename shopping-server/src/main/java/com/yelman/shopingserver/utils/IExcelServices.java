package com.yelman.shopingserver.utils;

import org.springframework.web.multipart.MultipartFile;

public interface IExcelServices {
    void processExcelFile(MultipartFile file) throws Exception;
}
