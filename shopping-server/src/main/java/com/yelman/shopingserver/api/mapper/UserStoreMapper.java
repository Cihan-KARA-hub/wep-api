package com.yelman.shopingserver.api.mapper;

import com.yelman.shopingserver.api.dto.UserStoreDto;
import com.yelman.shopingserver.model.Store;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserStoreMapper {


    UserStoreMapper INSTANCE = Mappers.getMapper(UserStoreMapper.class);

    Store dtoToEntity(UserStoreDto dto);

    UserStoreDto entityToDto(Store entity);
}