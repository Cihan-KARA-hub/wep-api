package com.yelman.photoserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blog_photo")
@Getter
@Setter
public class BlogPhotos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Lob
    @Column(name = "photo_data", nullable = false)
    private byte[] imageData;  // Fotoğraf verisi BYTEA olarak saklanacak
    @Column(name = "blog_id", nullable = false)
    private Long blogId;
    @Column(name = "photo_name", nullable = false)
    private String blogName;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
    @Column(name = "user_id")
    private long userId;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

}
