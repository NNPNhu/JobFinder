/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ntth.services;

import com.ntth.pojo.Feedback;
import com.ntth.pojo.User;
import java.util.List;

/**
 *
 * @author User
 */
public interface FeedbackService {
    List<Feedback> getFeedbackForJob(Integer jobId);
    List<Feedback> getFeedbackForUser(Integer userId);
    Feedback addFeedback(User fromUser, Integer toUserId, Integer rating, String comment);
    

}
