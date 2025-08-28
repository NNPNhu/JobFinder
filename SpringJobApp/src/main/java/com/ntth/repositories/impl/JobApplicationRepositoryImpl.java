/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.repositories.impl;

import com.ntth.pojo.JobApplication;
import com.ntth.repositories.JobApplicationRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author User
 */
@Repository
@Transactional
public class JobApplicationRepositoryImpl implements JobApplicationRepository {

    private static final int PAGE_SIZE = 4;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<JobApplication> getJobApplications(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<JobApplication> q = b.createQuery(JobApplication.class);
        Root root = q.from(JobApplication.class);
        q.select(root);

        Query query = s.createQuery(q);

        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            int start = (page - 1) * PAGE_SIZE;

            query.setMaxResults(PAGE_SIZE);
            query.setFirstResult(start);
        }

        return query.getResultList();

    }

    @Override
    public JobApplication getJobApplicationById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(JobApplication.class, id);

    }

    @Override
    public JobApplication addOrUpdateJobApplication(JobApplication p) {
        Session s = this.factory.getObject().getCurrentSession();
        if (p.getId() == 0) {
            s.persist(p);
        } else {
            s.merge(p);
        }

        return p;

    }

    @Override
    public void deleleJobApplication(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        JobApplication p = this.getJobApplicationById(id);
        s.remove(p);
    }
//    @Autowired
//    private JobApplicationRepository jobApplicationRepository;
//
//    @Override
//    public List<JobApplication> findByUserId(int userId) {
//        Session s = this.factory.getObject().getCurrentSession();
//        CriteriaBuilder cb = s.getCriteriaBuilder();
//        CriteriaQuery<JobApplication> cq = cb.createQuery(JobApplication.class);
//        Root<JobApplication> root = cq.from(JobApplication.class);
//
//        // Thêm điều kiện lọc theo userId
//        cq.select(root).where(cb.equal(root.get("user"), userId));
//
//        Query<JobApplication> query = s.createQuery(cq);
//        return query.getResultList();
// 

    @Override
    public List<JobApplication> findByUserId(int userId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<JobApplication> cq = cb.createQuery(JobApplication.class);
        Root<JobApplication> root = cq.from(JobApplication.class);

        // Thêm điều kiện lọc theo userId
        cq.select(root).where(cb.equal(root.get("userId").get("id"), userId));

        TypedQuery<JobApplication> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<JobApplication> findByEmployerId(int employerId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<JobApplication> cq = cb.createQuery(JobApplication.class);
        Root<JobApplication> root = cq.from(JobApplication.class);

        // Thêm điều kiện lọc theo employerId (là userId của công ty)
        cq.select(root).where(
                cb.equal(root.get("jobId").get("companyId").get("userId").get("id"), employerId),
                cb.equal(root.get("status"), "PENDING") // Chỉ lấy ứng tuyển có trạng thái PENDING));
        );
        TypedQuery<JobApplication> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public boolean existsByUserIdAndStatus(Integer userId, String status) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<JobApplication> root = cq.from(JobApplication.class);

        cq.select(cb.count(root))
                .where(cb.equal(root.get("userId").get("id"), userId),
                        cb.equal(root.get("status"), status));

        return session.createQuery(cq).getSingleResult() > 0;
    }

    @Override
    public boolean existsByUserIdAndCompanyId(Integer userId, Integer companyId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<JobApplication> root = cq.from(JobApplication.class);

        cq.select(cb.count(root))
                .where(cb.equal(root.get("userId").get("id"), userId),
                        cb.equal(root.get("jobId").get("companyId").get("id"), companyId));

        return session.createQuery(cq).getSingleResult() > 0;
    }

}
