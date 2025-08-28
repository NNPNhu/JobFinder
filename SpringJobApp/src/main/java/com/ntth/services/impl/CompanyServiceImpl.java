/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.services.impl;

import com.ntth.pojo.Company;
import com.ntth.services.CompanyService;
import com.ntth.repositories.CompanyRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author LOQ
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // Lưu thông tin công ty vào bảng company
    @Override
    @Transactional
    public void save(Company company) {
        companyRepository.save(company); // Sử dụng JPA để lưu bản ghi
    }

    @Override
    @Transactional
    public Company getCompanyById(int id) {
        return this.companyRepository.getCompanyById(id);
    }

    @Override
    public Company getCompanyByUserId(int id) {
        return companyRepository.findByUserId(id);
    }
      public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
}
