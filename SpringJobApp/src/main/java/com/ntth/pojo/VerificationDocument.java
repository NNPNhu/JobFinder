/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author LOQ
 */
@Entity
@Table(name = "verification_document")
@NamedQueries({
    @NamedQuery(name = "VerificationDocument.findAll", query = "SELECT v FROM VerificationDocument v"),
    @NamedQuery(name = "VerificationDocument.findById", query = "SELECT v FROM VerificationDocument v WHERE v.id = :id"),
    @NamedQuery(name = "VerificationDocument.findByDocumentType", query = "SELECT v FROM VerificationDocument v WHERE v.documentType = :documentType"),
    @NamedQuery(name = "VerificationDocument.findByDocumentUrl", query = "SELECT v FROM VerificationDocument v WHERE v.documentUrl = :documentUrl"),
    @NamedQuery(name = "VerificationDocument.findByStatus", query = "SELECT v FROM VerificationDocument v WHERE v.status = :status"),
    @NamedQuery(name = "VerificationDocument.findByUploadedAt", query = "SELECT v FROM VerificationDocument v WHERE v.uploadedAt = :uploadedAt")})
public class VerificationDocument implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 100)
    @Column(name = "document_type")
    private String documentType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "document_url")
    private String documentUrl;
    @Size(max = 8)
    @Column(name = "status")
    private String status;
    @Column(name = "uploaded_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadedAt;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;

    public VerificationDocument() {
    }

    public VerificationDocument(Integer id) {
        this.id = id;
    }

    public VerificationDocument(Integer id, String documentUrl) {
        this.id = id;
        this.documentUrl = documentUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VerificationDocument)) {
            return false;
        }
        VerificationDocument other = (VerificationDocument) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ntth.pojo.VerificationDocument[ id=" + id + " ]";
    }
    
}
