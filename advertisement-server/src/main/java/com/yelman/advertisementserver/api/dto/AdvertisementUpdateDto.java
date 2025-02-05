package com.yelman.advertisementserver.api.dto;

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
public class AdvertisementUpdateDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private long categories;
    private Integer stock;
    private StatusEnum status; // e.g., satılık, satılmadı
    private StateEnum state; // sıfır veya ikinci el
    private SellerTypeEnum sellerType;
    private String detail;
    private CurrencyEnum currency;
}
