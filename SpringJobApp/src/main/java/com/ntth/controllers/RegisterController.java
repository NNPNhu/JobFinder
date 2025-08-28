/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ntth.pojo.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ntth.services.UserService;
import com.ntth.pojo.User;
import com.ntth.repositories.UserRepository;
import com.ntth.services.CompanyService;
import com.ntth.util.CloudinaryUtil;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;
    // Getter để truy cập UserRepository từ UserService

    @Autowired
    private CloudinaryUtil cloudinaryUtil;

    @Autowired
    private UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        // Hiển thị form đăng ký
        model.addAttribute("user", new User());
        return "register"; // Trả về file register.html trong templates
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            @RequestParam("avatar") MultipartFile avatar,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "taxCode", required = false) String taxCode,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "website", required = false) String website,
            @RequestParam(value = "image1", required = false) MultipartFile image1,
            @RequestParam(value = "image2", required = false) MultipartFile image2,
            @RequestParam(value = "image3", required = false) MultipartFile image3,
            RedirectAttributes redirectAttributes) {

        try {
//            // Kiểm tra tính duy nhất của username và email
//            if (userService.getUserRepository().existsByUsername(username)
//                    || userService.getUserRepository().existsByEmail(email)) {
//                redirectAttributes.addFlashAttribute("error", "Tên đăng nhập hoặc email đã tồn tại.");
//                return "redirect:/register";
//            }
            // Kiểm tra dữ liệu bắt buộc cho người dùng
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty()
                    || password.isEmpty() || role.isEmpty() || avatar.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Tất cả thông tin người dùng và ảnh đại diện đều bắt buộc.");
                return "redirect:/register";
            }

            // Kiểm tra dữ liệu bắt buộc cho EMPLOYER
            int imageCount = 0;
            if (!image1.isEmpty()) {
                imageCount++;
            }
            if (!image2.isEmpty()) {
                imageCount++;
            }
            if (!image3.isEmpty()) {
                imageCount++;
            }
            if (role.equals("EMPLOYER") && (companyName == null || companyName.isEmpty()
                    || taxCode == null || taxCode.isEmpty() || imageCount < 3)) {
                redirectAttributes.addFlashAttribute("error", "Nhà tuyển dụng phải cung cấp tên công ty, mã số thuế và ít nhất 3 ảnh.");
                return "redirect:/register";
            }

            // Upload avatar lên Cloudinary
            String avatarUrl = cloudinaryUtil.uploadToCloudinary(avatar);
            if (avatarUrl == null) {
                redirectAttributes.addFlashAttribute("error", "Lỗi khi upload ảnh đại diện.");
                return "redirect:/register";
            }

            // Lưu thông tin người dùng
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPhone(phone);
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(User.Role.valueOf(role));
            user.setAvatar(avatarUrl);
//            if (role.equals("EMPLOYER")) {
//                user.setActive(false); // Chờ phê duyệt
//            }
            userService.addUser(user);

            // Lấy user từ database để đảm bảo có ID
            User savedUser = userService.getUserByUsername(username);
            if (savedUser == null || savedUser.getId() == null) {
                redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu thông tin người dùng.");
                return "redirect:/register";
            }

            // Lưu thông tin công ty cho EMPLOYER
            if (role.equals("EMPLOYER")) {
                String image1Url = cloudinaryUtil.uploadToCloudinary(image1);
                String image2Url = cloudinaryUtil.uploadToCloudinary(image2);
                String image3Url = cloudinaryUtil.uploadToCloudinary(image3);
                if (image1Url == null || image2Url == null || image3Url == null) {
                    redirectAttributes.addFlashAttribute("error", "Lỗi khi upload ảnh công ty.");
                    return "redirect:/register";
                }

                Company company = new Company();
                company.setUserId(savedUser);
                company.setName(companyName);
                company.setTaxCode(taxCode);
                company.setDescription(description);
                company.setAddress(address);
                company.setWebsite(website);
                company.setImage1(image1Url);
                company.setImage2(image2Url);
                company.setImage3(image3Url);
                //company.setApproved(false); // Chờ ADMIN xét duyệt
                company.setApproved(true);
                companyService.save(company);
            }

            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Nếu bạn là nhà tuyển dụng, vui lòng đợi quản trị viên xét duyệt.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Role không hợp lệ.");
            return "redirect:/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đăng ký thất bại: " + e.getMessage());
            return "redirect:/register";
        }
    }
}