package com.sd.test.springsecurity.service;

import com.sd.test.springsecurity.entity.User;
import com.sd.test.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserManagementServiceImpl implements UserManagementService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAll() {
        List list = new ArrayList();
        userRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public boolean addUser(User user) {
        boolean flag = true;

        try {
            if (!userRepository.existsUserByUsername(user.getUsername())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            } else {
                flag = false;
            }
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean deleteUser(String username) {
        boolean flag = true;

        try {
            if (userRepository.existsUserByUsername(username)) {
                userRepository.deleteUserByUsername(username);
                if (userRepository.existsUserByUsername(username))
                    flag = false;
            }
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean updateUser(User user) {
        boolean flag = true;

        try {
            if (userRepository.existsUserByUsername(user.getUsername())) {
                userRepository.save(user);
            } else {
                flag = false;
            }
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }
}
