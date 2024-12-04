package com.yelman.blogservices.api.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBlogDto {
    private int id;
    private String title;
    private String metaDescription;
    private String content;
    private OffsetDateTime updateAt;
    private int readingSecond;

}
