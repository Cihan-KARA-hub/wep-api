package com.yelman.advertisementserver.repository;

import com.yelman.advertisementserver.model.User;
import com.yelman.advertisementserver.model.UserStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);


}
