/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.services.impl;

import com.ntth.pojo.Follow;
import com.ntth.pojo.Job;
import com.ntth.pojo.User;
import com.ntth.repositories.CompanyRepository;
import com.ntth.repositories.FollowRepository;
import com.ntth.repositories.JobApplicationRepository;
import com.ntth.repositories.UserRepository;
import com.ntth.services.FollowService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Transactional
    @Override
    public void followCompany(int userId, int companyId) {
        Follow existingFollow = followRepository.findByUserAndCompany(userId, companyId);
        if (existingFollow != null) {
            throw new RuntimeException("Bạn đã theo dõi công ty này.");
        }

        Follow follow = new Follow();
        int uId = userRepository.getUserById(userId).getId().intValue();
        follow.setUserId(userRepository.getUserById(userId));

        follow.setCompanyId(companyRepository.getCompanyById(companyId));

        follow.setCreatedAt(new Date());

        followRepository.save(follow);
       

    }

    @Override
    public List<User> getFollowersByCompany(int companyId) {
        return followRepository.findUsersByCompanyId(companyId);
    }

    @Override
    public void notifyCandidates(Job job) {
        List<User> candidates = followRepository.findUsersByCompanyId(job.getCompanyId().getId());

        for (User candidate : candidates) {
            sendEmail(candidate.getEmail(),
                    "Tin tuyển dụng mới tại " + job.getCompanyId().getName(),
                    "Công ty bạn theo dõi vừa đăng tin tuyển dụng mới \n" + job.getTitle());
        }
    }

    @Autowired
    private JavaMailSender mailSender;

    private void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
     public List<Integer> getFollowedCompanyIdsByUser(int userId) {
        return followRepository.getFollowedCompanyIdsByUser(userId);
    }
}
