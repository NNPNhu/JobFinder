/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ntth.services;

import com.ntth.pojo.JobApplication;
import jakarta.data.repository.Param;
import java.util.List;
import java.util.Map;

/**
 *
 * @author User
 */
public interface JobApplicationService {

    List<JobApplication> getJobApplications(Map<String, String> params);

    JobApplication getJobApplicationById(int id);

    List<JobApplication> getJobApplicationsByUserId(int userId);

    List<JobApplication> getJobApplicationsByEmployerId(int employerId);

    JobApplication addOrUpdateJobApplication(JobApplication p);
//    void delelejobapplication(int id);
//    List<Comment> getComments(int jobapplicationId);

    boolean isCandidateEligibleForFeedback(Integer candidateId);

    boolean isEmployerEligibleForFeedback(Integer candidateId, Integer companyId);
}
