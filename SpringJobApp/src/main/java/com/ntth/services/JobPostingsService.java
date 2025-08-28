/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ntth.services;

import java.util.List;
import java.util.Map;
import com.ntth.pojo.Job;

/**
 *
 * @author LOQ
 */
public interface JobPostingsService {

    List<Job> getJob(Map<String, String> params);

    Job getJobPostingsById(int id);

    Job createOrUpdate(Job p);

    void deleleJobPostings(int id);

    long countJobs(Map<String, String> params);

}
