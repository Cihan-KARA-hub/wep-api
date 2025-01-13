package com.yelman.blogservices.services;

import com.yelman.blogservices.api.dto.BlogDto;
import com.yelman.blogservices.api.mapper.BlogMapper;
import com.yelman.blogservices.model.blog.Blogs;

import com.yelman.blogservices.model.blog.Role;
import com.yelman.blogservices.model.blog.User;
import com.yelman.blogservices.model.enums.ActiveEnum;
import com.yelman.blogservices.model.enums.ShortLangEnum;
import com.yelman.blogservices.repository.blog.BlogRepository;

import com.yelman.blogservices.repository.blog.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.text.Normalizer;
import java.util.Optional;

@Service
public class BlogServices {
    private static final Logger log = LoggerFactory.getLogger(BlogServices.class);
    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;
    private final UserRepository userRepository;

    public BlogServices(BlogRepository blogRepository, BlogMapper blogMapper, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.blogMapper = blogMapper;
        this.userRepository = userRepository;

    }

    @Transactional
    public ResponseEntity<HttpStatus> createBlog(BlogDto blogdto) throws IOException {
        Optional<User> user = userRepository.findByUsername(blogdto.getUserName());
        if (!user.get().getAuthorities().contains(Role.valueOf("ROLE_SUBSCRIBE"))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Blogs blogs = blogMapper.mapEntity(blogdto);
        blogs.setSlug(toSlug(blogdto.getTitle()));
        blogs.setIsActive(ActiveEnum.WAITING);
        int val = blogdto.getLanguage().ordinal();
        blogs.setShortLang(ShortLangEnum.values()[val]);
        blogRepository.save(blogs);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //TODO cache kullanılacak
    @Transactional
    public ResponseEntity<Void> incrementReadCount(long blogId) {
        Optional<Blogs> blog = blogRepository.findById(blogId);
        if (blog.isPresent()) {
            Blogs blogsToIncrement = blog.get();
            blogsToIncrement.setReadCount(blogsToIncrement.getReadCount() + 1);
            blogRepository.save(blogsToIncrement);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    public Page<Blogs> getAllUserBlog(String userName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return blogRepository.findByAuthor_Username(userName, pageable);
    }


    private static String toSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // 1. Küçük harfe çevir
        String slug = input.toLowerCase();

        // 2. Türkçe karakterleri İngilizce karşılıklarıyla değiştir
        slug = slug.replaceAll("ç", "c")
                .replaceAll("ğ", "g")
                .replaceAll("ı", "i")
                .replaceAll("ö", "o")
                .replaceAll("ş", "s")
                .replaceAll("ü", "u");

        // 3. Unicode karakterlerini normalleştir
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("\\p{M}", ""); // Kombine karakterleri kaldır

        // 4. Harf ve sayılar dışındaki karakterleri kaldır, boşlukları "-" ile değiştir
        slug = slug.replaceAll("[^a-z0-9\\s-]", "").replaceAll("\\s+", "-");

        // 5. Baş ve sondaki "-" karakterlerini kaldır
        slug = slug.replaceAll("^-+|-+$", "");

        return slug;
    }


}

