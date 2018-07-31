package org.sopac.repository.search;

import org.sopac.domain.DetailedSector;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DetailedSector entity.
 */
public interface DetailedSectorSearchRepository extends ElasticsearchRepository<DetailedSector, Long> {
}
