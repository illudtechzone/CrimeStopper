package com.illud.crimestopper.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link AuthoritySearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AuthoritySearchRepositoryMockConfiguration {

    @MockBean
    private AuthoritySearchRepository mockAuthoritySearchRepository;

}
