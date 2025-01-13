package com.yelman.shopingserver.utils;

import java.text.Normalizer;

public class SeoSlug {
    public static String generateSeoSlug(String text) {
        // Küçük harfe dönüştür
        String slug = text.toLowerCase();

        // Unicode karakterlerini normalleştir
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("[^\\p{ASCII}]", "");

        // Boşlukları tire (-) ile değiştir
        slug = slug.replaceAll("\\s+", "-");

        // Yalnızca harf, rakam ve tire (-) karakterlerine izin ver
        slug = slug.replaceAll("[^a-z0-9-]", "");

        // Başta ve sonda tire varsa temizle
        slug = slug.replaceAll("^-+", "");
        slug = slug.replaceAll("-+$", "");

        return slug;
    }
}
