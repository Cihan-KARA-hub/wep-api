package com.yelman.blogservices.api.dto;

import com.yelman.blogservices.model.enums.ActiveEnum;
import com.yelman.blogservices.model.enums.LanguageEnum;
import com.yelman.blogservices.model.enums.ShortLangEnum;
import lombok.*;

@Data
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
    private String userName;
    private String name;
    private int readingSecond;
    private ActiveEnum isActive;
    private long categories;
    private LanguageEnum language;
    private ShortLangEnum shortLang;



}
