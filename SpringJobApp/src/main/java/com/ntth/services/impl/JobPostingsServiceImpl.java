/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ntth.pojo.Job;
import com.ntth.repositories.JobPostingsRepository;
import com.ntth.services.JobPostingsService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author admin
 */
@Service
public class JobPostingsServiceImpl implements JobPostingsService {

    @Autowired
    //@Qualifier("JobPostingsRepositoryImpl")//tên mặc định của bean
    private JobPostingsRepository jobRepo;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<Job> getJob(Map<String, String> params) {
        return this.jobRepo.getJob(params);
    }

    @Override
    public Job getJobPostingsById(int id) {
        return this.jobRepo.getJobById(id);
    }

    @Override
    public Job createOrUpdate(Job p) {
//        if (!p.getFile().isEmpty()) {
//            try {
//                Map res = cloudinary.uploader().upload(p.getFile().getBytes(),
//                        ObjectUtils.asMap("resource_type", "auto"));
//                p.setImage(res.get("secure_url").toString());
//            } catch (IOException ex) {
//                Logger.getLogger(JobPostingsServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

        return this.jobRepo.createOrUpdate(p);
    }

    @Override
    public void deleleJobPostings(int id) {
        this.jobRepo.deleleJob(id);
    }

    @Override
    public long countJobs(Map<String, String> params) {
        return jobRepo.countJobs(params);
    }
}
