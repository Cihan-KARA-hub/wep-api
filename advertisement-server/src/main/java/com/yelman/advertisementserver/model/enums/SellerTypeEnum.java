package com.yelman.advertisementserver.model.enums;


import java.io.Serializable;

public enum  SellerTypeEnum implements Serializable {
    MANUFACTURER("Üretici"),
    ONLINE_RETAILER("Çevrimiçi Satıcı"),
    RETAILER("Perakendeci"),
    WHOLESALER("Toptancı"),
    DIRECT_SELLER("Doğrudan Satıcı");

    private final String description;

    SellerTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
