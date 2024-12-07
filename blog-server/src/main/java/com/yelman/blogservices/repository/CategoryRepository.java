package com.yelman.blogservices.repository;

import com.yelman.blogservices.model.Category;
import com.yelman.blogservices.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findById(long id);
    boolean findByParentId(long id);

}
