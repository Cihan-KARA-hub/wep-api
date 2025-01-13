package com.yelman.advertisementserver.repository;

import com.yelman.advertisementserver.model.Sehirler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SehirlerRepository extends JpaRepository<Sehirler, Integer> {
    Sehirler findByTitleIgnoreCase(String title);
}
