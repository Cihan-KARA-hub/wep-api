package com.yelman.advertisementserver.model.enums;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.io.Serializable;


public enum  AdvertisementOrdersEnum implements Serializable
{
    desc,
    asc
}

