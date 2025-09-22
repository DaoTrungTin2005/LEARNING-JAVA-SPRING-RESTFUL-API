package com.example.Jobhunter.controller;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.Jobhunter.domain.dto.LoginDTO;
import com.example.Jobhunter.util.SecurityUtil;

import jakarta.validation.Valid;

@RestController
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/login")

    // ResponseEntity là một class của Spring dùng để đại diện cho toàn bộ HTTP
    // Response (bao gồm: status code, headers, body).

    // khi xài @RequestBody thì khi gửi dữ liệu từ body của postman thì sẽ ta lấy
    // được dữ liệu đó
    // Nó nói với Spring rằng: lấy dữ liệu từ phần body của HTTP request (JSON, XML,
    // …) rồi map vào object Java.
    public ResponseEntity<LoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {

        // Nạp input gồm username và password vào Security
        // Tạo một object UsernamePasswordAuthenticationToken, đây là “vé” xác thực mà
        // Spring Security hiểu.
        // Đưa vào username và password từ loginDTO.
        // Object này sẽ được chuyển cho AuthenticationManager để xử lý.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());


        // Xac thực người dùng => cần viết hàm loadUserByUsername
        //authenticate(authenticationToken) sẽ gọi tới service UserDetailsService.loadUserByUsername()
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //create a token
        this.securityUtil.createToken(authentication);
        return ResponseEntity.ok().body(loginDTO);
    }
}
