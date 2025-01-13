package com.yelman.shopingserver.repository;


import com.yelman.shopingserver.model.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountriesRepository extends JpaRepository<Countries, Integer> {
    Countries findByTitleIgnoreCase(String title);
}
