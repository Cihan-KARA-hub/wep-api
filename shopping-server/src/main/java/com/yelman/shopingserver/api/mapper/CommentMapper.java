package com.yelman.shopingserver.api.mapper;

import com.yelman.shopingserver.api.dto.ShopCommentDto;
import com.yelman.shopingserver.model.ShopComment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);


    ShopComment dtoToEntity(ShopCommentDto dto);

    ShopCommentDto entityToDto(ShopComment entity);
}
