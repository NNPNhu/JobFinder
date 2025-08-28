/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.repositories.impl;

import com.ntth.pojo.Company;
import com.ntth.pojo.JobApplication;
import com.ntth.repositories.CompanyRepository;
import jakarta.data.repository.Param;
import jakarta.persistence.NoResultException;
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
public class CompanyRepositoryImpl implements CompanyRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CompanyRepositoryImpl(LocalSessionFactoryBean factory) {
        this.sessionFactory = factory.getObject();
    }
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Company save(Company company) {
        Session s = sessionFactory.getCurrentSession();
        if (company.getId() == null) {//đang thêm mới
            s.persist(company);
        } else {//khác null là đang cập nhật 
            s.merge(company);
        }

        s.refresh(company);//nạp lại dữ liệu để trả về 

        return company;
    }

    @Override
    public Company findByUserId(Integer userId) {
        System.out.println("🔍 CompanyrepositoryImpl: Đang tìm Company với userId: " + userId); // Kiểm tra userId

        if (userId == null) {
            throw new IllegalArgumentException("⚠ User ID không hợp lệ (null)!");
        }

        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT c FROM Company c WHERE c.userId.id = :userId", Company.class)
                .setParameter("userId", userId)
                .uniqueResult();
    }

    @Override
    public Company getCompanyById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID không hợp lệ!");
        }
        Session session = sessionFactory.getCurrentSession();
        return session.get(Company.class, id);
    }

    @Override
    public List<Company> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<Company> query = session.createQuery("FROM Company", Company.class);
        
        return query.getResultList();
    }

}
