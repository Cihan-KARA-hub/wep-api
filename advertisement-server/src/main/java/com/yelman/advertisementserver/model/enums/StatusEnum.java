package com.yelman.advertisementserver.model.enums;

public enum StatusEnum {
    FOR_SALE("Satılık"),
    SOLD("Satıldı");

    private final String description;

    StatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
