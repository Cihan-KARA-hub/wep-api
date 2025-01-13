package com.yelman.shopingserver.api.dto;

import com.yelman.shopingserver.model.Product;
import com.yelman.shopingserver.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopCommentDto {

    private Long id;

    private Long parentId;

    private Long products;  // Assuming you have an Advertisement entity

    private Long users;  // Assuming you have a User entity

    private String content;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
