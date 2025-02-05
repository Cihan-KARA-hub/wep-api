package com.yelman.photoserver.repository;

import com.yelman.photoserver.model.AdvertisementPhoto;
import com.yelman.photoserver.model.ShoppingPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ShoppingRepository extends JpaRepository<ShoppingPhoto, Long> {
    @Query("SELECT p FROM ShoppingPhoto p WHERE p.id IN :ids")
    List<ShoppingRepository> findByIds(@Param("ids") List<Long> ids);
    ShoppingPhoto findByIdAndUserId(long id, long userId);
    List<ShoppingPhoto> findByShoppingId(long shoppingId);
}
