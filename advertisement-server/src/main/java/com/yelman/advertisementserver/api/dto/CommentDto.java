package com.yelman.advertisementserver.api.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto implements Serializable {

    private Long id;
    private Long advertisementId;
    private Long userId;
    private String content;
    private OffsetDateTime createdAt;
}
