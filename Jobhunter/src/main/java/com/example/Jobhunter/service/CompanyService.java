package com.example.Jobhunter.service;

import org.springframework.stereotype.Service;

import com.example.Jobhunter.domain.Company;
import com.example.Jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company c) {
        return this.companyRepository.save(c);
    }
}
