package org.sopac.repository;

import org.sopac.domain.Integration;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Integration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntegrationRepository extends JpaRepository<Integration, Long> {

}
