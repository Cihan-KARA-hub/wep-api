package com.yelman.identityserver.model.role;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

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

    @Override
    public String getAuthority() {
        return name();
    }
}