package com.illud.crimestopper.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Media.
 */
@Entity
@Table(name = "media")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "media")
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "file_link")
    private String fileLink;

    @Lob
    @Column(name = "file")
    private byte[] file;

    @Column(name = "file_content_type")
    private String fileContentType;

    @ManyToOne
    @JsonIgnoreProperties("media")
    private Complaint complaint;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileLink() {
        return fileLink;
    }

    public Media fileLink(String fileLink) {
        this.fileLink = fileLink;
        return this;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public byte[] getFile() {
        return file;
    }

    public Media file(byte[] file) {
        this.file = file;
        return this;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public Media fileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public Media complaint(Complaint complaint) {
        this.complaint = complaint;
        return this;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Media)) {
            return false;
        }
        return id != null && id.equals(((Media) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Media{" +
            "id=" + getId() +
            ", fileLink='" + getFileLink() + "'" +
            ", file='" + getFile() + "'" +
            ", fileContentType='" + getFileContentType() + "'" +
            "}";
    }
}
