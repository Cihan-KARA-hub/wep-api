package com.yelman.advertisementserver.model.enums;

import lombok.Getter;


public enum CurrencyEnum {
    USD("USD", "$"),
    EUR("EUR", "€"),
    GBP("GBP", "£"),
    JPY("JPY", "¥"),
    AUD("AUD", "A$"),
    CAD("CAD", "CA$"),
    CHF("CHF", "CHF"),
    CNY("CNY", "¥"),
    SEK("SEK", "kr"),
    NZD("NZD", "NZ$"),
    MXN("MXN", "$"),
    INR("INR", "₹"),
    BRL("BRL", "R$"),
    ZAR("ZAR", "R"),
    RUB("RUB", "₽"),
    TRY("TRY", "₺"),
    KRW("KRW", "₩"),
    SGD("SGD", "S$"),
    HKD("HKD", "HK$"),
    NOK("NOK", "kr"),
    PLN("PLN", "zł");

    private final String code;
    private final String symbol;

    CurrencyEnum(String code, String symbol) {
        this.code = code;
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }
}
