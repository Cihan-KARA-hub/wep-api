package com.yelman.advertisementserver.api.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private Long advertisementId;
    private Long userId;
    private String content;
    private OffsetDateTime createdAt;
}
