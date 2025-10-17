package com.example.Jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Jobhunter.domain.User;
import com.example.Jobhunter.domain.dto.ResultPaginationDTO;
import com.example.Jobhunter.service.UserService;
import com.example.Jobhunter.util.error.IdInvalidException;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // ========================== TẠO USER MỚI ========================
    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User postmanUser) {

        // User user = new User();
        // user.setEmail("Tin@gmail.com");
        // user.setName("Tin");
        // user.setPassword("12345");

        // Hash password
        String hashPassword = this.passwordEncoder.encode(postmanUser.getPassword());
        postmanUser.setPassword(hashPassword);

        User newUser = this.userService.handleCreateUser(postmanUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // ========================== XÓA USER THEO ID ========================
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {

        if (id >= 1500) {
            throw new IdInvalidException("ID must be less than 1500");
        }

        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("users are deleted");
        // return ResponseEntity.ok("users are deleted");
    }

    // ========================== LẤY USER THEO ID ========================
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        User fetchUser = this.userService.fetchUserById(id);
        if (fetchUser != null) {
            return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // ========================== LẤY TẤT CẢ USER ========================
    @GetMapping("/users")

    // Trả về kiểu ResponseEntity<ResultPaginationDTO> → đây là wrapper để kèm
    // status code (200, 400,...) và body.

    // Nhận 3 tham số query:
    // current: trang hiện tại (default = 1)
    // size: số bản ghi mỗi trang (tùy chọn)
    // pageSize: ưu tiên hơn size nếu có (tùy
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
            @RequestParam(value = "current", defaultValue = "1") int current,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        // Ưu tiên pageSize nếu có, rồi size, rồi default 10
        // Xác định số bản ghi mỗi trang (finalSize)

        // Nếu pageSize tồn tại và > 0 → dùng pageSize
        // Else nếu size tồn tại và > 0 → dùng size
        // Nếu cả hai đều không hợp lệ → mặc định là 10 bản ghi/trang
        // Đây là cách ưu tiên tham số của client nhưng vẫn có default an toàn.
        int finalSize = (pageSize != null && pageSize > 0) ? pageSize : (size != null && size > 0) ? size : 10;

        // Validation
        // Validation tham số
        // current phải ≥ 1
        // finalSize phải ≥ 1 và ≤ 100
        // Nếu sai → trả về 400 Bad Request với DTO rỗng (có thể set thêm thông tin lỗi
        // trong errorDto)
        if (current < 1 || finalSize < 1 || finalSize > 100) {

            ResultPaginationDTO errorDto = new ResultPaginationDTO();

            return ResponseEntity.badRequest().body(errorDto);
        }

        // Chú ý: Spring dùng 0-based page → trang 1 của client phải chuyển thành 0.
        // finalSize là số bản ghi mỗi trang.
        Pageable pageable = PageRequest.of(current - 1, finalSize);

        // Gọi service để lấy DTO đầy đủ
        // userService.fetchAllUsers(pageable) trả về ResultPaginationDTO chứa:
        // danh sách user cho trang hiện tại
        // metadata phân trang (page, pageSize, total, ...)
        ResultPaginationDTO resultDto = this.userService.fetchAllUsers(pageable);

        // Adjust meta nếu cần (Spring getNumber() là 0-based, client current từ 1)
        if (resultDto != null && resultDto.getMeta() != null) {
            resultDto.getMeta().setPage(current); // Override để match param client
        }

        // Luôn return 200 nếu gọi thành công, dù empty
        return ResponseEntity.ok(resultDto);
    }

    // =========================== CẬP NHẬT USER THEO ID ========================
    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User userUpdate = this.userService.handleUpdateUser(user);
        if (userUpdate != null) {
            return ResponseEntity.status(HttpStatus.OK).body(userUpdate);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }
}
