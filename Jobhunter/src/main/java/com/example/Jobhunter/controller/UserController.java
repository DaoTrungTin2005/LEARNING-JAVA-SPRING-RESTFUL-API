package com.example.Jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Jobhunter.domain.User;
import com.example.Jobhunter.service.UserService;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/user/create")
    public String createNewUser(){

        User user = new User();
        user.setEmail("Tin@gmail.com");
        user.setName("Tin");
        user.setPassword("12345");

        this.userService.handleCreateUser(user);

        return "create new user";
    }
}
