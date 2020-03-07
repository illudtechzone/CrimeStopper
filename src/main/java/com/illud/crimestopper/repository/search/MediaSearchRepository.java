package com.illud.crimestopper.repository.search;

import com.illud.crimestopper.domain.Media;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Media} entity.
 */
public interface MediaSearchRepository extends ElasticsearchRepository<Media, Long> {
}
