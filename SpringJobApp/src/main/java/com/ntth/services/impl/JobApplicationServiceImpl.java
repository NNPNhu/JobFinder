/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.services.impl;

import com.ntth.pojo.JobApplication;
import com.ntth.repositories.JobApplicationRepository;
import com.ntth.services.JobApplicationService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ntth.pojo.Job;
import com.ntth.pojo.User;
import com.ntth.repositories.JobPostingsRepository;
import com.ntth.repositories.UserRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author User
 */
@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    @Autowired
    private JobApplicationRepository jobbappRepo;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<JobApplication> getJobApplications(Map<String, String> params) {
        return this.jobbappRepo.getJobApplications(params);
    }

    @Override
    @Transactional
    public JobApplication getJobApplicationById(int id) {
        return this.jobbappRepo.getJobApplicationById(id);
    }

    @Autowired
    private JobPostingsRepository jobRepo;
    @Autowired
    private UserRepository userRepository;

    @Override
    public JobApplication addOrUpdateJobApplication(JobApplication p) {

//        User user= userRepository.getUserById(3);
//        Job job= jobRepo.getJobById(1);
//        
//        p.setUser(user);
//        p.setJob(job);
        User user = p.getUser();

        // Lấy thông tin `Job` từ `JobApplication`
        Job job = p.getJobId();

//        if (user == null || job == null) {
//            throw new IllegalArgumentException("User hoặc Job không hợp lệ.");
//        }
        p.setApplicationDate(LocalDateTime.now());
        if (p.getFile() != null && !p.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(p.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                p.setResumeLink(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(JobApplicationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return this.jobbappRepo.addOrUpdateJobApplication(p);
    }

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Override
    public List<JobApplication> getJobApplicationsByUserId(int userId) {
        return jobApplicationRepository.findByUserId(userId);
    }

    @Override
    public List<JobApplication> getJobApplicationsByEmployerId(int employerId) {
        return jobApplicationRepository.findByEmployerId(employerId);
    }

    public boolean isCandidateEligibleForFeedback(Integer candidateId) {
        return jobApplicationRepository.existsByUserIdAndStatus(candidateId, "INTERVIEW");
    }

    public boolean isEmployerEligibleForFeedback(Integer candidateId, Integer companyId) {
        return jobApplicationRepository.existsByUserIdAndCompanyId(candidateId, companyId);
    }
}
