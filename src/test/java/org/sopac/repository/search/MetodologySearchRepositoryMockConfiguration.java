package org.sopac.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of MetodologySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class MetodologySearchRepositoryMockConfiguration {

    @MockBean
    private MetodologySearchRepository mockMetodologySearchRepository;

}
