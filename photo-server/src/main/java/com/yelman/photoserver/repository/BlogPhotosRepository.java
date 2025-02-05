package com.yelman.photoserver.repository;


import com.yelman.photoserver.model.BlogPhotos;
import com.yelman.photoserver.model.ShoppingPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BlogPhotosRepository extends JpaRepository<BlogPhotos, Long> {
    @Query("SELECT p FROM BlogPhotos p WHERE p.id IN :ids")
    List<BlogPhotos> findByIds(@Param("ids") List<Long> ids);
    BlogPhotos findByIdAndUserId(long id, long userId);
    List<BlogPhotos> findByBlogId(Long blogId);
}
