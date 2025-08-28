/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ntth.repositories;

import com.ntth.pojo.JobCategory;
import java.util.List;

/**
 *
 * @author LOQ
 */
public interface JobCategoryRepository {
    List<JobCategory> getAllCategories();
}
