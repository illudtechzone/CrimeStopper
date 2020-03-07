package com.illud.crimestopper.repository.search;

import com.illud.crimestopper.domain.Authority;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Authority} entity.
 */
public interface AuthoritySearchRepository extends ElasticsearchRepository<Authority, Long> {
}
