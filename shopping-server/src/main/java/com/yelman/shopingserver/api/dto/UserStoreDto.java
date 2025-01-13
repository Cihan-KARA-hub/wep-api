package com.yelman.shopingserver.api.dto;

import com.yelman.shopingserver.model.enums.ActiveEnum;


public class UserStoreDto {
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

    public UserStoreDto() {

    }

    public UserStoreDto(long id, String email, String phoneNumber, String storeName, String name, String surname, String category, String companyType, String taxCertificate, String taxOffice, String country, String city, ActiveEnum isActive, String district) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.storeName = storeName;
        this.name = name;
        this.surname = surname;
        this.category = category;
        this.companyType = companyType;
        this.taxCertificate = taxCertificate;
        this.taxOffice = taxOffice;
        this.country = country;
        this.city = city;
        this.isActive = isActive;
        this.district = district;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getTaxCertificate() {
        return taxCertificate;
    }

    public void setTaxCertificate(String taxCertificate) {
        this.taxCertificate = taxCertificate;
    }

    public String getTaxOffice() {
        return taxOffice;
    }

    public void setTaxOffice(String taxOffice) {
        this.taxOffice = taxOffice;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ActiveEnum getIsActive() {
        return isActive;
    }

    public void setIsActive(ActiveEnum isActive) {
        this.isActive = isActive;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
