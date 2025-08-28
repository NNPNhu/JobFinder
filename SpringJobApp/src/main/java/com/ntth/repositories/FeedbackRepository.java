/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ntth.repositories;

import com.ntth.pojo.Feedback;
import java.util.List;

/**
 *
 * @author User
 */
public interface FeedbackRepository {

    List<Feedback> findByToUserId(Integer toUserId); // Lấy danh sách đánh giá theo người được đánh giá

    List<Feedback> findByJobId(Integer jobId); // Lấy đánh giá theo công việc

    Feedback save(Feedback fb);

}


