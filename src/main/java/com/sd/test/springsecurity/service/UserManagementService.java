package com.sd.test.springsecurity.service;

import com.sd.test.springsecurity.entity.User;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

public interface UserManagementService {
    @Secured({"ROLE_ADMIN"})
    public List<User> getAll();

    @Secured({"ROLE_ADMIN"})
    public User getUserByUsername(String username);

    public boolean addUser(User user);

    @Secured({"ROLE_ADMIN"})
    public boolean deleteUser(String username);

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public boolean updateUser(User user);
}
