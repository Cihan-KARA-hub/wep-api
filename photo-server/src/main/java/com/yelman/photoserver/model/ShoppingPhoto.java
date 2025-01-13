package com.yelman.photoserver.model;

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
@Entity
@Table(name = "shopping_photo")
public class ShoppingPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Lob
    @Column(name = "photo_data")
    private byte[] imageData;
    @Column(name = "shopping_id")
    private Long shoppingId;
    @Column(name = "photo_name")
    private String blog_name;
    @CurrentTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @CurrentTimestamp
    @Column(name = "update_at")
    private OffsetDateTime updateAt;
}
