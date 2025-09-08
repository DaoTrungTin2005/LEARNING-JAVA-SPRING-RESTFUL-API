package com.example.Jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Jobhunter.domain.User;

public interface  UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    
}
