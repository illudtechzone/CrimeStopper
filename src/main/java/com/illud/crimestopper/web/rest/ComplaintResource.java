package com.illud.crimestopper.web.rest;

import com.illud.crimestopper.service.ComplaintService;
import com.illud.crimestopper.web.rest.errors.BadRequestAlertException;
import com.illud.crimestopper.service.dto.ComplaintDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.illud.crimestopper.domain.Complaint}.
 */
@RestController
@RequestMapping("/api")
public class ComplaintResource {

    private final Logger log = LoggerFactory.getLogger(ComplaintResource.class);

    private static final String ENTITY_NAME = "crimeStopperComplaint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComplaintService complaintService;

    public ComplaintResource(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    /**
     * {@code POST  /complaints} : Create a new complaint.
     *
     * @param complaintDTO the complaintDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new complaintDTO, or with status {@code 400 (Bad Request)} if the complaint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/complaints")
    public ResponseEntity<ComplaintDTO> createComplaint(@RequestBody ComplaintDTO complaintDTO) throws URISyntaxException {
        log.debug("REST request to save Complaint : {}", complaintDTO);
        if (complaintDTO.getId() != null) {
            throw new BadRequestAlertException("A new complaint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComplaintDTO result = complaintService.save(complaintDTO);
        return ResponseEntity.created(new URI("/api/complaints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /complaints} : Updates an existing complaint.
     *
     * @param complaintDTO the complaintDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated complaintDTO,
     * or with status {@code 400 (Bad Request)} if the complaintDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the complaintDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/complaints")
    public ResponseEntity<ComplaintDTO> updateComplaint(@RequestBody ComplaintDTO complaintDTO) throws URISyntaxException {
        log.debug("REST request to update Complaint : {}", complaintDTO);
        if (complaintDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ComplaintDTO result = complaintService.save(complaintDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, complaintDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /complaints} : get all the complaints.
     *

     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of complaints in body.
     */
    @GetMapping("/complaints")
    public ResponseEntity<List<ComplaintDTO>> getAllComplaints(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Complaints");
        Page<ComplaintDTO> page;
        if (eagerload) {
            page = complaintService.findAllWithEagerRelationships(pageable);
        } else {
            page = complaintService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /complaints/:id} : get the "id" complaint.
     *
     * @param id the id of the complaintDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the complaintDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/complaints/{id}")
    public ResponseEntity<ComplaintDTO> getComplaint(@PathVariable Long id) {
        log.debug("REST request to get Complaint : {}", id);
        Optional<ComplaintDTO> complaintDTO = complaintService.findOne(id);
        return ResponseUtil.wrapOrNotFound(complaintDTO);
    }

    /**
     * {@code DELETE  /complaints/:id} : delete the "id" complaint.
     *
     * @param id the id of the complaintDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/complaints/{id}")
    public ResponseEntity<Void> deleteComplaint(@PathVariable Long id) {
        log.debug("REST request to delete Complaint : {}", id);
        complaintService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/complaints?query=:query} : search for the complaint corresponding
     * to the query.
     *
     * @param query the query of the complaint search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/complaints")
    public ResponseEntity<List<ComplaintDTO>> searchComplaints(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Complaints for query {}", query);
        Page<ComplaintDTO> page = complaintService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    @GetMapping("/findComplaintByAuthorityId/{authorityId}")
    public ResponseEntity<List<ComplaintDTO>> findComplaintByAuthorityId(@PathVariable Long authorityId,Pageable pageable)
    {
    	
		 Page<ComplaintDTO> page = complaintService.findComplaintByAuthorityId(authorityId,pageable);
    	 HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

    	return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
