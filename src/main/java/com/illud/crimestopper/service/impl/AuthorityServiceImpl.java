package com.illud.crimestopper.service.impl;

import com.illud.crimestopper.service.AuthorityService;
import com.illud.crimestopper.domain.Authority;
import com.illud.crimestopper.repository.AuthorityRepository;
import com.illud.crimestopper.repository.search.AuthoritySearchRepository;
import com.illud.crimestopper.service.dto.AuthorityDTO;
import com.illud.crimestopper.service.mapper.AuthorityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Authority}.
 */
@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {

    private final Logger log = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    private final AuthorityRepository authorityRepository;

    private final AuthorityMapper authorityMapper;

    private final AuthoritySearchRepository authoritySearchRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository, AuthorityMapper authorityMapper, AuthoritySearchRepository authoritySearchRepository) {
        this.authorityRepository = authorityRepository;
        this.authorityMapper = authorityMapper;
        this.authoritySearchRepository = authoritySearchRepository;
    }

    /**
     * Save a authority.
     *
     * @param authorityDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AuthorityDTO save(AuthorityDTO authorityDTO) {
        log.debug("Request to save Authority : {}", authorityDTO);
        Authority authority = authorityMapper.toEntity(authorityDTO);
        authority = authorityRepository.save(authority);
        AuthorityDTO result = authorityMapper.toDto(authority);
        authoritySearchRepository.save(authority);
        return result;
    }

    /**
     * Get all the authorities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AuthorityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Authorities");
        return authorityRepository.findAll(pageable)
            .map(authorityMapper::toDto);
    }


    /**
     * Get one authority by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AuthorityDTO> findOne(Long id) {
        log.debug("Request to get Authority : {}", id);
        return authorityRepository.findById(id)
            .map(authorityMapper::toDto);
    }

    /**
     * Delete the authority by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Authority : {}", id);
        authorityRepository.deleteById(id);
        authoritySearchRepository.deleteById(id);
    }

    /**
     * Search for the authority corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AuthorityDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Authorities for query {}", query);
        return authoritySearchRepository.search(queryStringQuery(query), pageable)
            .map(authorityMapper::toDto);
    }
}
