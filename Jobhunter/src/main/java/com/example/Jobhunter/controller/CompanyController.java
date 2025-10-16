package com.example.Jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Jobhunter.domain.Company;
import com.example.Jobhunter.service.CompanyService;

import jakarta.validation.Valid;

@RestController
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    // @RequestBody → tự động chuyển JSON FE gửi lên thành đối tượng Java Company.

    // @Valid:
    // Kích hoạt validation nếu class Company có các annotation như:
    // @NotBlank
    // @Size(min = 3)
    // → Nếu dữ liệu FE gửi lên sai (ví dụ name trống), Spring Boot sẽ báo lỗi 400
    // Bad Request.

    // Dùng ResponseEntity để:
    // Chủ động set HTTP Status code (ở đây là 201 Created – chuẩn REST khi tạo mới
    // thành công).
    // Trả về body JSON chứa kết quả (ở đây là công ty vừa được tạo).
    public ResponseEntity<?> createCompany(@Valid @RequestBody Company reqcompany) {

        // Gọi hàm handleCreateCompany() trong CompanyService để xử lý lưu dữ liệu.
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.handleCreateCompany(reqcompany));
    }
}