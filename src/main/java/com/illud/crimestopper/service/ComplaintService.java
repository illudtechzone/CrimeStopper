package com.illud.crimestopper.service;

import com.illud.crimestopper.service.dto.ComplaintDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.List;
/**
 * Service Interface for managing {@link com.illud.crimestopper.domain.Complaint}.
 */
public interface ComplaintService {

    /**
     * Save a complaint.
     *
     * @param complaintDTO the entity to save.
     * @return the persisted entity.
     */
    ComplaintDTO save(ComplaintDTO complaintDTO);

    /**
     * Get all the complaints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ComplaintDTO> findAll(Pageable pageable);

    /**
     * Get all the complaints with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<ComplaintDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" complaint.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ComplaintDTO> findOne(Long id);

    /**
     * Delete the "id" complaint.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the complaint corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ComplaintDTO> search(String query, Pageable pageable);

	Page<ComplaintDTO> findComplaintByAuthorityId(Long authorityId,Pageable pageable);
}
