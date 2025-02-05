package com.yelman.shopingserver.model;

import com.yelman.shopingserver.model.enums.CurrencyEnum;
import com.yelman.shopingserver.model.enums.SellerTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shopping_product")
public class Product  {

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
    @JoinColumn(name = "shopping_categories_id", nullable = false)
    private Category shoppingCategory;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "seo_slug", nullable = true, unique = true, length = 200)
    private String seoSlug;

    @ManyToOne
    @JoinColumn(name = "shopping_product_shopping_store_id", nullable = false)
    private Store shoppingStore;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "seller_type", nullable = false, length = 20)
    private SellerTypeEnum sellerType;

    @Column(name = "detail", nullable = false, columnDefinition = "TEXT")
    private String detail;

    @Column(name = "vision_count")
    private Integer visionCount;

    @Column(name = "brand")
    private String brand;

    @Column(name = "rate_count")
    private Integer rateCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private CurrencyEnum currency;
}
