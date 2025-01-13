package com.yelman.shopingserver.repository;

import com.yelman.shopingserver.model.Ilceler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IlcelerRepository extends JpaRepository<Ilceler, Integer> {
    Ilceler findByTitleIgnoreCase(String title);
}
