package com.yelman.advertisementserver.model;

import com.yelman.advertisementserver.model.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "advertisement")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "adver_category_id", nullable = false,referencedColumnName = "id")
    private CategoryModel category;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private StatusEnum status; // e.g., satılık, satılmadı

    @Column(name = "seo_slug", nullable = true, unique = true, length = 200)
    private String seoSlug;

    @ManyToOne
    @JoinColumn(name = "advertisement_user_store_id", nullable = false)
    private UserStore userStore; // satılan magza id'si

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;


    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 20)
    private StateEnum state; // sıfır veya ikinci el

    @Enumerated(EnumType.STRING)
    @Column(name = "seller_type", nullable = false, length = 20)
    private SellerTypeEnum sellerType;

    @Column(name = "detail", nullable = false, columnDefinition = "TEXT")
    private String detail;

    @Column(name = "vision_count")
    private Integer visionCount;
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private CurrencyEnum currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_active", nullable = false, length = 20)
    private ActiveEnum isActive;

    @Column(name = "rate_count")
    private Integer rateCount;
}
