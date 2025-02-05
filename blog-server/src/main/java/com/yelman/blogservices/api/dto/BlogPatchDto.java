package com.yelman.blogservices.api.dto;

import com.yelman.blogservices.model.blog.Category;
import com.yelman.blogservices.model.blog.User;
import com.yelman.blogservices.model.enums.ActiveEnum;
import com.yelman.blogservices.model.enums.LanguageEnum;
import com.yelman.blogservices.model.enums.ShortLangEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogPatchDto  implements Serializable {
    private Long id;
    private String title;
    private String metaDescription;
    private String content;
    private int readingSecond;
    private int readCount;
    private Category category;
    private LanguageEnum language;
    private ShortLangEnum shortLang;
}
