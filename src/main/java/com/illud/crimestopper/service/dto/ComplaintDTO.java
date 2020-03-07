package com.illud.crimestopper.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link com.illud.crimestopper.domain.Complaint} entity.
 */
public class ComplaintDTO implements Serializable {

    private Long id;

    private String description;

    private String userIdpCode;

    private String address;

    private String latitude;

    private String longitude;

    private Instant createdOn;


    private Set<AuthorityDTO> authorities = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserIdpCode() {
        return userIdpCode;
    }

    public void setUserIdpCode(String userIdpCode) {
        this.userIdpCode = userIdpCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Set<AuthorityDTO> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<AuthorityDTO> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ComplaintDTO complaintDTO = (ComplaintDTO) o;
        if (complaintDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), complaintDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ComplaintDTO{" +
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
