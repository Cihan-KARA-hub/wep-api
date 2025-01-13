package com.yelman.blogservices.repository.blog;

import com.yelman.blogservices.model.blog.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findById(long id);
    boolean findByParentId(long id);

}
