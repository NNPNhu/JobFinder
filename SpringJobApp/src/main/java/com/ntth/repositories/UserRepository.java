/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ntth.repositories;

import com.ntth.pojo.Feedback;
import com.ntth.pojo.User;
import java.util.List;

/**
 *
 * @author LOQ
 */
public interface UserRepository {

    User addUser(User user);

    // Tìm người dùng theo username để hỗ trợ đăng nhập
    User getUserByUsername(String username);

    User getUserById(int id);

    // Kiểm tra tính duy nhất của username để tránh trùng lặp khi đăng ký
//    boolean existsByUsername(String username);
//
//    // Kiểm tra tính duy nhất của email để tránh trùng lặp khi đăng ký
//    boolean existsByEmail(String email);
    void save(User user);

    List<Feedback> getFeedbacks(int userId);

    List<User> getCompanyUsersForCandidate(int userId);

    List<User> getUsersWorkedWith(int userId, User.Role role);
    
    List<User> getUsersByActiveStatus(boolean isActive);
    
    void activateUser(int userId);
}
