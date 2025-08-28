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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author User
 */
@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    // Hiển thị danh sách phản hồi dành cho một người dùng
    @GetMapping("/user/{userId}")
    public String getFeedbackForUser(@PathVariable("userId") Integer userId, Model model) {
        List<Feedback> feedbackList = feedbackService.getFeedbackForUser(userId);
        model.addAttribute("feedbacks", feedbackList);
        return "feedback-list"; // Trả về trang hiển thị danh sách phản hồi
    }

    // Hiển thị danh sách phản hồi dành cho một công việc
    @GetMapping("/job/{jobId}")
    public String getFeedbackForJob(@PathVariable("userId") Integer jobId, Model model) {
        List<Feedback> feedbackList = feedbackService.getFeedbackForJob(jobId);
        model.addAttribute("feedbacks", feedbackList);
        return "feedback-job-list"; // Trả về trang hiển thị danh sách phản hồi theo công việc
    }

    // Hiển thị form để thêm phản hồi
    @GetMapping("/add/{toUserId}")
    public String addFeedbackView(@PathVariable("toUserId") int toUserId, Model model) {
        model.addAttribute("toUserId", toUserId);
        model.addAttribute("feedback", new Feedback());
        return "feedback-form"; // Trả về trang hiển thị form nhập phản hồi
    }

    // Xử lý khi người dùng gửi phản hồi
    @PostMapping("/add/{toUserId}")
    public String addFeedback(@PathVariable("toUserId") int toUserId,
                              Principal principal,
                              @RequestParam(name = "rating") int rating,
                              @RequestParam(name = "comment") String comment) {
        String username = principal.getName();
        User fromUser = userService.getUserByUsername(username);

        if (fromUser == null) {
            return "error/401"; // Trả về trang lỗi nếu không tìm thấy người dùng
        }

        feedbackService.addFeedback(fromUser, toUserId, rating, comment);

        return "redirect:/users/workedWith"; // Redirect về danh sách phản hồi
    }
}

