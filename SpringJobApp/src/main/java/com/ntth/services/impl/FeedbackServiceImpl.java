/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.services.impl;

import com.ntth.pojo.Company;
import com.ntth.pojo.Feedback;
import com.ntth.pojo.Job;
import com.ntth.pojo.User;
import com.ntth.repositories.FeedbackRepository;
import com.ntth.repositories.JobApplicationRepository;
import com.ntth.repositories.JobPostingsRepository;
import com.ntth.repositories.UserRepository;
import com.ntth.services.FeedbackService;
import com.ntth.services.JobApplicationService;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobPostingsRepository jobRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobApplicationService jobApplicationService;

    public Feedback addFeedback(User fromUser, Integer toUserId, Integer rating, String comment) {
        User toUser = userRepository.getUserById(toUserId);

        if (toUser == null) {
            throw new IllegalArgumentException("⚠ Người bị đánh giá hoặc công việc không tồn tại.");
        }
        if (fromUser.getRole() == User.Role.CANDIDATE) {
            if (!jobApplicationService.isCandidateEligibleForFeedback(fromUser.getId())) {
                throw new IllegalArgumentException("⚠ Bạn chỉ có thể đánh giá nhà tuyển dụng sau khi được phỏng vấn.");
            }
        } else if (fromUser.getRole() == User.Role.EMPLOYER) {
            // Lấy danh sách các công ty của nhà tuyển dụng
            Set<Company> employerCompanies = fromUser.getCompanySet();

            // Kiểm tra ứng viên có làm việc tại công ty của nhà tuyển dụng không
            boolean isCandidateEligible = employerCompanies.stream().anyMatch(company
                    -> jobApplicationService.isEmployerEligibleForFeedback(toUserId, company.getId())
            );

            if (!isCandidateEligible) {
                throw new IllegalArgumentException("⚠ Bạn chỉ có thể đánh giá ứng viên đã làm việc cho công ty của bạn.");
            }
        } else {
            throw new IllegalArgumentException("⚠ Chỉ ứng viên và nhà tuyển dụng được phép đánh giá.");
        }

        Feedback feedback = new Feedback();
        feedback.setFromUserId(fromUser);
        feedback.setToUserId(toUser);
        feedback.setRating(rating);
        feedback.setComment(comment);
        feedback.setCreatedAt(new Date());

        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getFeedbackForUser(Integer userId) {
        return feedbackRepository.findByToUserId(userId);
    }

    public List<Feedback> getFeedbackForJob(Integer jobId) {
        return feedbackRepository.findByJobId(jobId);
    }
}
