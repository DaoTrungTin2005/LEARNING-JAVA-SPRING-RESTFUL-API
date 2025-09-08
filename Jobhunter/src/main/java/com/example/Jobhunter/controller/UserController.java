package com.example.Jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Jobhunter.domain.User;
import com.example.Jobhunter.service.UserService;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    // @GetMapping("/user/create")
    @PostMapping("/user/create")
    public User createNewUser(@RequestBody User postmanser) {

        // User user = new User();
        // user.setEmail("Tin@gmail.com");
        // user.setName("Tin");
        // user.setPassword("12345");

        User newUser = this.userService.handleCreateUser(postmanser);

        return newUser;
    }
}
