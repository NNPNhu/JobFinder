/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.services.impl;

/**
 *
 * @author User
 */
import com.ntth.pojo.JobCategory;
import com.ntth.repositories.JobCategoryRepository;
import com.ntth.services.JobCategoryService;
import com.ntth.services.JobCategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author admin
 */
@Service
public class JobCategoryServiceImpl implements JobCategoryService {

    @Autowired
    private JobCategoryRepository cateRepo;

    @Override
    public List<JobCategory> getCates() {
        return this.cateRepo.getAllCategories();
    }

}
