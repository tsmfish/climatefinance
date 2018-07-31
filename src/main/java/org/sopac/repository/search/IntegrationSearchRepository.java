package org.sopac.repository.search;

import org.sopac.domain.Integration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Integration entity.
 */
public interface IntegrationSearchRepository extends ElasticsearchRepository<Integration, Long> {
}
