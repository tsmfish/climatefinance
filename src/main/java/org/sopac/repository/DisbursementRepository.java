package org.sopac.repository;

import org.sopac.domain.Disbursement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Disbursement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisbursementRepository extends JpaRepository<Disbursement, Long> {

}
