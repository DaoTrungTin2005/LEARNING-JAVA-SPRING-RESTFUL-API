package com.example.Jobhunter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    // ========================== TẠO USER MỚI ========================
    @PostMapping("/user")
    public User createNewUser(@RequestBody User postmanUser) {

        // User user = new User();
        // user.setEmail("Tin@gmail.com");
        // user.setName("Tin");
        // user.setPassword("12345");

        User newUser = this.userService.handleCreateUser(postmanUser);

        return newUser;
    }

    // ========================== XÓA USER THEO ID ========================
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return "user deleted";
    }

    // ========================== LẤY USER THEO ID ========================
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return this.userService.fetchUserById(id);
    }

    // ========================== LẤY TẤT CẢ USER ========================
    @GetMapping("/user")
    public List<User> getAllUsers() {
        return this.userService.fetchAllUsers();
    }

}
