package com.yelman.advertisementserver.api.dto;

import com.yelman.advertisementserver.model.CategoryModel;
import com.yelman.advertisementserver.model.enums.CurrencyEnum;
import com.yelman.advertisementserver.model.enums.SellerTypeEnum;
import com.yelman.advertisementserver.model.enums.StateEnum;
import com.yelman.advertisementserver.model.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private CategoryDto category; // Category modelinin id'si
    private Integer rate;
    private Integer stock;
    private StatusEnum status; // e.g., satılık, satılmadı
    private String seoSlug;
    private UserStoreDto userStore; // UserStore modelinin id'si
    private StateEnum state; // sıfır veya ikinci el
    private SellerTypeEnum sellerType;
    private String detail;
    private Integer visionCount;
    private CurrencyEnum currency;

}
