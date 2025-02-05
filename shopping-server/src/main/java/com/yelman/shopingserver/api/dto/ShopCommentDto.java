package com.yelman.shopingserver.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopCommentDto {
    private Long id;

    private Long parentId;

    private Long products;

    private Long users;

    private String content;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;


}
