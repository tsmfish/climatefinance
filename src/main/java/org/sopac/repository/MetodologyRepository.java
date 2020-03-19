package org.sopac.repository;

import org.sopac.domain.Metodology;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Metodology entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetodologyRepository extends JpaRepository<Metodology, Long> {

}
