package com.yelman.advertisementserver.services;

import com.yelman.advertisementserver.api.dto.CommentDto;
import com.yelman.advertisementserver.api.mapper.CommentMapper;
import com.yelman.advertisementserver.model.Advertisement;
import com.yelman.advertisementserver.model.Comment;
import com.yelman.advertisementserver.model.User;
import com.yelman.advertisementserver.model.enums.Role;
import com.yelman.advertisementserver.repository.AdvertisementRepository;
import com.yelman.advertisementserver.repository.CommentRepository;
import com.yelman.advertisementserver.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServices {
    private final CommentRepository commentRepository;
    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentServices(CommentRepository commentRepository, AdvertisementRepository advertisementRepository, UserRepository userRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.advertisementRepository = advertisementRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    @Transactional
    public ResponseEntity<HttpStatus> createComment(CommentDto commentDto) {
        Comment comment = new Comment();
        Optional<User> userOptional = userRepository.findById(commentDto.getUserId());
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(commentDto.getAdvertisementId());
        if (advertisementOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Advertisement advertisement = advertisementOptional.get();

        if (!user.getAuthorities().contains(Role.ROLE_SUBSCRIBE)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        comment.setContent(commentDto.getContent());
        comment.setUser(user);
        comment.setAdvertisement(advertisement);
        commentRepository.save(comment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Transactional
    public ResponseEntity<HttpStatus> deleteComment(long id, long userId) {
        Comment data = commentRepository.findById(id).orElse(null);
        if (data != null && data.getUser().getId() == userId) {
            commentRepository.deleteById(data.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Page<CommentDto>> getComment(long advertisement, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> dataPage = commentRepository.findByAdvertisement_Id(advertisement, pageable);
        if (dataPage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<CommentDto> dtoList = dataPage.stream()
                .map(commentMapper::toDto)
                .toList();
        return ResponseEntity.ok(new PageImpl<>(dtoList, pageable, dataPage.getTotalElements()));
    }

}
