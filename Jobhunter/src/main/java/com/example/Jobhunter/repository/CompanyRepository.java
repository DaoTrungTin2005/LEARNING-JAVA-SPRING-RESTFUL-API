package com.example.Jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Jobhunter.domain.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
