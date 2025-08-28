/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.repositories.impl;

import com.ntth.pojo.Follow;
import com.ntth.pojo.User;
import com.ntth.repositories.FollowRepository;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
public class FollowRepositoryImpl implements FollowRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

//    public void followCompany(int userId, int companyId) {
//        Session session = this.factory.getObject().getCurrentSession();
//        Follow follow = new Follow();
//        follow.setUserId(userId);
//        follow.setCompanyId(companyId);
//        session.persist(follow);
//    }
    @Override
    public Follow findByUserAndCompany(int userId, int companyId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Follow> cq = cb.createQuery(Follow.class);
        Root<Follow> root = cq.from(Follow.class);

        // Lọc theo userId và companyId
        cq.select(root).where(
                cb.equal(root.get("userId").get("id"), userId),
                cb.equal(root.get("companyId").get("id"), companyId)
        );

        TypedQuery<Follow> query = session.createQuery(cq);
        List<Follow> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<User> findUsersByCompanyId(int companyId) {
        System.out.println("🔍 FollowRepositoryImpl: Đang tìm User với CômpanyId: " + companyId); // Kiểm tra userId
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<Follow> root = cq.from(Follow.class);

        // Lấy danh sách userId của các ứng viên theo dõi companyId
        cq.select(root.get("userId")).where(cb.equal(root.get("companyId").get("id"), companyId));

        TypedQuery<User> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public void save(Follow follow) {
        Session session = factory.getObject().getCurrentSession();
        if (follow == null) {
            throw new IllegalStateException("⚠ Đối tượng Follow chưa được khởi tạo!");
        }
        if (follow.getId() == null) {
            session.persist(follow);  // Nếu entity chưa tồn tại, thêm mới
        } else {
            session.merge(follow);  // Nếu entity đã tồn tại, cập nhật
        }
    }

    @Override
    public List<Integer> getFollowedCompanyIdsByUser(int userId) { 
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
        Root<Follow> root = cq.from(Follow.class);

        // Lấy danh sách companyId mà user đã theo dõi
        cq.select(root.get("companyId").get("id")).where(cb.equal(root.get("userId").get("id"), userId));

        TypedQuery<Integer> query = session.createQuery(cq);
        return query.getResultList();
    }

}
