package com.illud.crimestopper.domain;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Complaint.
 */
@Entity
@Table(name = "complaint")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "complaint")
public class Complaint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "user_idp_code")
    private String userIdpCode;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "created_on")
    private Instant createdOn;

    @OneToMany(mappedBy = "complaint")
    private Set<Media> media = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "complaint_authority",
               joinColumns = @JoinColumn(name = "complaint_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private Set<Authority> authorities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Complaint description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserIdpCode() {
        return userIdpCode;
    }

    public Complaint userIdpCode(String userIdpCode) {
        this.userIdpCode = userIdpCode;
        return this;
    }

    public void setUserIdpCode(String userIdpCode) {
        this.userIdpCode = userIdpCode;
    }

    public String getAddress() {
        return address;
    }

    public Complaint address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public Complaint latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public Complaint longitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Complaint createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Set<Media> getMedia() {
        return media;
    }

    public Complaint media(Set<Media> media) {
        this.media = media;
        return this;
    }

    public Complaint addMedia(Media media) {
        this.media.add(media);
        media.setComplaint(this);
        return this;
    }

    public Complaint removeMedia(Media media) {
        this.media.remove(media);
        media.setComplaint(null);
        return this;
    }

    public void setMedia(Set<Media> media) {
        this.media = media;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public Complaint authorities(Set<Authority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public Complaint addAuthority(Authority authority) {
        this.authorities.add(authority);
        authority.getComplaints().add(this);
        return this;
    }

    public Complaint removeAuthority(Authority authority) {
        this.authorities.remove(authority);
        authority.getComplaints().remove(this);
        return this;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Complaint)) {
            return false;
        }
        return id != null && id.equals(((Complaint) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Complaint{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", userIdpCode='" + getUserIdpCode() + "'" +
            ", address='" + getAddress() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            "}";
    }
}
