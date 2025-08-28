/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ntth.repositories;

import com.ntth.pojo.Job;
import java.util.List;
import java.util.Map;

/**
 *
 * @author LOQ
 */
public interface JobPostingsRepository {
    List<Job> getJob(Map<String, String> params);
    Job getJobById(int id);
    Job createOrUpdate(Job p);
    void deleleJob(int id);
    long countJobs(Map<String, String> params);

}
