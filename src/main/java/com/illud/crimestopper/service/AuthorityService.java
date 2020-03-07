package com.illud.crimestopper.service;

import com.illud.crimestopper.service.dto.AuthorityDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.illud.crimestopper.domain.Authority}.
 */
public interface AuthorityService {

    /**
     * Save a authority.
     *
     * @param authorityDTO the entity to save.
     * @return the persisted entity.
     */
    AuthorityDTO save(AuthorityDTO authorityDTO);

    /**
     * Get all the authorities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AuthorityDTO> findAll(Pageable pageable);


    /**
     * Get the "id" authority.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AuthorityDTO> findOne(Long id);

    /**
     * Delete the "id" authority.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the authority corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AuthorityDTO> search(String query, Pageable pageable);
}
