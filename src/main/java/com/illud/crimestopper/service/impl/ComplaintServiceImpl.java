package com.illud.crimestopper.service.impl;

import com.illud.crimestopper.service.ComplaintService;
import com.illud.crimestopper.domain.Complaint;
import com.illud.crimestopper.repository.ComplaintRepository;
import com.illud.crimestopper.repository.search.ComplaintSearchRepository;
import com.illud.crimestopper.service.dto.ComplaintDTO;
import com.illud.crimestopper.service.mapper.ComplaintMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;
import java.util.List;
/**
 * Service Implementation for managing {@link Complaint}.
 */
@Service
@Transactional
public class ComplaintServiceImpl implements ComplaintService {

    private final Logger log = LoggerFactory.getLogger(ComplaintServiceImpl.class);

    private final ComplaintRepository complaintRepository;

    private final ComplaintMapper complaintMapper;

    private final ComplaintSearchRepository complaintSearchRepository;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository, ComplaintMapper complaintMapper, ComplaintSearchRepository complaintSearchRepository) {
        this.complaintRepository = complaintRepository;
        this.complaintMapper = complaintMapper;
        this.complaintSearchRepository = complaintSearchRepository;
    }

    /**
     * Save a complaint.
     *
     * @param complaintDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ComplaintDTO save(ComplaintDTO complaintDTO) {
        log.debug("Request to save Complaint : {}", complaintDTO);
        Complaint complaint = complaintMapper.toEntity(complaintDTO);
        complaint = complaintRepository.save(complaint);
        ComplaintDTO result = complaintMapper.toDto(complaint);
        complaintSearchRepository.save(complaint);
        return result;
    }

    /**
     * Get all the complaints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ComplaintDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Complaints");
        return complaintRepository.findAll(pageable)
            .map(complaintMapper::toDto);
    }

    /**
     * Get all the complaints with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ComplaintDTO> findAllWithEagerRelationships(Pageable pageable) {
        return complaintRepository.findAllWithEagerRelationships(pageable).map(complaintMapper::toDto);
    }
    

    /**
     * Get one complaint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ComplaintDTO> findOne(Long id) {
        log.debug("Request to get Complaint : {}", id);
        return complaintRepository.findOneWithEagerRelationships(id)
            .map(complaintMapper::toDto);
    }

    /**
     * Delete the complaint by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Complaint : {}", id);
        complaintRepository.deleteById(id);
        complaintSearchRepository.deleteById(id);
    }

    /**
     * Search for the complaint corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ComplaintDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Complaints for query {}", query);
        return complaintSearchRepository.search(queryStringQuery(query), pageable)
            .map(complaintMapper::toDto);
    }
    
    public Page<ComplaintDTO> findComplaintByAuthorityId(Long authorityId,Pageable pageable)
    {
		
    	/*log.debug("//////////////////////", complaintRepository.findByAuthorities_id(authorityId));*/
    	return complaintRepository.findByAuthorities_id(authorityId,pageable)
    			.map(complaintMapper::toDto);
    }
}
