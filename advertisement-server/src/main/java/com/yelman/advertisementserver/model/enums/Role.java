package com.yelman.advertisementserver.model.enums;

import java.io.Serializable;

public enum  Role implements Serializable {

    ROLE_ADMIN("ADMIN"),
    ROLE_SUBSCRIBE("SUBSCRIBE"),
    ROLE_OWNER("OWNER");

    private String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String getAuthority() {
        return name();
    }
}