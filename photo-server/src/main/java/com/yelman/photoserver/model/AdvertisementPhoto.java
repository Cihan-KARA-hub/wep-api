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
@Table(name = "advertisement_photo")
public class AdvertisementPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "photo_data", nullable = false)
    private byte[] photoData;  // BYTEA veri tipi ile eşleşen byte[] veri tipi

    @Column(name = "photo_name", nullable = false)
    private String photoName;

    @Column(name = "advertisement_id")
    private Long advertisementId;

}
