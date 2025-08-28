/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ntth.services;

import com.ntth.pojo.Company;
import java.util.List;

/**
 *
 * @author LOQ
 */
public interface CompanyService {

    void save(Company company);

    Company getCompanyById(int id);

    Company getCompanyByUserId(int id);

    List<Company> getAllCompanies();
}
