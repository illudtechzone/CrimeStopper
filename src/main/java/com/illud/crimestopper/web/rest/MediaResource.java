package com.illud.crimestopper.web.rest;

import com.illud.crimestopper.service.MediaService;
import com.illud.crimestopper.web.rest.errors.BadRequestAlertException;
import com.illud.crimestopper.service.dto.MediaDTO;

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
 * REST controller for managing {@link com.illud.crimestopper.domain.Media}.
 */
@RestController
@RequestMapping("/api")
public class MediaResource {

    private final Logger log = LoggerFactory.getLogger(MediaResource.class);

    private static final String ENTITY_NAME = "crimeStopperMedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MediaService mediaService;

    public MediaResource(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    /**
     * {@code POST  /media} : Create a new media.
     *
     * @param mediaDTO the mediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mediaDTO, or with status {@code 400 (Bad Request)} if the media has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/media")
    public ResponseEntity<MediaDTO> createMedia(@RequestBody MediaDTO mediaDTO) throws URISyntaxException {
        log.debug("REST request to save Media : {}", mediaDTO);
        if (mediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new media cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MediaDTO result = mediaService.save(mediaDTO);
        return ResponseEntity.created(new URI("/api/media/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /media} : Updates an existing media.
     *
     * @param mediaDTO the mediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mediaDTO,
     * or with status {@code 400 (Bad Request)} if the mediaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/media")
    public ResponseEntity<MediaDTO> updateMedia(@RequestBody MediaDTO mediaDTO) throws URISyntaxException {
        log.debug("REST request to update Media : {}", mediaDTO);
        if (mediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MediaDTO result = mediaService.save(mediaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mediaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /media} : get all the media.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of media in body.
     */
    @GetMapping("/media")
    public ResponseEntity<List<MediaDTO>> getAllMedia(Pageable pageable) {
        log.debug("REST request to get a page of Media");
        Page<MediaDTO> page = mediaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /media/:id} : get the "id" media.
     *
     * @param id the id of the mediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mediaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/media/{id}")
    public ResponseEntity<MediaDTO> getMedia(@PathVariable Long id) {
        log.debug("REST request to get Media : {}", id);
        Optional<MediaDTO> mediaDTO = mediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mediaDTO);
    }

    /**
     * {@code DELETE  /media/:id} : delete the "id" media.
     *
     * @param id the id of the mediaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/media/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        log.debug("REST request to delete Media : {}", id);
        mediaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/media?query=:query} : search for the media corresponding
     * to the query.
     *
     * @param query the query of the media search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/media")
    public ResponseEntity<List<MediaDTO>> searchMedia(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Media for query {}", query);
        Page<MediaDTO> page = mediaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    @GetMapping("/findAllMediaByComplaintId/{complaintId}")
    public ResponseEntity<List<MediaDTO>> findAllMediaByComplaintId(@PathVariable Long complaintId,Pageable pageable)
    {
    	Page<MediaDTO> page = mediaService.findAllMediaByComplaintId(complaintId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());

    }
}
