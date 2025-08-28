/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.controllers;

import com.ntth.pojo.User;
import com.ntth.repositories.UserRepository;
import com.ntth.services.UserService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author admin
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginView(Model model, @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng, hoặc tài khoản chưa được kích hoạt.");
        }
        if (logout != null) {
            model.addAttribute("message", "Bạn đã đăng xuất thành công.");
        }
        return "login";
    }

    @GetMapping("users/workedWith")
    public String getUsersWorkedWith(Model model, Principal principal) {
        String username = principal.getName();
        User currentUser = userService.getUserByUsername(username);

        if (currentUser == null) {
            return "redirect:/login"; // Trả về trang đăng nhập
        }
        List<User> workedUsers = userService.getUsersWorkedWith(currentUser.getId(), currentUser.getRole());

        model.addAttribute("workedUsers", workedUsers);
        return "feedback"; // Trả về giao diện Thymeleaf
    }

//    @GetMapping("/user")
//    public String addView(Model model) {
//        model.addAttribute("user", new User());
//        return "approve";
//    }
    @GetMapping("/user")
    public String showInactiveUsers(Model model) {
        List<User> inactiveUsers = userService.getUsersByActiveStatus(false); // Lấy danh sách user chưa kích hoạt
        model.addAttribute("users", inactiveUsers);
        return "approve"; // Trả về giao diện duyệt user
    }

    @GetMapping("/user/approve/{userId}")
    public String activateUser(@PathVariable("userId") int userId) {
        userService.activateUser(userId); // Gọi Service để cập nhật trạng thái
        return "redirect:/"; // Quay lại danh sách user chưa kích hoạt
    }

}
