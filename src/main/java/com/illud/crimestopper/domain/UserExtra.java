package com.illud.crimestopper.domain;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A UserExtra.
 */
@Entity
@Table(name = "user_extra")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "userextra")
public class UserExtra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "current_latitude")
    private String currentLatitude;

    @Column(name = "current_longitude")
    private String currentLongitude;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public UserExtra phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public UserExtra address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrentLatitude() {
        return currentLatitude;
    }

    public UserExtra currentLatitude(String currentLatitude) {
        this.currentLatitude = currentLatitude;
        return this;
    }

    public void setCurrentLatitude(String currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public String getCurrentLongitude() {
        return currentLongitude;
    }

    public UserExtra currentLongitude(String currentLongitude) {
        this.currentLongitude = currentLongitude;
        return this;
    }

    public void setCurrentLongitude(String currentLongitude) {
        this.currentLongitude = currentLongitude;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserExtra)) {
            return false;
        }
        return id != null && id.equals(((UserExtra) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserExtra{" +
            "id=" + getId() +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", currentLatitude='" + getCurrentLatitude() + "'" +
            ", currentLongitude='" + getCurrentLongitude() + "'" +
            "}";
    }
}
