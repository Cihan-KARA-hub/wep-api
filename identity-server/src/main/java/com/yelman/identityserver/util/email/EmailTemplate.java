package com.yelman.identityserver.util.email;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class EmailTemplate {
    public static String getEmailTemplate(String filePath, String name, String link) {
        try {
            InputStream inputStream = EmailTemplate.class.getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new RuntimeException("Şablon dosyası bulunamadı: " + filePath);
            }
            String template = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return template.replace("{{name}}", name).replace("{{link}}", link);
        } catch (IOException e) {
            throw new RuntimeException("Şablon dosyası okunamadı: " + filePath, e);
        }
    }
}
