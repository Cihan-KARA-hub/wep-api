package com.yelman.shopingserver.repository;

import com.yelman.shopingserver.model.ShopComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<ShopComment, Long> {
    List<ShopComment> findByParentId(long id);
    Page<ShopComment> findByParentId(long ids, Pageable pageable);
    Page<ShopComment> findByProduct_Id(long productId, Pageable pageable);
    @Modifying
    @Query("UPDATE ShopComment e SET e.content = :content WHERE e.id = :id")
    void updateContentById(@Param("id") Long id, @Param("content") String title);


}
