package com.example.Jobhunter.domain;

import java.time.Instant;

import com.example.Jobhunter.util.SecurityUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Table(name = "companies")
@Entity
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name không được để trống")
    private String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private String address;
    private String logo;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    // Được gọi tự động trước khi entity được lưu vào database (do annotation
    // @PrePersist).
    @PrePersist
    public void handleBeforeCreateAt() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : ""; // trả ra ai tạo
        this.createdAt = Instant.now(); // trả ra thời gian tạo

        // createdBy: ghi lại người nào đã tạo bản ghi → lấy từ getCurrentUserLogin(). (bên SecurityUtil).
        // createdAt: ghi lại thời điểm tạo bản ghi → Instant.now() (UTC).
    }
}
