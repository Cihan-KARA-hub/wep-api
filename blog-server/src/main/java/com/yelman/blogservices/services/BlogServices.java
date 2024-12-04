package com.yelman.blogservices.services;

import com.yelman.blogservices.api.dto.BlogDto;
import com.yelman.blogservices.api.mapper.BlogMapper;
import com.yelman.blogservices.model.ActiveEnum;
import com.yelman.blogservices.model.Blogs;
import com.yelman.blogservices.model.Role;
import com.yelman.blogservices.model.User;
import com.yelman.blogservices.repository.BlogRepository;
import com.yelman.blogservices.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BlogServices {
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
        User user = userRepository.findById(blogdto.getUser_id());
        if (!user.getAuthorities().contains(Role.valueOf("ROLE_SUBSCRIBE"))) {
            return ResponseEntity.badRequest().body("not authorized or null user");
        }
        Blogs blogDtoToBlogs = blogMapper.mapEntity(blogdto);
        blogDtoToBlogs.setIsActive(ActiveEnum.WAITING);
        blogDtoToBlogs.setAuthor(user);
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
    //TODO blogları getirmek için dinamik bir yapı oluşturulacak


}

