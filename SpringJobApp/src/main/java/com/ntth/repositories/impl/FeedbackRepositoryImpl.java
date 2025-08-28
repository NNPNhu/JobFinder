/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.repositories.impl;

import com.ntth.pojo.Feedback;
import com.ntth.pojo.Follow;
import com.ntth.repositories.FeedbackRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

/**
 *
 * @author User
 */
@Repository
@Transactional
public class FeedbackRepositoryImpl implements FeedbackRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Feedback> findByToUserId(Integer toUserId) {
        Session session = factory.getObject().getCurrentSession();
        Query<Feedback> query = session.createNamedQuery("Feedback.findByToUserId", Feedback.class);
        query.setParameter("toUserId", toUserId);
        return query.getResultList();
    }

    @Override
    public List<Feedback> findByJobId(Integer jobId) {
        Session session = factory.getObject().getCurrentSession();
        Query<Feedback> query = session.createNamedQuery("Feedback.findByJobId", Feedback.class);
        query.setParameter("jobId", jobId);
        return query.getResultList();
    }

    @Override
    public Feedback save(Feedback fb) {
        Session session = factory.getObject().getCurrentSession();
        if (fb == null) {
            throw new IllegalStateException("⚠ Đối tượng Follow chưa được khởi tạo!");
        }
        if (fb.getId() == null) {
            session.persist(fb);  // Nếu entity chưa tồn tại, thêm mới
        } else {
            session.merge(fb);  // Nếu entity đã tồn tại, cập nhật
        }
        session.refresh(fb);//nạp lại dữ liệu để trả về 

        return fb;
    }
}
