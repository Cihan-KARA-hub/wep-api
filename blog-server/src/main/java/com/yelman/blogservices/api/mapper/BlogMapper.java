package com.yelman.blogservices.api.mapper;

import com.yelman.blogservices.api.dto.BlogDto;
import com.yelman.blogservices.model.blog.Blogs;
import com.yelman.blogservices.model.blog.Category;
import com.yelman.blogservices.model.blog.User;
import com.yelman.blogservices.repository.CategoryRepository;
import com.yelman.blogservices.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class BlogMapper implements BlogMappers {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public BlogMapper(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public BlogDto mapDto(Blogs blogs) {
        if (blogs == null) {
            return null;
        }
        BlogDto blogDto = new BlogDto();
        blogDto.setId(blogs.getId());
        blogDto.setTitle(blogs.getTitle());
        blogDto.setContent(blogs.getContent());
        blogDto.setIsActive(blogs.getIsActive());
        blogDto.setMetaDescription(blogs.getMetaDescription());
        blogDto.setUserName(blogs.getAuthor().getUsername());
        blogDto.setName(blogs.getAuthor().getUsername());
        blogDto.setReadingSecond(blogs.getReadingSecond());
        blogDto.setCategories(blogs.getCategory().getId());
        blogDto.setLanguage(blogs.getLanguage());
        blogDto.setShortLang(blogs.getShortLang());


        String slug = generateSlug(blogs.getTitle());
        blogDto.setSlug(slug);

        return blogDto;
    }

    @Override
    public Blogs mapEntity(BlogDto blogDto) {
        if (blogDto == null) {
            return null;
        }

        Blogs blogs = new Blogs();
        blogs.setId(blogDto.getId());
        blogs.setTitle(blogDto.getTitle());
        blogs.setContent(blogDto.getContent());
        blogs.setIsActive(blogDto.getIsActive());
        blogs.setMetaDescription(blogDto.getMetaDescription());
        blogs.setReadingSecond(blogDto.getReadingSecond());
        blogs.setCategory(findCategoryById(blogDto.getCategories()));
        blogs.setLanguage(blogDto.getLanguage());
        blogs.setShortLang(blogDto.getShortLang());

        String slug = generateSlug(blogDto.getTitle());
        blogs.setSlug(slug);

        blogs.setAuthor(findUserById(blogDto.getName()));
        blogs.setAuthor(findUserById(blogDto.getUserName()));

        return blogs;
    }

    private User findUserById(String id) {
        if (id != null) {
            return userRepository.findByUsernameAndEnabledIsTrue(id).orElse(null);
        }
        return null;
    }

    private Category findCategoryById(Long id) {
        if (id != null) {
            return categoryRepository.findById(id).orElse(null);
        }
        return null;
    }

    private String generateSlug(String title) {
        if (title == null || title.isEmpty()) {
            return null;
        }

        return title.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("-$", "");
    }
}
