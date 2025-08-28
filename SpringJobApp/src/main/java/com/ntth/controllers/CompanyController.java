/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.controllers;

import com.ntth.pojo.Company;
import com.ntth.pojo.User;
import com.ntth.services.CompanyService;
import com.ntth.services.FollowService;
import com.ntth.services.UserService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author User
 */
@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    
    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;
    @GetMapping
    public String listCompanies(Model model, Principal principal) {
        String username = principal.getName();
        User currentUser = userService.getUserByUsername(username);

        List<Integer> followedCompanyIds = followService.getFollowedCompanyIdsByUser(currentUser.getId());

        model.addAttribute("companies", companyService.getAllCompanies());
        model.addAttribute("followedCompanies", followedCompanyIds);
        return "company";
    }

//    @PostMapping("/add")
//    public String addCompany(@ModelAttribute("company") Company company) {
//        companyService.addOrUpdateCompany(company);
//        return "redirect:/companies";
//    }
}
