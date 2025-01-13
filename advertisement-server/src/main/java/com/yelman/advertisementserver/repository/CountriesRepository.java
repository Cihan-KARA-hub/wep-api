package com.yelman.advertisementserver.repository;

import com.yelman.advertisementserver.model.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountriesRepository extends JpaRepository<Countries, Integer> {
    Countries findByTitleIgnoreCase(String title);
}
