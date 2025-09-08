package com.example.Jobhunter.service;

import java.util.List;
import java.util.Optional;

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

    // ========================== XÓA USERS ========================
    public void handleDeleteUser(long id){
        this.userRepository.deleteById(id);
    }

    // ========================== LẤY USERS THEO ID ========================
    public User fetchUserById(long id){
        Optional<User> userOptional = this.userRepository.findById(id);
        if(userOptional.isPresent()){
            return userOptional.get();
        } else {
            return null;
        }
    }

    // ========================== LẤY TẤT CẢ USERS ========================
    public List<User> fetchAllUsers(){
        return this.userRepository.findAll();
    }
}
