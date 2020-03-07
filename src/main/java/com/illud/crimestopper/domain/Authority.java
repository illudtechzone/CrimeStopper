package com.illud.crimestopper.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Authority.
 */
@Entity
@Table(name = "authority")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "authority")
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "authority_idp_code")
    private String authorityIdpCode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "website")
    private String website;

    @ManyToMany(mappedBy = "authorities")
    @JsonIgnore
    private Set<Complaint> complaints = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Authority name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorityIdpCode() {
        return authorityIdpCode;
    }

    public Authority authorityIdpCode(String authorityIdpCode) {
        this.authorityIdpCode = authorityIdpCode;
        return this;
    }

    public void setAuthorityIdpCode(String authorityIdpCode) {
        this.authorityIdpCode = authorityIdpCode;
    }

    public String getPhone() {
        return phone;
    }

    public Authority phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public Authority address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public Authority website(String website) {
        this.website = website;
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Set<Complaint> getComplaints() {
        return complaints;
    }

    public Authority complaints(Set<Complaint> complaints) {
        this.complaints = complaints;
        return this;
    }

    public Authority addComplaint(Complaint complaint) {
        this.complaints.add(complaint);
        complaint.getAuthorities().add(this);
        return this;
    }

    public Authority removeComplaint(Complaint complaint) {
        this.complaints.remove(complaint);
        complaint.getAuthorities().remove(this);
        return this;
    }

    public void setComplaints(Set<Complaint> complaints) {
        this.complaints = complaints;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authority)) {
            return false;
        }
        return id != null && id.equals(((Authority) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Authority{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", authorityIdpCode='" + getAuthorityIdpCode() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", website='" + getWebsite() + "'" +
            "}";
    }
}
