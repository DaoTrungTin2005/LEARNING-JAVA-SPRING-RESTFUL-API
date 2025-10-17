package com.example.Jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Jobhunter.domain.Company;
import com.example.Jobhunter.service.CompanyService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    // ------------------------------Lấy tất cả---------------------------------------

    // ResponseEntity<...> là kiểu trả về được Spring cung cấp để bạn có thể:
    // kiểm soát HTTP status code (200, 201, 400, 404, …),
    // và trả về body JSON (dữ liệu bạn muốn gửi cho frontend).
    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getCompanies() {
        List<Company> companies = companyService.handleGetCompanies();
        return ResponseEntity.ok(companies);
    }

    // -----------------------------------Cập nhật-----------------------------------
//     RequestBody:
// Dùng để nhận dữ liệu JSON từ body của request và ánh xạ nó thành một object Company.
// GET chỉ lấy dữ liệu → không cần body → không cần @RequestBody.
// POST/PUT cần gửi dữ liệu mới để tạo hoặc cập nhật → cần body → dùng @RequestBody.
    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company reqCompany) {
        Company updatedCompany = companyService.handleUpdateCompany(reqCompany);
        return ResponseEntity.ok(updatedCompany);
    }

    // ------------------------------------Xóa-------------------------------------
    //Phản hồi sẽ không có body (nội dung trả về).
    // /@PathVariable dùng để lấy giá trị từ đường dẫn URL.
    @DeleteMapping("/companies/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId) {
        this.companyService.handleDeleteCompany(companyId);
        return ResponseEntity.ok(null);
    }

}