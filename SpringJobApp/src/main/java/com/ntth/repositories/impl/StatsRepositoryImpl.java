/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.repositories.impl;

import com.ntth.pojo.Job;
import com.ntth.pojo.JobApplication;
import com.ntth.pojo.Job;
import com.ntth.pojo.JobApplication;
import com.ntth.pojo.User;
import com.ntth.repositories.StatsRepository;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

/**
 *
 * @author User
 */
@Repository
@Transactional
public class StatsRepositoryImpl implements StatsRepository {

    //thống kê theo thời gian
//    public List<Object[]> statsRevenueByJob() {
//        try (Session session = HibernateUtils.getFACTORY().openSession()) {
////            CriteriaBuilder b = s.getCriteriaBuilder();
////            CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
////            Root root = q.from(JobApplication.class);
////            Join<JobApplication, Job> join = root.join("job", JoinType.RIGHT);
////            
////            q.multiselect(join.get("id"), join.get("title"), 
////                    b.sum(b.prod(root.get("unitPrice"), root.get("quantity"))));
////            q.groupBy(join.get("id"));
////            
////            Query query = s.createQuery(q);
////            return query.getResultList();
//            CriteriaBuilder cb = session.getCriteriaBuilder();
//            CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
////          root la lop co khoa ngoai
//            Root<JobApplication> root = query.from(JobApplication.class);
//            Expression<Date> dateExpr = root.get("applicationDate");
//
//            // Trích ngày (DATE) từ datetime
//            Expression<java.sql.Date> dateOnly = cb.function("DATE", java.sql.Date.class, dateExpr);
//
//            query.multiselect(dateOnly, cb.count(root.get("id")));
//            query.groupBy(dateOnly);
//            query.orderBy(cb.asc(dateOnly));
//
//            return session.createQuery(query).getResultList();
//
//        }
//    }
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Object[]> statsRevenueByTime(String time, int year) {
        try (Session session = factory.getObject().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

            Root<JobApplication> root = query.from(JobApplication.class);
            Join<JobApplication, Job> joinJob = root.join("jobId", JoinType.INNER);
            Join<User, JobApplication> joinUser = root.join("userId", JoinType.INNER);

            // Trích tháng, tuần hoặc ngày từ `createdDate`
            Expression<Integer> timeExpression = cb.function(time, Integer.class, joinJob.get("createdDate"));

            // Đếm số lượng Job
            Expression<Long> jobCount = cb.count(joinJob.get("id"));

            // Đếm số lượng Candidate duy nhất
            Expression<Long> candidateCount = cb.countDistinct(
                    cb.selectCase()
                            .when(cb.equal(joinUser.get("role"), "CANDIDATE"), joinUser.get("id"))
                            .otherwise(cb.literal(-1)) // 🔥 Giá trị mặc định để tránh lỗi null
            );

            // Đếm số lượng Employer duy nhất
            Expression<Long> employerCount = cb.countDistinct(
                    cb.selectCase()
                            .when(cb.equal(joinUser.get("role"), "EMPLOYER"), joinUser.get("id"))
                            .otherwise(cb.literal(-1)) // 🔥 Giá trị mặc định để tránh lỗi null
            );

            // Chọn dữ liệu cần thống kê
            query.multiselect(timeExpression, jobCount, candidateCount, employerCount);

            // Lọc theo năm
            query.where(cb.equal(cb.function("YEAR", Integer.class, joinJob.get("createdDate")), year));

            // Nhóm theo thời gian (tháng, tuần, ngày...)
            query.groupBy(timeExpression);
            query.orderBy(cb.asc(timeExpression));

            return session.createQuery(query).getResultList();
        }
    }

}
