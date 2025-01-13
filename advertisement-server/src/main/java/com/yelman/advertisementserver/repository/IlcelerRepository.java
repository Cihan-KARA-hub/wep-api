package com.yelman.advertisementserver.repository;

import com.yelman.advertisementserver.model.Ilceler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IlcelerRepository extends JpaRepository<Ilceler, Integer> {
    Ilceler findByTitleIgnoreCase(String title);
}
