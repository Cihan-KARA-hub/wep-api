package com.yelman.shopingserver.model.enums;


public enum SellerTypeEnum {
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
