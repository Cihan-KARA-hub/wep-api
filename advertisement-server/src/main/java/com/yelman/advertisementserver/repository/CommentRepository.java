package com.yelman.advertisementserver.repository;

import com.yelman.advertisementserver.model.Advertisement;
import com.yelman.advertisementserver.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
        List<Comment> findByAdvertisement_Id(Long advertisement_id);
        Page<Comment> findByAdvertisement_Id(long advertisementId, Pageable pageable);
}
