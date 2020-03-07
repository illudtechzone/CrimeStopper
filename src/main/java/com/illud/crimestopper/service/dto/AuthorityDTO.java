package com.illud.crimestopper.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.illud.crimestopper.domain.Authority} entity.
 */
public class AuthorityDTO implements Serializable {

    private Long id;

    private String name;

    private String authorityIdpCode;

    private String phone;

    private String address;

    private String website;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorityIdpCode() {
        return authorityIdpCode;
    }

    public void setAuthorityIdpCode(String authorityIdpCode) {
        this.authorityIdpCode = authorityIdpCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuthorityDTO authorityDTO = (AuthorityDTO) o;
        if (authorityDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), authorityDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AuthorityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", authorityIdpCode='" + getAuthorityIdpCode() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", website='" + getWebsite() + "'" +
            "}";
    }
}
