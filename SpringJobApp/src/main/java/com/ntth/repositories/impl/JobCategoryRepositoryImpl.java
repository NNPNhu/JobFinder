/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.repositories.impl;

import com.ntth.pojo.JobCategory;
import com.ntth.repositories.JobCategoryRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

/**
 *
 * @author LOQ
 */
@Repository
@Transactional
public class JobCategoryRepositoryImpl implements JobCategoryRepository {
   private final SessionFactory sessionFactory;
    
    @Autowired
    public JobCategoryRepositoryImpl(LocalSessionFactoryBean factory) {
        this.sessionFactory = factory.getObject();
    }

    @Override
    public List<JobCategory> getAllCategories() {
        System.out.println("[DEBUG] Fetching all job categories");
        Session session = sessionFactory.getCurrentSession();
        Query<JobCategory> query = session.createQuery("FROM JobCategory", JobCategory.class);
        return query.getResultList();
    }
}
