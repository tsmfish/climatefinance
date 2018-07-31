package org.sopac.repository;

import org.sopac.domain.DetailedSector;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DetailedSector entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetailedSectorRepository extends JpaRepository<DetailedSector, Long> {

}
