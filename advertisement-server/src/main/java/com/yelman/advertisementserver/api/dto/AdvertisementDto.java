package com.yelman.advertisementserver.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yelman.advertisementserver.model.Category;
import com.yelman.advertisementserver.model.enums.CurrencyEnum;
import com.yelman.advertisementserver.model.enums.SellerTypeEnum;
import com.yelman.advertisementserver.model.enums.StateEnum;
import com.yelman.advertisementserver.model.enums.StatusEnum;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Category category; // Category modelinin id'si
    private Integer rate;
    private Integer stock;
    private StatusEnum status; // e.g., satılık, satılmadı
    private UserStoreDto userStore; // UserStore modelinin id'si
    private StateEnum state; // sıfır veya ikinci el
    private SellerTypeEnum sellerType;
    private String detail;
    private Integer visionCount;
    private CurrencyEnum currency;

}
