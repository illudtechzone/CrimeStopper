package com.illud.crimestopper.repository;

import com.illud.crimestopper.domain.Authority;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Authority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	
	public Optional<Authority> findById(String id);

}
