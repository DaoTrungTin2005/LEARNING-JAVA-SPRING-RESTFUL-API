package com.example.Jobhunter.service;

import java.util.List;
import java.util.Optional;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.Jobhunter.domain.User;
import com.example.Jobhunter.domain.dto.Meta;
import com.example.Jobhunter.domain.dto.ResultPaginationDTO;
import com.example.Jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ========================== LƯU USERS ========================
    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    // ========================== XÓA USERS ========================
    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    // ========================== LẤY USERS THEO ID ========================
    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            return null;
        }
    }

    // ========================== LẤY TẤT CẢ USERS ========================
    public ResultPaginationDTO fetchAllUsers(Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageUser.getNumber() + 1);
        mt.setPageSize(pageUser.getSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());
        return rs;
    }

    // ========================== CẬP NHẬT USERS THEO ID ========================
    public User handleUpdateUser(User user) {
        User currentUser = this.fetchUserById(user.getId());
        if (currentUser != null) {
            currentUser.setEmail(user.getEmail());
            currentUser.setName(user.getName());
            currentUser.setPassword(user.getPassword());

            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    // ========================= LẤY USERS THEO USERNAME (nhưng thực chất là theo
    // email)========================
    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }
}
