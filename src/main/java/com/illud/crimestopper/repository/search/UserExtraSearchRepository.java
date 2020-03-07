package com.illud.crimestopper.repository.search;

import com.illud.crimestopper.domain.UserExtra;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link UserExtra} entity.
 */
public interface UserExtraSearchRepository extends ElasticsearchRepository<UserExtra, Long> {
}
