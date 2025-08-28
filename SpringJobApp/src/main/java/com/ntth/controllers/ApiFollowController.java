/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.controllers;

import com.ntth.pojo.Company;
import com.ntth.pojo.JobApplication;
import com.ntth.pojo.User;
import com.ntth.services.CompanyService;
import com.ntth.services.FollowService;
import com.ntth.services.JobApplicationService;
import com.ntth.services.UserService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
public class ApiFollowController {

    @Autowired
    private JobApplicationService jaService;

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.companyService.getAllCompanies(), HttpStatus.OK);
    }

    //lấy danh sách công ty người dùng đã theo dõi
    @GetMapping("/followed")
    public ResponseEntity<?> getFollowedCompanies(Principal principal) {
        if (principal == null) {
            System.out.println("⚠️ Principal is null!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập");
        }
        String username = principal.getName();
        User currentUser = userService.getUserByUsername(username);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }

        List<Integer> followedCompanyIds = followService.getFollowedCompanyIdsByUser(currentUser.getId());
        return ResponseEntity.ok(followedCompanyIds);
    }

    @PostMapping("/follow/{companyId}")
    public ResponseEntity<?> followCompany(@PathVariable int companyId, Principal principal) {
        String username = principal.getName();
        User currentUser = userService.getUserByUsername(username);

        if (currentUser == null || currentUser.getRole() != User.Role.CANDIDATE) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn không có quyền thực hiện hành động này.");
        }

        try {
            followService.followCompany(currentUser.getId(), companyId);
            return ResponseEntity.ok("Đã theo dõi công ty thành công!");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 - Conflict
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400 - Bad Request
        } catch (Exception e) {
            e.printStackTrace(); // log stack trace để debug nếu cần
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi máy chủ khi theo dõi công ty.");
        }
    }

    @GetMapping("/company/by-user/{userId}")
    public ResponseEntity<?> getCompanyByUserId(@PathVariable("userId") int userId) {
        Company company = companyService.getCompanyByUserId(userId);
        if (company != null) {
            return ResponseEntity.ok(company);
        } else {
            return ResponseEntity.ok(null);  // hoặc `ResponseEntity.noContent().build()` tùy ý
        }
    }

//    @PostMapping("/follow/{companyId}")
//    public ResponseEntity<?> followCompany(@PathVariable(value = "companyId") int companyId) {
//        // test không cần login
//        System.out.println("theo doi");
//        return ResponseEntity.ok("API gọi được");
//    }
}
