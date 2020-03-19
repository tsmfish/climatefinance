package org.sopac.repository.search;

import org.sopac.domain.Metodology;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Metodology entity.
 */
public interface MetodologySearchRepository extends ElasticsearchRepository<Metodology, Long> {
}
