/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ntth.pojo.Company;
import com.ntth.pojo.Feedback;
import com.ntth.pojo.User;
import com.ntth.repositories.CompanyRepository;
import com.ntth.services.UserService;
import com.ntth.repositories.UserRepository;
import com.ntth.repositories.impl.UserRepositoryImpl;
import com.ntth.services.CompanyService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import com.ntth.util.CloudinaryUtil;
import com.ntth.util.JwtUtils;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author admin
 */
//@Service báo cho Spring rằng đây là một bean cần được quản lý.
//@Service("userDetailsService")//dat ten hat dau
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CloudinaryUtil cloudinaryUtil;

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private CompanyService companyService;

    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.getUserByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Đang cố gắng tải người dùng: " + username);
        User u = userRepository.getUserByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Tên đăng nhập không tồn tại: " + username);
        }
        if (!u.isActive()) {
            throw new UsernameNotFoundException("Tài khoản chưa được kích hoạt: " + username);
        }
        // Kiểm tra approved cho EMPLOYER
        if (u.getRole() == User.Role.EMPLOYER) {
            Company company = companyRepository.findByUserId(u.getId());
            System.out.println("Company for user " + username + ": " + (company != null ? company.getApproved() : "null"));
            if (company == null || !company.getApproved()) {
                throw new UsernameNotFoundException("Tài khoản EMPLOYER chưa được phê duyệt: " + username);
            }
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(u.getRole().name()));
        System.out.println("Authorities: " + authorities);
        // Trả về UserDetails
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                authorities
        );
    }

//    //xác thực người dùng dựa trên username và password
//    @Override
//    public User authenticate(String username, String password) {
//        User user = userRepository.getUserByUsername(username);
//        if (user != null && passwordEncoder.matches(password, user.getPassword()) && user.isActive()) {
//            return user;
//        }
//        return null;
//    }

    @Override
    public boolean isEmployerApproved(Integer userId) {   
        Company company = companyRepository.findByUserId(userId);
        return company != null && company.getApproved();
    }

    @Override
    @Transactional
    public User addUser(User user) {
        // Kiểm tra trùng lặp trước khi thêm (tùy chọn, nếu không đã có trong RegisterController)
//        if (userRepository.existsByUsername(user.getUsername())
//                || userRepository.existsByEmail(user.getEmail())) {
//            throw new RuntimeException("Tên đăng nhập hoặc email đã tồn tại");
//        }
        return this.userRepository.addUser(user);
    }

    @Override
    @Transactional
    public void save(User user) {
        this.userRepository.save(user); // Lưu người dùng vào bảng user
    }

    // Getter để truy cập UserRepository từ Controller
    @Override
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    @Transactional
    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    @Override
    public List<Feedback> getFeedbacks(int id) {
        return this.userRepository.getFeedbacks(id);
    }

    public List<User> getCompanyUsersForCandidate(int userId) {
        return userRepository.getCompanyUsersForCandidate(userId);
    }

    public List<User> getUsersWorkedWith(int userId, User.Role role) {
        return userRepository.getUsersWorkedWith(userId, role);
    }

    public List<User> getUsersByActiveStatus(boolean isActive) {
        return userRepository.getUsersByActiveStatus(isActive);
    }

    public void activateUser(int userId) {
        userRepository.activateUser(userId);

    }
    //xác thực người dùng dựa trên username và password
//
// @Override
//    public Map<String, Object> authenticateUser(String username, String password) {
//        Map<String, Object> response = new HashMap<>();
//        // Tìm user theo username
//        User user = userRepository.getUserByUsername(username);
//        if (user == null) {
//            response.put("success", false);
//            response.put("message", "Tên đăng nhập không tồn tại.");
//            return response;
//        }
//        // Kiểm tra mật khẩu
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            response.put("success", false);
//            response.put("message", "Mật khẩu không đúng.");
//            return response;
//        }
//        // Kiểm tra trạng thái tài khoản (EMPLOYER chờ phê duyệt)
//        if (!user.isActive()) {
//            response.put("success", false);
//            response.put("message", "Tài khoản của bạn đang chờ phê duyệt.");
//            return response;
//        }
//        if (user.getRole() == User.Role.EMPLOYER) {
//            Company company = companyRepository.findByUserId(user.getId());
//            if (company == null || !company.getApproved()) {
//                response.put("success", false);
//                response.put("message", "Tài khoản nhà tuyển dụng chưa được phê duyệt.");
//                return response;
//            }
//        }
//        // Tạo token
//        try {
//            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().toString());
//            response.put("success", true);
//            response.put("message", "Đăng nhập thành công!");
//            response.put("token", token);
//            response.put("user", user); // Đảm bảo user được thêm vào response
//            // Trả về thông tin user dưới dạng Map
//            //Map<String, Object> userInfo = new HashMap<>();
////            userInfo.put("username", user.getUsername());
////            userInfo.put("role", user.getRole().toString());
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", "Lỗi khi tạo token: " + e.getMessage());
//        }
//
//        return response;
//    }
//
//    @Override
//    public Map<String, Object> registerUser(
//            String firstName,
//            String lastName,
//            String email,
//            String phone,
//            String username,
//            String password,
//            String role,
//            MultipartFile avatar,
//            String companyName,
//            String taxCode,
//            String description,
//            String address,
//            String website,
//            MultipartFile image1,
//            MultipartFile image2,
//            MultipartFile image3) {
//
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            // Kiểm tra tính duy nhất của username và email
////            if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
////                response.put("success", false);
////                response.put("message", "Tên đăng nhập hoặc email đã tồn tại.");
////                return response;
////            }
//
//            // Kiểm tra dữ liệu bắt buộc cho người dùng
//            if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()
//                    || email == null || email.trim().isEmpty() || username == null || username.trim().isEmpty()
//                    || password == null || password.trim().isEmpty() || role == null || role.trim().isEmpty()
//                    || avatar == null || avatar.isEmpty()) {
//                response.put("success", false);
//                response.put("message", "Tất cả thông tin người dùng và ảnh đại diện đều bắt buộc.");
//                return response;
//            }
//
//            // Kiểm tra dữ liệu bắt buộc cho EMPLOYER
//            int imageCount = 0;
//            if (image1 != null && !image1.isEmpty()) {
//                imageCount++;
//            }
//            if (image2 != null && !image2.isEmpty()) {
//                imageCount++;
//            }
//            if (image3 != null && !image3.isEmpty()) {
//                imageCount++;
//            }
//            if ("EMPLOYER".equalsIgnoreCase(role) && (companyName == null || companyName.trim().isEmpty()
//                    || taxCode == null || taxCode.trim().isEmpty() || imageCount < 3)) {
//                response.put("success", false);
//                response.put("message", "Nhà tuyển dụng phải cung cấp tên công ty, mã số thuế và ít nhất 3 ảnh.");
//                return response;
//            }
//
//            // Upload avatar lên Cloudinary
//            String avatarUrl = cloudinaryUtil.uploadToCloudinary(avatar);
//            if (avatarUrl == null) {
//                response.put("success", false);
//                response.put("message", "Lỗi khi upload ảnh đại diện.");
//                return response;
//            }
//
//            // Lưu thông tin người dùng
//                User user = new User();
//            user.setFirstName(firstName.trim());
//            user.setLastName(lastName.trim());
//            user.setEmail(email.trim());
//            user.setPhone(phone != null ? phone.trim() : null);
//            user.setUsername(username.trim());
//            user.setPassword(passwordEncoder.encode(password)); // Mã hóa mật khẩu tại tầng service
//            user.setRole(User.Role.valueOf(role.toUpperCase()));
//            user.setAvatar(avatarUrl);
//            user.setActive(!"EMPLOYER".equalsIgnoreCase(role)); // EMPLOYER chờ phê duyệt
//
//            userRepository.addUser(user);
//
//            // Lấy user từ database để đảm bảo có ID
//            User savedUser = userRepository.getUserByUsername(username);
//            if (savedUser == null || savedUser.getId() == null) {
//                response.put("success", false);
//                response.put("message", "Lỗi khi lưu thông tin người dùng.");
//                return response;
//            }
//
//            // Lưu thông tin công ty cho EMPLOYER
//            if ("EMPLOYER".equalsIgnoreCase(role)) {
//                String image1Url = cloudinaryUtil.uploadToCloudinary(image1);
//                String image2Url = cloudinaryUtil.uploadToCloudinary(image2);
//                String image3Url = cloudinaryUtil.uploadToCloudinary(image3);
//                if (image1Url == null || image2Url == null || image3Url == null) {
//                    response.put("success", false);
//                    response.put("message", "Lỗi khi upload ảnh công ty.");
//                    return response;
//                }
//
//                Company company = new Company();
//                company.setUserId(savedUser);
//                company.setName(companyName.trim());
//                company.setTaxCode(taxCode.trim());
//                company.setDescription(description != null ? description.trim() : null);
//                company.setAddress(address != null ? address.trim() : null);
//                company.setWebsite(website != null ? website.trim() : null);
//                company.setImage1(image1Url);
//                company.setImage2(image2Url);
//                company.setImage3(image3Url);
//                company.setApproved(false); // Chờ ADMIN xét duyệt
//                companyService.save(company);
//            }
//
//            response.put("success", true);
//            response.put("message", "Đăng ký thành công! Nếu bạn là nhà tuyển dụng, vui lòng đợi quản trị viên xét duyệt.");
//            response.put("user", savedUser); // Trả về thông tin user (có thể tùy chỉnh)
//            return response;
//
//        } catch (IllegalArgumentException e) {
//            response.put("success", false);
//            response.put("message", "Role không hợp lệ: " + e.getMessage());
//            return response;
//        } catch (IOException e) {
//            response.put("success", false);
//            response.put("message", "Lỗi khi upload file: " + e.getMessage());
//            return response;
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", "Đăng ký thất bại: " + e.getMessage());
//            return response;
//        }
//    }
    @Override
    public Map<String, Object> authenticateUser(String username, String password) {
        Map<String, Object> response = new HashMap<>();
        // Tìm user theo username
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            response.put("success", false);
            response.put("message", "Tên đăng nhập không tồn tại.");
            return response;
        }
        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(password, user.getPassword())) {
            response.put("success", false);
            response.put("message", "Mật khẩu không đúng.");
            return response;
        }
        // Kiểm tra trạng thái tài khoản (EMPLOYER chờ phê duyệt)
        if (!user.isActive()) {
            response.put("success", false);
            response.put("message", "Tài khoản của bạn đang chờ phê duyệt.");
            return response;
        }
        if (user.getRole() == User.Role.EMPLOYER) {
            Company company = companyRepository.findByUserId(user.getId());
            if (company == null || !company.getApproved()) {
                response.put("success", false);
                response.put("message", "Tài khoản nhà tuyển dụng chưa được phê duyệt.");
                return response;
            }
        }
        // Tạo token
        try {
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().toString());
            response.put("success", true);
            response.put("message", "Đăng nhập thành công!");
            response.put("token", token);
            response.put("user", user); // Đảm bảo user được thêm vào response

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi tạo token: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Map<String, Object> registerUser(
            String firstName,
            String lastName,
            String email,
            String phone,
            String username,
            String password,
            String role,
            MultipartFile avatar,
            String companyName,
            String taxCode,
            String description,
            String address,
            String website,
            MultipartFile image1,
            MultipartFile image2,
            MultipartFile image3) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Kiểm tra tính duy nhất của username và email
//            if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
//                response.put("success", false);
//                response.put("message", "Tên đăng nhập hoặc email đã tồn tại.");
//                return response;
//            }

            // Kiểm tra dữ liệu bắt buộc cho người dùng
            if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()
                    || email == null || email.trim().isEmpty() || username == null || username.trim().isEmpty()
                    || password == null || password.trim().isEmpty() || role == null || role.trim().isEmpty()
                    || avatar == null || avatar.isEmpty()) {
                response.put("success", false);
                response.put("message", "Tất cả thông tin người dùng và ảnh đại diện đều bắt buộc.");
                return response;
            }

            // Kiểm tra dữ liệu bắt buộc cho EMPLOYER
            int imageCount = 0;
            if (image1 != null && !image1.isEmpty()) {
                imageCount++;
            }
            if (image2 != null && !image2.isEmpty()) {
                imageCount++;
            }
            if (image3 != null && !image3.isEmpty()) {
                imageCount++;
            }
            if ("EMPLOYER".equalsIgnoreCase(role) && (companyName == null || companyName.trim().isEmpty()
                    || taxCode == null || taxCode.trim().isEmpty() || imageCount < 3)) {
                response.put("success", false);
                response.put("message", "Nhà tuyển dụng phải cung cấp tên công ty, mã số thuế và ít nhất 3 ảnh.");
                return response;
            }

            // Upload avatar lên Cloudinary
            String avatarUrl = cloudinaryUtil.uploadToCloudinary(avatar);
            if (avatarUrl == null) {
                response.put("success", false);
                response.put("message", "Lỗi khi upload ảnh đại diện.");
                return response;
            }

            // Lưu thông tin người dùng
            User user = new User();
            user.setFirstName(firstName.trim());
            user.setLastName(lastName.trim());
            user.setEmail(email.trim());
            user.setPhone(phone != null ? phone.trim() : null);
            user.setUsername(username.trim());
            user.setPassword(password); // Mã hóa mật khẩu tại tầng service
            user.setRole(User.Role.valueOf(role.toUpperCase()));
            user.setAvatar(avatarUrl);
            user.setActive(!"EMPLOYER".equalsIgnoreCase(role)); // EMPLOYER chờ phê duyệt

            userRepository.addUser(user);

            // Lấy user từ database để đảm bảo có ID
            User savedUser = userRepository.getUserByUsername(username);
            if (savedUser == null || savedUser.getId() == null) {
                response.put("success", false);
                response.put("message", "Lỗi khi lưu thông tin người dùng.");
                return response;
            }

            // Lưu thông tin công ty cho EMPLOYER
            if ("EMPLOYER".equalsIgnoreCase(role)) {
                String image1Url = cloudinaryUtil.uploadToCloudinary(image1);
                String image2Url = cloudinaryUtil.uploadToCloudinary(image2);
                String image3Url = cloudinaryUtil.uploadToCloudinary(image3);
                if (image1Url == null || image2Url == null || image3Url == null) {
                    response.put("success", false);
                    response.put("message", "Lỗi khi upload ảnh công ty.");
                    return response;
                }

                Company company = new Company();
                company.setUserId(savedUser);
                company.setName(companyName.trim());
                company.setTaxCode(taxCode.trim());
                company.setDescription(description != null ? description.trim() : null);
                company.setAddress(address != null ? address.trim() : null);
                company.setWebsite(website != null ? website.trim() : null);
                company.setImage1(image1Url);
                company.setImage2(image2Url);
                company.setImage3(image3Url);
                company.setApproved(false); // Chờ ADMIN xét duyệt
                companyService.save(company);
            }

            response.put("success", true);
            response.put("message", "Đăng ký thành công! Nếu bạn là nhà tuyển dụng, vui lòng đợi quản trị viên xét duyệt.");
            response.put("user", savedUser); // Trả về thông tin user (có thể tùy chỉnh)
            return response;

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", "Role không hợp lệ: " + e.getMessage());
            return response;
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Lỗi khi upload file: " + e.getMessage());
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Đăng ký thất bại: " + e.getMessage());
            return response;
        }
    }
}
