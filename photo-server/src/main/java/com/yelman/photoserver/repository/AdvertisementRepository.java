package com.yelman.photoserver.repository;

import com.yelman.photoserver.model.AdvertisementPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AdvertisementRepository extends JpaRepository<AdvertisementPhoto, Long> {
    @Query("SELECT p FROM AdvertisementPhoto p WHERE p.id IN :ids")
    List<AdvertisementRepository> findByIds(@Param("ids") List<Long> ids);

}
