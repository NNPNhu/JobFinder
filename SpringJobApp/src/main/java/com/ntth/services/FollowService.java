/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ntth.services;

import com.ntth.pojo.Job;
import com.ntth.pojo.User;
import com.ntth.repositories.FollowRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author User
 */
public interface FollowService {

    void followCompany(int userId, int companyId);

    List<User> getFollowersByCompany(int companyId);

    void notifyCandidates(Job job);

    List<Integer> getFollowedCompanyIdsByUser(int userId);

}
