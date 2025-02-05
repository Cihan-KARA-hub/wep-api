package com.yelman.blogservices.api.dto;

import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBlogDto  implements Serializable {
    private int id;
    private String title;
    private String metaDescription;
    private String content;
    private OffsetDateTime updateAt;
    private int readingSecond;

}
