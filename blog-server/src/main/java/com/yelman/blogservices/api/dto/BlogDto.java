package com.yelman.blogservices.api.dto;

import com.yelman.blogservices.model.ActiveEnum;
import lombok.*;

import java.time.OffsetDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogDto {
    private long id;
    private String title;
    private String metaDescription;
    private String content;
    private String slug;
    private long user_id;
    private int readingSecond;
    private ActiveEnum isActive;

}
