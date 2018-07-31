package org.sopac.repository.search;

import org.sopac.domain.Disbursement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Disbursement entity.
 */
public interface DisbursementSearchRepository extends ElasticsearchRepository<Disbursement, Long> {
}
