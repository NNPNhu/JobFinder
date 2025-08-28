/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ntth.repositories;

import com.ntth.pojo.Company;
import jakarta.data.repository.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author LOQ
 */
public interface CompanyRepository {
    Company save(Company company);
    Company findByUserId(Integer userId);
    Company getCompanyById(int id);
    List<Company> findAll();
}
