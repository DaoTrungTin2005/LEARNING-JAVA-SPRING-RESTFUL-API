package com.example.Jobhunter.domain.dto;

// Login DTO tồn tại để khống chế input : chỉ nhận username và password (vì domain User có nhiều trường hơn)
// Và vì lí do bảo mật
public class LoginDTO {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
