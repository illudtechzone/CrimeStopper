package com.illud.crimestopper.repository.search;

import com.illud.crimestopper.domain.Complaint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Complaint} entity.
 */
public interface ComplaintSearchRepository extends ElasticsearchRepository<Complaint, Long> {
}
