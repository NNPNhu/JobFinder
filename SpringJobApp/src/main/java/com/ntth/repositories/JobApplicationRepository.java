/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ntth.repositories;

import com.ntth.pojo.JobApplication;
import jakarta.data.repository.Param;
import jakarta.data.repository.Query;
import java.util.List;
import java.util.Map;

/**
 *
 * @author User
 */
public interface JobApplicationRepository {

    List<JobApplication> getJobApplications(Map<String, String> params);

    JobApplication getJobApplicationById(int id);

    JobApplication addOrUpdateJobApplication(JobApplication p);

    void deleleJobApplication(int id);

    List<JobApplication> findByUserId(@Param("userId") int userId);    //List<JobApplication> findByUserId(int userId);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.jobId.companyId.userId.id = :employerId")
    List<JobApplication> findByEmployerId(@Param("employerId") int employerId);

    // Kiểm tra ứng viên có ứng tuyển với trạng thái cụ thể không (INTERVIEW)
    boolean existsByUserIdAndStatus(Integer userId, String status);

    // Kiểm tra ứng viên có ứng tuyển vào công việc thuộc công ty của nhà tuyển dụng không
    // @Query("SELECT COUNT(ja) > 0 FROM JobApplication ja WHERE ja.user.id = :userId AND ja.job.company.id = :companyId")
    boolean existsByUserIdAndCompanyId(@Param("userId") Integer userId, @Param("companyId") Integer companyId);
    //  List<Comment> getComments(int productId);
}
