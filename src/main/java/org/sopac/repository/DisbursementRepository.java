package org.sopac.repository;

import org.sopac.domain.Disbursement;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Disbursement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisbursementRepository extends JpaRepository<Disbursement, Long> {

}
