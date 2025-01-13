package com.yelman.shopingserver.model.enums;

public enum StateEnum {
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
