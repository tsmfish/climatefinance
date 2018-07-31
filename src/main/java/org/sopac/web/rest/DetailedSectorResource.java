package org.sopac.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.sopac.domain.DetailedSector;

import org.sopac.repository.DetailedSectorRepository;
import org.sopac.repository.search.DetailedSectorSearchRepository;
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
 * REST controller for managing DetailedSector.
 */
@RestController
@RequestMapping("/api")
public class DetailedSectorResource {

    private final Logger log = LoggerFactory.getLogger(DetailedSectorResource.class);

    private static final String ENTITY_NAME = "detailedSector";

    private final DetailedSectorRepository detailedSectorRepository;

    private final DetailedSectorSearchRepository detailedSectorSearchRepository;

    public DetailedSectorResource(DetailedSectorRepository detailedSectorRepository, DetailedSectorSearchRepository detailedSectorSearchRepository) {
        this.detailedSectorRepository = detailedSectorRepository;
        this.detailedSectorSearchRepository = detailedSectorSearchRepository;
    }

    /**
     * POST  /detailed-sectors : Create a new detailedSector.
     *
     * @param detailedSector the detailedSector to create
     * @return the ResponseEntity with status 201 (Created) and with body the new detailedSector, or with status 400 (Bad Request) if the detailedSector has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/detailed-sectors")
    @Timed
    public ResponseEntity<DetailedSector> createDetailedSector(@RequestBody DetailedSector detailedSector) throws URISyntaxException {
        log.debug("REST request to save DetailedSector : {}", detailedSector);
        if (detailedSector.getId() != null) {
            throw new BadRequestAlertException("A new detailedSector cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetailedSector result = detailedSectorRepository.save(detailedSector);
        detailedSectorSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/detailed-sectors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /detailed-sectors : Updates an existing detailedSector.
     *
     * @param detailedSector the detailedSector to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated detailedSector,
     * or with status 400 (Bad Request) if the detailedSector is not valid,
     * or with status 500 (Internal Server Error) if the detailedSector couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/detailed-sectors")
    @Timed
    public ResponseEntity<DetailedSector> updateDetailedSector(@RequestBody DetailedSector detailedSector) throws URISyntaxException {
        log.debug("REST request to update DetailedSector : {}", detailedSector);
        if (detailedSector.getId() == null) {
            return createDetailedSector(detailedSector);
        }
        DetailedSector result = detailedSectorRepository.save(detailedSector);
        detailedSectorSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, detailedSector.getId().toString()))
            .body(result);
    }

    /**
     * GET  /detailed-sectors : get all the detailedSectors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of detailedSectors in body
     */
    @GetMapping("/detailed-sectors")
    @Timed
    public List<DetailedSector> getAllDetailedSectors() {
        log.debug("REST request to get all DetailedSectors");
        return detailedSectorRepository.findAll();
        }

    /**
     * GET  /detailed-sectors/:id : get the "id" detailedSector.
     *
     * @param id the id of the detailedSector to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the detailedSector, or with status 404 (Not Found)
     */
    @GetMapping("/detailed-sectors/{id}")
    @Timed
    public ResponseEntity<DetailedSector> getDetailedSector(@PathVariable Long id) {
        log.debug("REST request to get DetailedSector : {}", id);
        DetailedSector detailedSector = detailedSectorRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(detailedSector));
    }

    /**
     * DELETE  /detailed-sectors/:id : delete the "id" detailedSector.
     *
     * @param id the id of the detailedSector to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/detailed-sectors/{id}")
    @Timed
    public ResponseEntity<Void> deleteDetailedSector(@PathVariable Long id) {
        log.debug("REST request to delete DetailedSector : {}", id);
        detailedSectorRepository.delete(id);
        detailedSectorSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/detailed-sectors?query=:query : search for the detailedSector corresponding
     * to the query.
     *
     * @param query the query of the detailedSector search
     * @return the result of the search
     */
    @GetMapping("/_search/detailed-sectors")
    @Timed
    public List<DetailedSector> searchDetailedSectors(@RequestParam String query) {
        log.debug("REST request to search DetailedSectors for query {}", query);
        return StreamSupport
            .stream(detailedSectorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
