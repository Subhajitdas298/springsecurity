package com.sd.test.springsecurity.controller;

import com.sd.test.springsecurity.entity.User;
import com.sd.test.springsecurity.service.UserManagementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("${app.api.rest.root}/open")
public class PublicController {

    @Autowired
    UserManagementServiceImpl userManagementService;

    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
        User user = userManagementService.getUserByUsername(username);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> list = userManagementService.getAll();
        return new ResponseEntity<List<User>>(list, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody User user, UriComponentsBuilder builder) {
        boolean flag = userManagementService.addUser(user);
        if (flag == false) {
            return new ResponseEntity<String>("This user already exist", HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/user/{username}").buildAndExpand(user.getUsername()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/user")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userManagementService.updateUser(user);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable("username") String username) {
        userManagementService.deleteUser(username);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
