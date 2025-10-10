package com.example.Jobhunter.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.Jobhunter.domain.RestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Trong Spring Security, khi người dùng chưa xác thực mà cố gọi API cần bảo vệ → nó sẽ gọi AuthenticationEntryPoint.
// Mặc định, BearerTokenAuthenticationEntryPoint sẽ trả về 401 Unauthorized kèm header WWW-Authenticate

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
    private final ObjectMapper mapper ; 

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        // Vẫn giữ lại hành vi mặc định của Spring (trả về 401 + header chuẩn).
        // delegate.commence(...) gọi cái mặc định đó trước.
        this.delegate.commence(request, response, authException);

        response.setContentType("application/json;charset=UTF-8"); // → báo cho client đây là JSON.
        RestResponse<Object> res = new RestResponse<Object>();

        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());

        String errorMessage = Optional.ofNullable(authException.getCause()).map(Throwable::getMessage)
                .orElse(authException.getMessage());

        res.setError(errorMessage);
        res.setMessage("Token không hợp lệ (hết hạn, không đúng định dạng, hoặc không tồn tại)");
        mapper.writeValue(response.getWriter(), res); // convert res sang JSON và ghi vào HTTP response.
    }

}
