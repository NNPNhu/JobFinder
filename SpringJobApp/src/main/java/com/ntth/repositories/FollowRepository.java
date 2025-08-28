/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ntth.repositories;

import com.ntth.pojo.Follow;
import com.ntth.pojo.User;
import jakarta.data.repository.Param;
import jakarta.data.repository.Query;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author User
 */
public interface FollowRepository {

    @Query("SELECT f FROM Follow f WHERE f.userId.id = :userId AND f.companyId.id = :companyId")
    Follow findByUserAndCompany(@Param("userId") int userId, @Param("companyId") int companyId);

    @Query("SELECT f.userId FROM Follow f WHERE f.companyId.id = :companyId")
    List<User> findUsersByCompanyId(@Param("companyId") int companyId);

    void save(Follow follow);

    List<Integer> getFollowedCompanyIdsByUser(int userId);
}
