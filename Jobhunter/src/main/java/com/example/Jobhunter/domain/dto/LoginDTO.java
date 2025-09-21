package com.example.Jobhunter.domain.dto;

import jakarta.validation.constraints.NotBlank;

// Login DTO tồn tại để khống chế input : chỉ nhận username và password (vì domain User có nhiều trường hơn)
// Và vì lí do bảo mật
public class LoginDTO {
    @NotBlank(message = "Username khong dc bo trong")
    private String username;
    @NotBlank(message = "Password khong dc bo trong")
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
