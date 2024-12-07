package com.yelman.blogservices.services;

import com.yelman.blogservices.api.dto.BlogDto;
import com.yelman.blogservices.api.mapper.BlogMapper;
import com.yelman.blogservices.model.enums.ActiveEnum;
import com.yelman.blogservices.model.Blogs;
import com.yelman.blogservices.model.Role;
import com.yelman.blogservices.model.User;
import com.yelman.blogservices.model.enums.ShortLangEnum;
import com.yelman.blogservices.repository.BlogRepository;
import com.yelman.blogservices.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseEntity<String> createBlog(BlogDto blogdto) {
        Optional<User> user = userRepository.findByUsername(blogdto.getUserName());
        if (!user.get().getAuthorities().contains(Role.valueOf("ROLE_SUBSCRIBE"))) {
            return ResponseEntity.badRequest().body("not authorized or null user");
        }
        Blogs blogDtoToBlogs = blogMapper.mapEntity(blogdto);
        blogDtoToBlogs.setIsActive(ActiveEnum.WAITING);
        blogDtoToBlogs.setAuthor(user.get());
        blogRepository.save(blogDtoToBlogs);
        return ResponseEntity.ok().body("success");
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

    public Page<Blogs> getBlogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return blogRepository.findAll(pageable);
    }
     public Page<Blogs> getCategoryNameAndLanguage(String category , int page, int size) {
         Pageable pageable = PageRequest.of(page, size);
       Page<Blogs> blogs= blogRepository.findByAuthor_Username(category,pageable);
         log.info(blogs.toString());
         return blogs;
     }


}

