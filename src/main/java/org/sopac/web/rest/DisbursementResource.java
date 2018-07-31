package org.sopac.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.sopac.domain.Disbursement;

import org.sopac.repository.DisbursementRepository;
import org.sopac.repository.search.DisbursementSearchRepository;
import org.sopac.web.rest.errors.BadRequestAlertException;
import org.sopac.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Disbursement.
 */
@RestController
@RequestMapping("/api")
public class DisbursementResource {

    private final Logger log = LoggerFactory.getLogger(DisbursementResource.class);

    private static final String ENTITY_NAME = "disbursement";

    private final DisbursementRepository disbursementRepository;

    private final DisbursementSearchRepository disbursementSearchRepository;

    public DisbursementResource(DisbursementRepository disbursementRepository, DisbursementSearchRepository disbursementSearchRepository) {
        this.disbursementRepository = disbursementRepository;
        this.disbursementSearchRepository = disbursementSearchRepository;
    }

    /**
     * POST  /disbursements : Create a new disbursement.
     *
     * @param disbursement the disbursement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new disbursement, or with status 400 (Bad Request) if the disbursement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/disbursements")
    @Timed
    public ResponseEntity<Disbursement> createDisbursement(@RequestBody Disbursement disbursement) throws URISyntaxException {
        log.debug("REST request to save Disbursement : {}", disbursement);
        if (disbursement.getId() != null) {
            throw new BadRequestAlertException("A new disbursement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Disbursement result = disbursementRepository.save(disbursement);
        disbursementSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/disbursements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /disbursements : Updates an existing disbursement.
     *
     * @param disbursement the disbursement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated disbursement,
     * or with status 400 (Bad Request) if the disbursement is not valid,
     * or with status 500 (Internal Server Error) if the disbursement couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/disbursements")
    @Timed
    public ResponseEntity<Disbursement> updateDisbursement(@RequestBody Disbursement disbursement) throws URISyntaxException {
        log.debug("REST request to update Disbursement : {}", disbursement);
        if (disbursement.getId() == null) {
            return createDisbursement(disbursement);
        }
        Disbursement result = disbursementRepository.save(disbursement);
        disbursementSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, disbursement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /disbursements : get all the disbursements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of disbursements in body
     */
    @GetMapping("/disbursements")
    @Timed
    public List<Disbursement> getAllDisbursements() {
        log.debug("REST request to get all Disbursements");
        return disbursementRepository.findAll();
        }

    /**
     * GET  /disbursements/:id : get the "id" disbursement.
     *
     * @param id the id of the disbursement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the disbursement, or with status 404 (Not Found)
     */
    @GetMapping("/disbursements/{id}")
    @Timed
    public ResponseEntity<Disbursement> getDisbursement(@PathVariable Long id) {
        log.debug("REST request to get Disbursement : {}", id);
        Disbursement disbursement = disbursementRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(disbursement));
    }

    /**
     * DELETE  /disbursements/:id : delete the "id" disbursement.
     *
     * @param id the id of the disbursement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/disbursements/{id}")
    @Timed
    public ResponseEntity<Void> deleteDisbursement(@PathVariable Long id) {
        log.debug("REST request to delete Disbursement : {}", id);
        disbursementRepository.delete(id);
        disbursementSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/disbursements?query=:query : search for the disbursement corresponding
     * to the query.
     *
     * @param query the query of the disbursement search
     * @return the result of the search
     */
    @GetMapping("/_search/disbursements")
    @Timed
    public List<Disbursement> searchDisbursements(@RequestParam String query) {
        log.debug("REST request to search Disbursements for query {}", query);
        return StreamSupport
            .stream(disbursementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
