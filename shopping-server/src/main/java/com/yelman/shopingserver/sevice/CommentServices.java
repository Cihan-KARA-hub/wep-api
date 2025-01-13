package com.yelman.shopingserver.sevice;

import com.yelman.shopingserver.api.dto.ShopCommentDto;
import com.yelman.shopingserver.api.mapper.CommentMapperImpl;
import com.yelman.shopingserver.model.Product;
import com.yelman.shopingserver.model.ShopComment;
import com.yelman.shopingserver.model.User;
import com.yelman.shopingserver.model.enums.Role;
import com.yelman.shopingserver.repository.CommentRepository;
import com.yelman.shopingserver.repository.ProductRepository;
import com.yelman.shopingserver.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServices {
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CommentMapperImpl commentMapper;


    public CommentServices(CommentRepository commentRepository, ProductRepository productRepository, UserRepository userRepository, CommentMapperImpl commentMapper) {
        this.commentRepository = commentRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    @Transactional
    public ResponseEntity<Void> addComment(ShopCommentDto comment) {
        Optional<Product> store = productRepository.findById(comment.getProducts());
        if (store.isPresent()) {
            Optional<User> user = userRepository.findById(comment.getUsers());
            if (user.isPresent() && user.get().getAuthorities().contains(Role.ROLE_SUBSCRIBE)) {
                ShopComment comment1 = new ShopComment();
                comment1.setProduct(store.get());
                comment1.setUser(user.get());
                comment1.setParentId((long) 0);
                comment1.setContent(comment.getContent());
                commentRepository.save(comment1);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();

        }
        return ResponseEntity.notFound().build();

    }

    @Transactional
    public ResponseEntity<Void> addSubComment(ShopCommentDto comment) {
        Optional<Product> store = productRepository.findById(comment.getProducts());
        if (store.isPresent()) {
            Optional<User> user = userRepository.findById(comment.getUsers());
            if (user.isPresent() && user.get().getAuthorities().contains(Role.ROLE_SUBSCRIBE)) {
                Optional<ShopComment> com = commentRepository.findById(comment.getParentId());
                if (com.isPresent()) {
                    ShopComment comment1 = new ShopComment();
                    comment1.setProduct(store.get());
                    comment1.setUser(user.get());
                    comment1.setParentId(com.get().getId());
                    comment1.setContent(comment.getContent());
                    commentRepository.save(comment1);
                    return ResponseEntity.ok().build();
                }
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.notFound().build();

        }
        return ResponseEntity.notFound().build();

    }

    @Transactional
    public ResponseEntity<Void> deleteParentComment(long comment, long userId) {
        Optional<ShopComment> comment1 = commentRepository.findById(comment);
        if (comment1.isPresent() && comment1.get().getUser().getId() == userId) {
            List<ShopComment> comment2 = commentRepository.findByParentId(comment);
            commentRepository.deleteAll(comment2);
            commentRepository.deleteById(comment);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<Void> deleteSubComment(long comment, long userId) {
        Optional<ShopComment> comment1 = commentRepository.findById(comment);
        if (comment1.isPresent() && comment1.get().getUser().getId() == userId) {
            commentRepository.deleteById(comment);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<Void> updateComment(long userId, String content, long commentId) {
        Optional<ShopComment> comment1 = commentRepository.findById(commentId);
        if (comment1.isPresent() && comment1.get().getUser().getId() == userId) {
            ShopComment comment2 = new ShopComment();
            comment2.setContent(content);
            comment2.setParentId(comment1.get().getId());
            commentRepository.updateContentById(commentId, content);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<Page<ShopCommentDto>> getParentComment(long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ShopComment> entity = commentRepository.findByProduct_Id(productId, pageable);
        if (!entity.hasContent()) {
            return ResponseEntity.notFound().build();
        }
        Page<ShopCommentDto> dto = entity.map(commentMapper::entityToDto);
        return ResponseEntity.ok(dto);
    }

    @Transactional
    public ResponseEntity<Page<ShopCommentDto>> getSubComment(long parentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ShopComment> entity = commentRepository.findByParentId(parentId, pageable);
        if (!entity.hasContent()) {
            return ResponseEntity.notFound().build();
        }
        Page<ShopCommentDto> dto = entity.map(commentMapper::entityToDto);
        return ResponseEntity.ok(dto);
    }


}
