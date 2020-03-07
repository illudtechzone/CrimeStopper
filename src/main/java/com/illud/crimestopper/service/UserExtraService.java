package com.illud.crimestopper.service;

import com.illud.crimestopper.service.dto.UserExtraDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.illud.crimestopper.domain.UserExtra}.
 */
public interface UserExtraService {

    /**
     * Save a userExtra.
     *
     * @param userExtraDTO the entity to save.
     * @return the persisted entity.
     */
    UserExtraDTO save(UserExtraDTO userExtraDTO);

    /**
     * Get all the userExtras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserExtraDTO> findAll(Pageable pageable);


    /**
     * Get the "id" userExtra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserExtraDTO> findOne(Long id);

    /**
     * Delete the "id" userExtra.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the userExtra corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserExtraDTO> search(String query, Pageable pageable);
}
