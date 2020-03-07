package com.illud.crimestopper.web.rest;

import com.illud.crimestopper.service.AuthorityService;
import com.illud.crimestopper.web.rest.errors.BadRequestAlertException;
import com.illud.crimestopper.service.dto.AuthorityDTO;

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
 * REST controller for managing {@link com.illud.crimestopper.domain.Authority}.
 */
@RestController
@RequestMapping("/api")
public class AuthorityResource {

    private final Logger log = LoggerFactory.getLogger(AuthorityResource.class);

    private static final String ENTITY_NAME = "crimeStopperAuthority";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthorityService authorityService;

    public AuthorityResource(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * {@code POST  /authorities} : Create a new authority.
     *
     * @param authorityDTO the authorityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new authorityDTO, or with status {@code 400 (Bad Request)} if the authority has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/authorities")
    public ResponseEntity<AuthorityDTO> createAuthority(@RequestBody AuthorityDTO authorityDTO) throws URISyntaxException {
        log.debug("REST request to save Authority : {}", authorityDTO);
        if (authorityDTO.getId() != null) {
            throw new BadRequestAlertException("A new authority cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuthorityDTO result = authorityService.save(authorityDTO);
        return ResponseEntity.created(new URI("/api/authorities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /authorities} : Updates an existing authority.
     *
     * @param authorityDTO the authorityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorityDTO,
     * or with status {@code 400 (Bad Request)} if the authorityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authorityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/authorities")
    public ResponseEntity<AuthorityDTO> updateAuthority(@RequestBody AuthorityDTO authorityDTO) throws URISyntaxException {
        log.debug("REST request to update Authority : {}", authorityDTO);
        if (authorityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AuthorityDTO result = authorityService.save(authorityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, authorityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /authorities} : get all the authorities.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authorities in body.
     */
    @GetMapping("/authorities")
    public ResponseEntity<List<AuthorityDTO>> getAllAuthorities(Pageable pageable) {
        log.debug("REST request to get a page of Authorities");
        Page<AuthorityDTO> page = authorityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /authorities/:id} : get the "id" authority.
     *
     * @param id the id of the authorityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the authorityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/authorities/{id}")
    public ResponseEntity<AuthorityDTO> getAuthority(@PathVariable Long id) {
        log.debug("REST request to get Authority : {}", id);
        Optional<AuthorityDTO> authorityDTO = authorityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(authorityDTO);
    }

    /**
     * {@code DELETE  /authorities/:id} : delete the "id" authority.
     *
     * @param id the id of the authorityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/authorities/{id}")
    public ResponseEntity<Void> deleteAuthority(@PathVariable Long id) {
        log.debug("REST request to delete Authority : {}", id);
        authorityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/authorities?query=:query} : search for the authority corresponding
     * to the query.
     *
     * @param query the query of the authority search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/authorities")
    public ResponseEntity<List<AuthorityDTO>> searchAuthorities(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Authorities for query {}", query);
        Page<AuthorityDTO> page = authorityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
