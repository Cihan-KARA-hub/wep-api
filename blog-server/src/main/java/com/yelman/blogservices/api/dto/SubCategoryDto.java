package com.yelman.blogservices.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDto {
    private long parentId;
    private String name;
}
