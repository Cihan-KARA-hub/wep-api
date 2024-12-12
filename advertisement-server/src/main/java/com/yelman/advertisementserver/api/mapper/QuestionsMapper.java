package com.yelman.advertisementserver.api.mapper;

import com.yelman.advertisementserver.api.dto.QuestionsDto;
import com.yelman.advertisementserver.model.Questions;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface QuestionsMapper {



    QuestionsMapper INSTANCE = Mappers.getMapper(QuestionsMapper.class);

    QuestionsDto toDto(Questions questions);

    Questions toEntity(QuestionsDto questionsDto);
}
