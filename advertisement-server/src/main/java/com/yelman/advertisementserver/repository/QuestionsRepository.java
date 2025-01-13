package com.yelman.advertisementserver.repository;
import com.yelman.advertisementserver.model.Questions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Long> {
    Page<Questions> findAllByAdvertisement_IdAndParentId(Pageable pageable,long advertisementId,long parentId);
    List<Questions> findAllByParentId(long parentId);
    Page<Questions> findAllByParentId(long parentId, Pageable pageable);
}
