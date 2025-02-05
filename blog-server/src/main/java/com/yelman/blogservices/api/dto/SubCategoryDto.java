package com.yelman.blogservices.api.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDto  implements Serializable {
    private long parentId;
    private String name;
}
