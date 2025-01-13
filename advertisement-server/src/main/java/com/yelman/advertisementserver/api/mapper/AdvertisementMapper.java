package com.yelman.advertisementserver.api.mapper;

import com.yelman.advertisementserver.api.dto.AdvertisementDto;
import com.yelman.advertisementserver.api.dto.AdvertisementDtoSecond;
import com.yelman.advertisementserver.model.Advertisement;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;




@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdvertisementMapper  {
    AdvertisementMapper INSTANCE = Mappers.getMapper(AdvertisementMapper.class);
    AdvertisementDto toDto(Advertisement advertisement);

    Advertisement toEntity(AdvertisementDto advertisementDto);

    Advertisement toEntites(AdvertisementDtoSecond advertisementDto);
}
