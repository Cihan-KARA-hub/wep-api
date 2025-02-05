package com.yelman.advertisementserver.api.dto;

import com.yelman.advertisementserver.model.enums.ActiveEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserStoreDto implements Serializable {
    private long id;
    private String email;
    private String phoneNumber;
    private String storeName;
    private String name;
    private String surname;
    private String category;
    private String companyType;
    private String taxCertificate;
    private String taxOffice;
    private String country;
    private String city;
    private ActiveEnum isActive;
    private String district;
}
