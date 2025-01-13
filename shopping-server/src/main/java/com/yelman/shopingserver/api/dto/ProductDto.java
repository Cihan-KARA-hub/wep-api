package com.yelman.shopingserver.api.dto;

import com.yelman.shopingserver.model.enums.CurrencyEnum;
import com.yelman.shopingserver.model.enums.SellerTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Long shoppingCategoryId;
    private Integer rate;
    private Integer stock;
    private String brand;
    private Long shoppingStoreId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private SellerTypeEnum sellerType;
    private String detail;
    private Integer visionCount;
    private CurrencyEnum currency;
    private  int rateCount;
}
