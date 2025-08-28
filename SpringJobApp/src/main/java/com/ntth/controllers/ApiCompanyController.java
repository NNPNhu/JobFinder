/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.controllers;

import com.ntth.pojo.Company;
import com.ntth.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author User
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiCompanyController {

//    @Autowired
//    CompanyService companyService;
//    
//    @GetMapping("/company/by-user/{userId}")
//    public ResponseEntity<?> getCompanyByUserId(@PathVariable("userId") int userId) {
//        Company company = companyService.getCompanyByUserId(userId);
//        if (company != null) {
//            return ResponseEntity.ok(company);
//        } else {
//            return ResponseEntity.ok(null);  // hoặc `ResponseEntity.noContent().build()` tùy ý
//        }
//    }
}
