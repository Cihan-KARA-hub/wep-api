package com.yelman.advertisementserver.api.mapper;

import com.yelman.advertisementserver.api.dto.UserStoreDto;
import com.yelman.advertisementserver.model.UserStore;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserStoreMapper {


    UserStoreMapper INSTANCE = Mappers.getMapper(UserStoreMapper.class);
    UserStore dtoToEntity(UserStoreDto dto);
    UserStoreDto entityToDto(UserStore entity);
}
