package org.sopac.repository;

import org.sopac.domain.DetailedSector;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DetailedSector entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetailedSectorRepository extends JpaRepository<DetailedSector, Long> {

}
