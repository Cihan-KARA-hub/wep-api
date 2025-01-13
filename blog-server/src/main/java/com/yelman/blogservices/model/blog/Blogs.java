package com.yelman.blogservices.model.blog;


import com.yelman.blogservices.model.enums.ActiveEnum;
import com.yelman.blogservices.model.enums.LanguageEnum;
import com.yelman.blogservices.model.enums.ShortLangEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blogs")
public class Blogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", length = 60)
    private String title;
    @Column(name = "meta_description", length = 155)
    private String metaDescription;
    @Column(name = "content")
    private String content;
    @Column(name = "slug", nullable = true)
    private String slug;
    @Column(name = "created_at")
    @CreationTimestamp
    private OffsetDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private OffsetDateTime updateAt;
    @Column(name = "reading_second")
    private int readingSecond;
    @Column(name = "read_count", nullable = true)
    private int readCount;
    @Enumerated(EnumType.STRING)
    @Column(name = "is_blog_active", nullable = true)
    private ActiveEnum isActive;
    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "id", nullable = true)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private LanguageEnum language;
    @Enumerated(EnumType.STRING)
    @Column(name = "short_language", nullable = false)
    private ShortLangEnum shortLang;


}
