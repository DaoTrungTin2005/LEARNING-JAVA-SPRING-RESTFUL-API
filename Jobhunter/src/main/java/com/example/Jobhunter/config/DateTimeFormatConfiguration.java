package com.example.Jobhunter.config;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.swing.Spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

// WebMvcConfigurer là interface của Spring MVC cho phép bạn tùy chỉnh hành vi
// mặc định của Spring Web (ví dụ: format dữ liệu, cấu hình view, interceptor,
// resource...).
// không cần viết toàn bộ, chỉ override những hàm cần.
// Ở đây là addFormatters.
public class DateTimeFormatConfiguration implements WebMvcConfigurer {
    @Override

    // Đây là phương thức bạn override để đăng ký các bộ định dạng
    // (formatter/converter) cho Spring.
    // Khi nhận hoặc trả về dữ liệu JSON có kiểu thời gian, Spring Boot sẽ
    // chuyển đổi định dạng theo cấu hình ở đây.
    public void addFormatters(FormatterRegistry registry) {
        // Tạo một "người đăng ký định dạng thời gian" (formatter registrar).
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();

        // Nói với Spring Boot rằng bạn muốn dùng định dạng ISO chuẩn quốc tế (VD:
        // yyyy-MM-dd, yyyy-MM-dd'T'HH:mm:ss).
        registrar.setUseIsoFormat(true);

        // Đăng ký cấu hình này vào hệ thống format chung của Spring.
        registrar.registerFormatters(registry);

    }
}