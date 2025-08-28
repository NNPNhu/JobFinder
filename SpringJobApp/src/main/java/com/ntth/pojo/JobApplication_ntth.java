///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.ntth.pojo;
//
//import jakarta.persistence.Basic;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.Lob;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.NamedQueries;
//import jakarta.persistence.NamedQuery;
//import jakarta.persistence.Table;
//import jakarta.persistence.Temporal;
//import jakarta.persistence.TemporalType;
//import jakarta.validation.constraints.Size;
//import java.io.Serializable;
//import java.util.Date;
//
///**
// *
// * @author LOQ
// */
//@Entity
//@Table(name = "job_application")
//@NamedQueries({
//    @NamedQuery(name = "JobApplication.findAll", query = "SELECT j FROM JobApplication j"),
//    @NamedQuery(name = "JobApplication.findById", query = "SELECT j FROM JobApplication j WHERE j.id = :id"),
//    @NamedQuery(name = "JobApplication.findByResumeLink", query = "SELECT j FROM JobApplication j WHERE j.resumeLink = :resumeLink"),
//    @NamedQuery(name = "JobApplication.findByApplicationDate", query = "SELECT j FROM JobApplication j WHERE j.applicationDate = :applicationDate"),
//    @NamedQuery(name = "JobApplication.findByStatus", query = "SELECT j FROM JobApplication j WHERE j.status = :status")})
//public class JobApplication implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
//    @Column(name = "id")
//    private Integer id;
//    @Lob
//    @Size(max = 65535)
//    @Column(name = "cover_letter")
//    private String coverLetter;
//    @Size(max = 255)
//    @Column(name = "resume_link")
//    private String resumeLink;
//    @Column(name = "application_date")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date applicationDate;
//    @Size(max = 9)
//    @Column(name = "status")
//    private String status;
//    @JoinColumn(name = "job_id", referencedColumnName = "id")
//    @ManyToOne(optional = false)
//    private Job jobId;
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    @ManyToOne(optional = false)
//    private User userId;
//
//    public JobApplication() {
//    }
//
//    public JobApplication(Integer id) {
//        this.id = id;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getCoverLetter() {
//        return coverLetter;
//    }
//
//    public void setCoverLetter(String coverLetter) {
//        this.coverLetter = coverLetter;
//    }
//
//    public String getResumeLink() {
//        return resumeLink;
//    }
//
//    public void setResumeLink(String resumeLink) {
//        this.resumeLink = resumeLink;
//    }
//
//    public Date getApplicationDate() {
//        return applicationDate;
//    }
//
//    public void setApplicationDate(Date applicationDate) {
//        this.applicationDate = applicationDate;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public Job getJobId() {
//        return jobId;
//    }
//
//    public void setJobId(Job jobId) {
//        this.jobId = jobId;
//    }
//
//    public User getUserId() {
//        return userId;
//    }
//
//    public void setUserId(User userId) {
//        this.userId = userId;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof JobApplication)) {
//            return false;
//        }
//        JobApplication other = (JobApplication) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "com.ntth.pojo.JobApplication[ id=" + id + " ]";
//    }
//    
//}
