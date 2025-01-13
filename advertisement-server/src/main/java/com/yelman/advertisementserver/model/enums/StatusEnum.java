package com.yelman.advertisementserver.model.enums;

import lombok.Getter;

public enum StatusEnum {
    FOR_SALE("Satılık"),
    SOLD("Satıldı");

    private final String description;

    StatusEnum(String description) {
        this.description = description;
    }

}
