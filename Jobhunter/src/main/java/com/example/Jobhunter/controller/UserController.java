package com.example.Jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Jobhunter.domain.User;
import com.example.Jobhunter.service.UserService;
import com.example.Jobhunter.service.error.IdInvalidException;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // ========================== TẠO USER MỚI ========================
    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User postmanUser) {

        // User user = new User();
        // user.setEmail("Tin@gmail.com");
        // user.setName("Tin");
        // user.setPassword("12345");

        // Hash password
        String hashPassword = this.passwordEncoder.encode(postmanUser.getPassword());
        postmanUser.setPassword(hashPassword);

        User newUser = this.userService.handleCreateUser(postmanUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // ========================== XÓA USER THEO ID ========================
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {

        if (id >= 1500) {
            throw new IdInvalidException("ID must be less than 1500");
        }

        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("users are deleted");
        // return ResponseEntity.ok("users are deleted");
    }

    // ========================== LẤY USER THEO ID ========================
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        User fetchUser = this.userService.fetchUserById(id);
        if (fetchUser != null) {
            return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // ========================== LẤY TẤT CẢ USER ========================
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> fetchAllUsers = this.userService.fetchAllUsers();
        if (fetchAllUsers != null) {
            return ResponseEntity.status(HttpStatus.OK).body(fetchAllUsers);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // =========================== CẬP NHẬT USER THEO ID ========================
    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User userUpdate = this.userService.handleUpdateUser(user);
        if (userUpdate != null) {
            return ResponseEntity.status(HttpStatus.OK).body(userUpdate);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }
}
