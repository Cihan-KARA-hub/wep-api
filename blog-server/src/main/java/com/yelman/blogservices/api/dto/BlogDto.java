package com.yelman.blogservices.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yelman.blogservices.model.enums.ActiveEnum;
import com.yelman.blogservices.model.enums.LanguageEnum;
import com.yelman.blogservices.model.enums.ShortLangEnum;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BlogDto  implements Serializable {
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
