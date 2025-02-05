package com.yelman.advertisementserver.model.enums;

import lombok.Getter;

import java.io.Serializable;

public enum StatusEnum implements Serializable {
    FOR_SALE("Satılık"),
    SOLD("Satıldı");

    private final String description;

    StatusEnum(String description) {
        this.description = description;
    }

}
