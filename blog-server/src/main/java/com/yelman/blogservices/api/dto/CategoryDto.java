package com.yelman.blogservices.api.dto;

import java.io.Serializable;

public class CategoryDto  implements Serializable {
    private String name;

    public CategoryDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
