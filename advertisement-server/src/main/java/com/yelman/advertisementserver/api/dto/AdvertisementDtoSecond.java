package com.yelman.advertisementserver.api.dto;

import com.yelman.advertisementserver.model.Category;
import com.yelman.advertisementserver.model.enums.CurrencyEnum;
import com.yelman.advertisementserver.model.enums.SellerTypeEnum;
import com.yelman.advertisementserver.model.enums.StateEnum;
import com.yelman.advertisementserver.model.enums.StatusEnum;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementDtoSecond {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private long categories;
    private Integer rate;
    private Integer stock;
    private StatusEnum status; // e.g., satılık, satılmadı
    private long userStories; // UserStore modelinin id'si
    private StateEnum state; // sıfır veya ikinci el
    private SellerTypeEnum sellerType;
    private String detail;
    private Integer visionCount;
    private CurrencyEnum currency;

}
