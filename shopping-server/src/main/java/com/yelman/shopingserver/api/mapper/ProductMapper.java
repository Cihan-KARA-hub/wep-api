package com.yelman.shopingserver.api.mapper;

import com.yelman.shopingserver.api.dto.ProductDto;
import com.yelman.shopingserver.api.dto.UserStoreDto;
import com.yelman.shopingserver.model.Product;
import com.yelman.shopingserver.model.Store;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {



    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);


    Product dtoToEntity(ProductDto dto);

    ProductDto entityToDto(Product entity);
}
