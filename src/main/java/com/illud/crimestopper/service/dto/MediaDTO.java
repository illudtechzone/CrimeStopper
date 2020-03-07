package com.illud.crimestopper.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.illud.crimestopper.domain.Media} entity.
 */
public class MediaDTO implements Serializable {

    private Long id;

    private String fileLink;

    @Lob
    private byte[] file;

    private String fileContentType;

    private Long complaintId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MediaDTO mediaDTO = (MediaDTO) o;
        if (mediaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mediaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MediaDTO{" +
            "id=" + getId() +
            ", fileLink='" + getFileLink() + "'" +
            ", file='" + getFile() + "'" +
            ", complaintId=" + getComplaintId() +
            "}";
    }
}
