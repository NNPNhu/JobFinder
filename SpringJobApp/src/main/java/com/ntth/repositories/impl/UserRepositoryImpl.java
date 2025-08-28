/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.repositories.impl;

import com.ntth.pojo.Feedback;
import com.ntth.pojo.JobApplication;
import com.ntth.pojo.User;
import com.ntth.repositories.UserRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author admin
 */
@Repository//đánh dấu UserRepositoryImpl là một Spring bean
//nên Spring sẽ tự động tạo bean cho nó.
@Transactional//đảm bảo các phương thức chạy trong một giao dịch
public class UserRepositoryImpl implements UserRepository {

//    @Autowired
//    private LocalSessionFactoryBean factory;
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
    private final SessionFactory sessionFactory;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserRepositoryImpl(LocalSessionFactoryBean factory, BCryptPasswordEncoder passwordEncoder) {
        this.sessionFactory = factory.getObject();
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User addUser(User user) {
        Session s = sessionFactory.getCurrentSession();
        try {
            // Mã hóa mật khẩu
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            //Đặt active dựa trên role
            if (user.getRole() == User.Role.EMPLOYER) {
                user.setActive(false); // Chờ phê duyệt
            } else {
                user.setActive(true); // CANDIDATE hoặc ADMIN
            }
            user.setCreatedAt(new Date());
            // Lưu user
            s.persist(user);//để thêm bản ghi mới.
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Error adding user: " + e.getMessage());
        }
    }

    //Sử dụng Hibernate
    @Override
    public User getUserByUsername(String username) {
        Session s = sessionFactory.getCurrentSession();
        Query q = s.createQuery("FROM User WHERE username = :username", User.class);
        q.setParameter("username", username);

        try {
            Query query = s.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", username);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.out.println("[ERROR] Error finding user: " + e.getMessage());
            throw new RuntimeException("Lỗi khi tìm user theo username: " + e.getMessage(), e);
        }
    }

    @Override
    public User getUserById(int id) {
        Session s = sessionFactory.getCurrentSession();
        Query q = s.createNamedQuery("User.findById", User.class);
        q.setParameter("id", id);

        try {
            Query query = s.createNamedQuery("User.findById", User.class);
            query.setParameter("id", id);
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.out.println("[ERROR] Error finding user: " + e.getMessage());
            throw new RuntimeException("Lỗi khi tìm user theo id: " + e.getMessage(), e);
        }
    }

//    @Override
//    public boolean existsByUsername(String username) {
//        Session session = factory.getObject().getCurrentSession();
//        Query query = session.createQuery("SELECT COUNT(*) FROM User WHERE username = :username", Long.class);
//        query.setParameter("username", username);
//
//        try {
//            return query.getSingleResult() > 0;
//        } catch (NoResultException e) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean existsByEmail(String email) {
//        Session session = factory.getObject().getCurrentSession();
//        Query query = session.createQuery("SELECT COUNT(*) FROM User WHERE email = :email", Long.class);
//        query.setParameter("email", email);
//
//        try {
//            return query.getSingleResult() > 0;
//        } catch (NoResultException e) {
//            return false;
//        }
//    }
    @Override
    public void save(User user) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            session.saveOrUpdate(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Error saving user: " + e.getMessage());
        }
    }

    //Admin duyệt active cho EMPLOYER
    @Override
    public void activateUser(int userId) {
        Session s = sessionFactory.getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaUpdate<User> update = b.createCriteriaUpdate(User.class);
        Root<User> root = update.from(User.class);

        // Cập nhật trường active thành true
        update.set(root.get("active"), true);
        update.where(b.equal(root.get("id"), userId));

        Query query = s.createQuery(update);
        query.executeUpdate();
    }

    @Override
    public List<Feedback> getFeedbacks(int userId) {
        Session s = sessionFactory.getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Feedback> q = b.createQuery(Feedback.class);
        Root root = q.from(Feedback.class);
        q.select(root);

        q.where(b.equal(root.get("toUserId").get("id"), userId));

        Query query = s.createQuery(q);
        return query.getResultList();
    }

    @Override
    public List<User> getCompanyUsersForCandidate(int userId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<JobApplication> root = cq.from(JobApplication.class);

        // Lấy danh sách user thuộc công ty mà ứng viên đã ứng tuyển
        cq.select(root.get("jobId").get("companyId").get("userId"))
                .distinct(true)
                .where(cb.equal(root.get("userId").get("id"), userId));

        TypedQuery<User> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<User> getUsersWorkedWith(int userId, User.Role role) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<JobApplication> root = cq.from(JobApplication.class);

        if (role == User.Role.CANDIDATE) {
            // Lấy danh sách employer đã nhận ứng viên vào công ty của họ
            cq.select(root.get("jobId").get("companyId").get("userId"))
                    .distinct(true)
                    //                    .where(cb.equal(root.get("userId").get("id"), userId));
                    .where(cb.and(
                            cb.equal(root.get("userId").get("id"), userId),
                            cb.equal(root.get("status"), JobApplication.Status.INTERVIEW)
                    ));
        } else if (role == User.Role.EMPLOYER) {
            // Lấy danh sách ứng viên đã ứng tuyển vào job của công ty employer
            cq.select(root.get("userId"))
                    .distinct(true)
                    //.where(cb.equal(root.get("jobId").get("companyId").get("userId").get("id"), userId));

                    .where(cb.and(
                            cb.equal(root.get("jobId").get("companyId").get("userId").get("id"), userId),
                            cb.equal(root.get("status"), JobApplication.Status.INTERVIEW)
                    ));
        }

        TypedQuery<User> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<User> getUsersByActiveStatus(boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);

        // Lọc user có active = false
        cq.select(root).where(cb.equal(root.get("active"), isActive));

        TypedQuery<User> query = session.createQuery(cq);
        return query.getResultList();
    }

}
