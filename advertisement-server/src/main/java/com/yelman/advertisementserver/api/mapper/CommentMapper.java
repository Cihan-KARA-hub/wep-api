package com.yelman.advertisementserver.api.mapper;

import com.yelman.advertisementserver.api.dto.CommentDto;

import com.yelman.advertisementserver.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentDto toDto(Comment comment);

    Comment toComment(CommentDto commentDto);

}
