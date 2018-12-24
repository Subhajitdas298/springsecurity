package com.sd.test.springsecurity.repository;

import com.sd.test.springsecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
    boolean existsUserByUsername(String username);
    void deleteUserByUsername(String username);
}
