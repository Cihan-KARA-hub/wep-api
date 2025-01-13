package com.yelman.shopingserver.repository;


import com.yelman.shopingserver.model.Sehirler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SehirlerRepository extends JpaRepository<Sehirler, Integer> {
    Sehirler findByTitleIgnoreCase(String title);
}
