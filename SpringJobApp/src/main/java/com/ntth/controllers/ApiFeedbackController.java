/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.controllers;

import com.ntth.pojo.Feedback;
import com.ntth.pojo.User;
import com.ntth.services.FeedbackService;
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
public class ApiFeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    @PostMapping("/feedback/add/{toUserId}")
    public ResponseEntity<?> addFeedback(@PathVariable("toUserId") int toUserId,
            Principal principal,
            @RequestParam(name = "rating") int rating,
            @RequestParam(name = "comment") String comment) {
        System.out.println("[DEBUG] Bắt đầu xử lý phản hồi...");

        // Lấy username từ Principal
        String username = principal.getName();
        System.out.println("[DEBUG] Username người đánh giá: " + username);

        // Tìm thông tin người đánh giá
        User fromUser = userService.getUserByUsername(username);
        if (fromUser == null) {
            System.out.println("[ERROR] Không tìm thấy người đánh giá!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("⚠ Không tìm thấy người đánh giá.");
        }
        System.out.println("[DEBUG] ID người đánh giá: " + fromUser.getId());

        // Gọi service để lưu phản hồi
        Feedback feedback = feedbackService.addFeedback(fromUser, toUserId, rating, comment);

        System.out.println("[DEBUG] Đã lưu phản hồi từ " + fromUser.getId() + " đến " + toUserId);
        System.out.println("[DEBUG] Nội dung phản hồi: " + feedback.getComment());

        return ResponseEntity.ok("Đánh giá thành công!");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Feedback>> getFeedbackForUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(feedbackService.getFeedbackForUser(userId));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Feedback>> getFeedbackForJob(@PathVariable Integer jobId) {
        return ResponseEntity.ok(feedbackService.getFeedbackForJob(jobId));
    }
}
