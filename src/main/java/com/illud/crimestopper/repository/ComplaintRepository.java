package com.illud.crimestopper.repository;

import com.illud.crimestopper.domain.Complaint;
import com.illud.crimestopper.service.dto.ComplaintDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Complaint entity.
 */
@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    @Query(value = "select distinct complaint from Complaint complaint left join fetch complaint.authorities",
        countQuery = "select count(distinct complaint) from Complaint complaint")
    Page<Complaint> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct complaint from Complaint complaint left join fetch complaint.authorities")
    List<Complaint> findAllWithEagerRelationships();

    @Query("select complaint from Complaint complaint left join fetch complaint.authorities where complaint.id =:id")
    Optional<Complaint> findOneWithEagerRelationships(@Param("id") Long id);

	Page<Complaint> findByAuthorities_id(Long id,Pageable pageable);

	Page<Complaint> findComplaintByUserIdpCode(String userIdpCode, Pageable pageable);

}
