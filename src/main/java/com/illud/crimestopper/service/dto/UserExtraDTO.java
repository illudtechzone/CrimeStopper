package com.illud.crimestopper.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.illud.crimestopper.domain.UserExtra} entity.
 */
public class UserExtraDTO implements Serializable {

    private Long id;

    private String phone;

    private String address;

    private String currentLatitude;

    private String currentLongitude;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(String currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public String getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(String currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserExtraDTO userExtraDTO = (UserExtraDTO) o;
        if (userExtraDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userExtraDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserExtraDTO{" +
            "id=" + getId() +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", currentLatitude='" + getCurrentLatitude() + "'" +
            ", currentLongitude='" + getCurrentLongitude() + "'" +
            "}";
    }
}
