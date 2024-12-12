package com.yelman.advertisementserver.api.dto;

import com.yelman.advertisementserver.model.Advertisement;
import com.yelman.advertisementserver.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionsDto {
    private Long id;
    private Long advertisementId;
    private Long userId;
    private String title;
    private String content;
    private OffsetDateTime createdAt;

}
