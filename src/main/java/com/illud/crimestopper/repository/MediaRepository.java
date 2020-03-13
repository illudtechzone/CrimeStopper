package com.illud.crimestopper.repository;

import com.illud.crimestopper.domain.Media;
import com.illud.crimestopper.service.dto.MediaDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Media entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

	Page<Media> findAllMediaByComplaintId(Long complaintId, Pageable pageable);

	
}
