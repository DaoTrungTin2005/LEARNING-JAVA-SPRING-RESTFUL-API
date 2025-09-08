package com.example.Jobhunter.service;

import org.springframework.stereotype.Service;

import com.example.Jobhunter.domain.User;
import com.example.Jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ========================== LƯU USERS ========================
    public User handleCreateUser(User user){
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id){
        this.userRepository.deleteById(id);
    }
}
