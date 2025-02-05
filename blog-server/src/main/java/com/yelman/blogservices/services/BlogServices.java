package com.yelman.blogservices.services;

import com.yelman.blogservices.api.dto.BlogDto;
import com.yelman.blogservices.api.dto.BlogPatchDto;
import com.yelman.blogservices.api.mapper.BlogMapper;
import com.yelman.blogservices.model.blog.Blogs;
import com.yelman.blogservices.model.blog.Role;
import com.yelman.blogservices.model.blog.User;
import com.yelman.blogservices.model.enums.ActiveEnum;
import com.yelman.blogservices.model.enums.ShortLangEnum;
import com.yelman.blogservices.repository.BlogRepository;
import com.yelman.blogservices.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.Normalizer;
import java.util.List;
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
    @CacheEvict(value = {
            "blog_est_new",
            "blog_dynamic_query",
            "blog_user_blog"
    }, allEntries = true)
    @Transactional
    public ResponseEntity<HttpStatus> createBlog(BlogDto blogdto) throws IOException {
        Optional<User> user = userRepository.findByUsernameAndEnabledIsTrue(blogdto.getUserName());
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

    @CacheEvict(value = {
            "blog_est_new",
            "blog_dynamic_query",
            "blog_user_blog"
    }, allEntries = true)
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
    @Cacheable(value = "blog_user_blog", key = "#userName+'_'+#page + '_' + #size")
    public Page<BlogDto> getAllUserBlog(String userName,Pageable page ) {
        Page<Blogs> blog = blogRepository.findByAuthor_Username(userName, page);
        Page<BlogDto> dto = blog.map(blogMapper::mapDto);
        if (dto.isEmpty()) {
            return null;
        }
        return dto;
    }
    @CacheEvict(value = {
            "blog_est_new",
            "blog_dynamic_query",
            "blog_user_blog"
    }, allEntries = true)
    public ResponseEntity<HttpStatus> deleteBlog(long blogId, long userId) {
        Optional<Blogs> blog = blogRepository.findById(blogId);
        User user = userRepository.findByIdAndEnabledIsTrue(userId);
        if (blog.isPresent() && blog.get().getAuthor().getId() == user.getId()) {
            blogRepository.delete(blog.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @CacheEvict(value = {
            "blog_est_new",
            "blog_dynamic_query",
            "blog_user_blog"
    }, allEntries = true)
    public ResponseEntity<HttpStatus> patchBlog(long blogId, long userId, BlogPatchDto blogUpdateDto) {
        Optional<Blogs> blog = blogRepository.findById(blogId);
        User user = userRepository.findByIdAndEnabledIsTrue(userId);
        if (blog.isPresent() && blog.get().getAuthor().getId() == user.getId()) {
            Blogs blogToUpdate = blog.get();
            BeanUtils.copyProperties(blogUpdateDto, blogToUpdate, getNullPropertyNames(blogUpdateDto));
            blogRepository.save(blogToUpdate);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private String[] getNullPropertyNames(Object source) {
        return java.util.Arrays.stream(source.getClass().getDeclaredFields())
                .filter(field -> {
                    field.setAccessible(true);
                    try {
                        return field.get(source) == null;
                    } catch (IllegalAccessException e) {
                        return false;
                    }
                })
                .map(field -> field.getName())
                .toArray(String[]::new);
    }
}

