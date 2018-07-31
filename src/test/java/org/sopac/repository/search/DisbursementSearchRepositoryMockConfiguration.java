package org.sopac.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of DisbursementSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DisbursementSearchRepositoryMockConfiguration {

    @MockBean
    private DisbursementSearchRepository mockDisbursementSearchRepository;

}
