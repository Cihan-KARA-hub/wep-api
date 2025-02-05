package com.yelman.advertisementserver.api.dto;

import com.yelman.advertisementserver.model.enums.CurrencyEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferDto implements Serializable {
    private Long id;
    private BigDecimal price;
    private long user;
    private long advertisement;
    private CurrencyEnum currency;


}
