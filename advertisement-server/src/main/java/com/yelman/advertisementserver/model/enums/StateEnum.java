package com.yelman.advertisementserver.model.enums;

import java.io.Serializable;

public enum StateEnum implements Serializable {
    NEW("Sıfır"),
    SECOND_HAND("İkinci El");

    private final String description;

    StateEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
