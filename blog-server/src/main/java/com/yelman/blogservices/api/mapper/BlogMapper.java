package com.yelman.blogservices.api.mapper;

import com.yelman.blogservices.api.dto.BlogDto;
import com.yelman.blogservices.model.Blogs;
import com.yelman.blogservices.model.User;
import com.yelman.blogservices.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class BlogMapper implements BlogMappers {

    private final UserRepository userRepository;

    public BlogMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        blogDto.setUser_id(blogs.getAuthor().getId());
        blogDto.setReadingSecond(blogs.getReadingSecond());


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


        String slug = generateSlug(blogDto.getTitle());
        blogs.setSlug(slug);


        User author = findUserById(blogDto.getUser_id());
        blogs.setAuthor(author);

        return blogs;
    }

    private User findUserById(Long id) {
        if (id != null) {
            return userRepository.findById(id).orElse(null);
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
