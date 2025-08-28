/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.controllers;

import com.ntth.pojo.User;
import com.ntth.services.FollowService;
import com.ntth.services.UserService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author User
 */
@Controller
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

//    // Hiển thị danh sách công ty mà người dùng đã theo dõi
//    @GetMapping
//    public String listFollowedCompanies(Model model, Principal principal) {
//        String username = principal.getName();
//        User currentUser = userService.getUserByUsername(username);
//
//        if (currentUser == null) {
//            return "error/401"; // Trả về trang lỗi nếu user chưa đăng nhập
//        }
//
//        model.addAttribute("followedCompanies", followService.getFollowedCompanies(currentUser.getId()));
//        return "follow-list"; // Trả về trang danh sách công ty đã theo dõi
//    }

    
    // Hiển thị form theo dõi công ty mới
    @GetMapping("/add")
    public String followCompanyView(Model model) {
        model.addAttribute("companyId", 0); // Placeholder để nhập ID công ty
        return "follow-form"; // Trả về trang nhập ID công ty để theo dõi
    }

    // Xử lý theo dõi công ty
    @PostMapping("/add/{companyId}")
    public String followCompany(@PathVariable("companyId") int companyId, Principal principal) {
        String username = principal.getName();
        User currentUser = userService.getUserByUsername(username);

        if (currentUser == null || currentUser.getRole() != User.Role.CANDIDATE) {
            return "redirect:"; //nếu không phải ứng viên
        }

        followService.followCompany(currentUser.getId(), companyId);
        return "redirect:/company"; // Redirect về danh sách công ty đã theo dõi
    }
}
