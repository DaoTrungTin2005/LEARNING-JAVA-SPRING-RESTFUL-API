package com.example.Jobhunter.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Jobhunter.domain.Company;
import com.example.Jobhunter.repository.CompanyRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // ----------Creteate Company------------------------------
    public Company handleCreateCompany(Company c) {
        return this.companyRepository.save(c);
    }

    // -------------------- READ --------------------
    public List<Company> handleGetCompanies() {
        return companyRepository.findAll();
    }

    // -------------------- UPDATE --------------------

    // Logic nghiệp vụ:

    // Tìm công ty cần cập nhật theo id:
    // Nếu không tìm thấy → ném exception EntityNotFoundException → controller trả
    // 404.
    // Cập nhật dữ liệu mới:

    // name, description, address, logo → được lấy từ request body.
    // Cập nhật thông tin quản trị (audit info):

    // updatedAt → thời gian hiện tại (Instant.now())
    // updatedBy → username của người đang đăng nhập
    // (SecurityUtil.getCurrentUserLogin())

    // Lưu lại database:

    // companyRepository.save(existingCompany) → thực hiện update.

    public Company handleUpdateCompany(Company reqCompany) {
        Optional<Company> optionalCompany = companyRepository.findById(reqCompany.getId());
        if (optionalCompany.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy công ty có id = " + reqCompany.getId());
        }

        Company existingCompany = optionalCompany.get();
        existingCompany.setName(reqCompany.getName());
        existingCompany.setDescription(reqCompany.getDescription());
        existingCompany.setAddress(reqCompany.getAddress());
        existingCompany.setLogo(reqCompany.getLogo());

        // cập nhật thời gian và người cập nhật
        existingCompany.setUpdatedAt(Instant.now());
        existingCompany.setUpdatedBy(
                com.example.Jobhunter.util.SecurityUtil.getCurrentUserLogin().orElse("unknown"));

        return companyRepository.save(existingCompany);
    }

    // -------------------- DELETE --------------------

    // Logic nghiệp vụ:

    // Kiểm tra tồn tại bản ghi theo id:
    // Nếu không tồn tại → ném EntityNotFoundException → controller trả về 404.

    // Xóa bản ghi khỏi DB:
    // deleteById(companyId)

    // Không trả dữ liệu về:
    // Controller trả status code 200 hoặc 204 (No Content).

    public void handleDeleteCompany(Long companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new EntityNotFoundException("Không tìm thấy công ty có id = " + companyId);
        }
        companyRepository.deleteById(companyId);
    }
}
