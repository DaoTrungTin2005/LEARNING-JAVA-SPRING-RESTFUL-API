package com.example.Jobhunter.service;

import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("userDetailsService")

// UserDetailsService Đây là interface mà Spring Security dùng để tìm user trong
// hệ thống
public class UserDetailsCustom implements UserDetailsService {

    private final UserService userService;

    public UserDetailsCustom(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Tìm user trong database

        // vì Spring Security bắt buộc phải gọi hàm loadUserByUsername, nên “mượn”
        // cái username đó để thực chất đi tìm bằng email. ở UserService viết
        // this.userRepository.findByEmail(username);
        com.example.Jobhunter.domain.User user = this.userService.handleGetUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username/Passsword không hợp lệ");
        }

        // Convert sang UserDetails mà Spring Security hiểu
        return new User(
                user.getEmail(), // cái này Spring sẽ coi như "username"
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

}
