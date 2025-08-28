/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.controllers;

import com.ntth.pojo.Feedback;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ntth.pojo.User;
import com.ntth.services.UserService;
import com.ntth.util.JwtUtils;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/feedback/user/{userId}")
    public ResponseEntity<List<Feedback>> getFeedbacks(@PathVariable(value = "userId") int id) {
        return new ResponseEntity<>(this.userService.getFeedbacks(id), HttpStatus.OK);
    }
//
//    @PostMapping("/users")
//    public ResponseEntity<?> registerUser(
//            @RequestParam("firstName") String firstName,
//            @RequestParam("lastName") String lastName,
//            @RequestParam("email") String email,
//            @RequestParam(value = "phone", required = false) String phone,
//            @RequestParam("username") String username,
//            @RequestParam("password") String password,
//            @RequestParam("role") String role,
//            @RequestParam("avatar") MultipartFile avatar,
//            @RequestParam(value = "companyName", required = false) String companyName,
//            @RequestParam(value = "taxCode", required = false) String taxCode,
//            @RequestParam(value = "description", required = false) String description,
//            @RequestParam(value = "address", required = false) String address,
//            @RequestParam(value = "website", required = false) String website,
//            @RequestParam(value = "image1", required = false) MultipartFile image1,
//            @RequestParam(value = "image2", required = false) MultipartFile image2,
//            @RequestParam(value = "image3", required = false) MultipartFile image3) {
//
//        Map<String, Object> response = userService.registerUser(
//                firstName, lastName, email, phone, username, password, role,
//                avatar, companyName, taxCode, description, address, website,
//                image1, image2, image3);
//
//        if ((boolean) response.get("success")) {
//            return ResponseEntity.ok(response);
//        } else {
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    @PostMapping("/logins")
//    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
//        System.out.println("[DEBUG] Login request received: " + loginRequest);
//        String username = loginRequest.get("username");
//        String password = loginRequest.get("password");
//
//        // Kiểm tra đầu vào
//        Map<String, Object> response = new HashMap<>();
//        if (username == null || username.trim().isEmpty()) {
//            response.put("success", false);
//            response.put("message", "Tên đăng nhập là bắt buộc.");
//            System.out.println("[DEBUG] Missing or empty username");
//            return ResponseEntity.badRequest().body(response);
//        }
//        if (password == null || password.trim().isEmpty()) {
//            response.put("success", false);
//            response.put("message", "Mật khẩu là bắt buộc.");
//            System.out.println("[DEBUG] Missing or empty password");
//            return ResponseEntity.badRequest().body(response);
//        }
//        // Gọi userService để xác thực
//        Map<String, Object> authResult = userService.authenticateUser(username, password);
//        boolean success = (boolean) authResult.getOrDefault("success", false);
//
//        if (success) {
//            // Sử dụng token từ authResult
//            String token = (String) authResult.getOrDefault("token", "");
//            response.put("success", true);
//            response.put("message", "Đăng nhập thành công!");
//            response.put("token", token);
//
//            // Lấy thông tin user từ authResult
//            Object user = authResult.get("user");
//            if (user != null) {
//                response.put("user", user);
//            } else {
//                response.put("user", new HashMap<>()); // Trả về user rỗng nếu không có
//            }
//            System.out.println("[DEBUG] Login successful for user: " + username);
//            return ResponseEntity.ok(response);
//        } else {
//            // Đăng nhập thất bại, lấy thông báo lỗi từ authResult
//            String errorMessage = (String) authResult.getOrDefault("message", "Đăng nhập thất bại.");
//            response.put("success", false);
//            response.put("message", errorMessage);
//            System.out.println("[DEBUG] Login failed: " + errorMessage);
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//        }
//    }
//
//    @RequestMapping("/secure/profile")
//    //@ResponseBody
//    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                response.put("success", false);
//                response.put("message", "Missing or invalid Authorization header.");
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//            }
//            String token = authHeader.replace("Bearer ", "");
//            String username = jwtUtils.validateTokenAndGetUsername(token);
//            if (username == null) {
//                response.put("success", false);
//                response.put("message", "Token không hợp lệ hoặc đã hết hạn.");
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//            }
//            User user = userService.getUserByUsername(username);
//            if (user != null) {
//                response.put("success", true);
//                response.put("user", user);
//            } else {
//                response.put("success", false);
//                response.put("message", "Người dùng không tồn tại.");
//            }
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", "Token không hợp lệ: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//        }
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/users")
    public ResponseEntity<?> registerUser(
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
            @RequestParam(value = "image3", required = false) MultipartFile image3) {
        System.out.println("Received registration request: " + firstName + " " + lastName); // Debug
        Map<String, Object> response = userService.registerUser(
                firstName, lastName, email, phone, username, password, role,
                avatar, companyName, taxCode, description, address, website,
                image1, image2, image3);
        if ((boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/logins")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        System.out.println("[DEBUG] Login request received: " + loginRequest);
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        // Kiểm tra đầu vào
        Map<String, Object> response = new HashMap<>();
        if (username == null || username.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Tên đăng nhập là bắt buộc.");
            System.out.println("[DEBUG] Missing or empty username");
            return ResponseEntity.badRequest().body(response);
        }
        if (password == null || password.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Mật khẩu là bắt buộc.");
            System.out.println("[DEBUG] Missing or empty password");
            return ResponseEntity.badRequest().body(response);
        }
        // Gọi userService để xác thực
        Map<String, Object> authResult = userService.authenticateUser(username, password);
        boolean success = (boolean) authResult.getOrDefault("success", false);

        if (success) {
            // Sử dụng token từ authResult
            String token = (String) authResult.getOrDefault("token", "");
            response.put("success", true);
            response.put("message", "Đăng nhập thành công!");
            response.put("token", token);

            // Lấy thông tin user từ authResult
            Object user = authResult.get("user");
            if (user != null) {
                response.put("user", user);
            } else {
                response.put("user", new HashMap<>()); // Trả về user rỗng nếu không có
            }
            System.out.println("[DEBUG] Login successful for user: " + username);
            return ResponseEntity.ok(response);
        } else {
            // Đăng nhập thất bại, lấy thông báo lỗi từ authResult
            String errorMessage = (String) authResult.getOrDefault("message", "Đăng nhập thất bại.");
            response.put("success", false);
            response.put("message", errorMessage);
            System.out.println("[DEBUG] Login failed: " + errorMessage);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @RequestMapping("/secure/profile")
    //@ResponseBody
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.put("success", false);
                response.put("message", "Missing or invalid Authorization header.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            String token = authHeader.replace("Bearer ", "");
            String username = jwtUtils.validateTokenAndGetUsername(token);
            if (username == null) {
                response.put("success", false);
                response.put("message", "Token không hợp lệ hoặc đã hết hạn.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = userService.getUserByUsername(username);
            if (user != null) {
                response.put("success", true);
                response.put("user", user);
            } else {
                response.put("success", false);
                response.put("message", "Người dùng không tồn tại.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Token không hợp lệ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/worked-with")
    public ResponseEntity<?> getWorkedWithUsers(Principal principal) {
        String username = principal.getName();
        User currentUser = userService.getUserByUsername(username);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
        }

        List<User> users = userService.getUsersWorkedWith(currentUser.getId(), currentUser.getRole());

        return ResponseEntity.ok(users);
    }

    // API lấy danh sách user chưa kích hoạt
    @GetMapping("users/inactive")
    public ResponseEntity<List<User>> getInactiveUsers() {
        List<User> inactiveUsers = userService.getUsersByActiveStatus(false);
        return ResponseEntity.ok(inactiveUsers);
    }

    // API kích hoạt user theo ID
    @PutMapping("users/approve/{userId}")
    public ResponseEntity<String> approveUser(@PathVariable("userId") int userId) {
        try {
            userService.activateUser(userId);
            return ResponseEntity.ok("Người dùng đã được kích hoạt!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi kích hoạt người dùng.");
        }
    }
}
