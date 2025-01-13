package com.yelman.photoserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blog_photo")
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

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public String getBlogName() {
        return blogName;
    }

    public Long getBlogId() {
        return blogId;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public long getId() {
        return id;
    }

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public void setId(long id) {
        this.id = id;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
