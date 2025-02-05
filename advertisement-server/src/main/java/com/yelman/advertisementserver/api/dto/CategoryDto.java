package com.yelman.advertisementserver.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto implements Serializable {
    private Long id;
    private String name;
    private long parentId;

}
