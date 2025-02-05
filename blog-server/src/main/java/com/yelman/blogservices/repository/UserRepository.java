package com.yelman.blogservices.repository;

import com.yelman.blogservices.model.blog.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByIdAndEnabledIsTrue(long id);
  Optional<User> findByUsernameAndEnabledIsTrue(String name);


}
