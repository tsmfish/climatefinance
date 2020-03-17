package org.sopac.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.sopac.domain.Metodology;
import org.sopac.repository.MetodologyRepository;
import org.sopac.repository.search.MetodologySearchRepository;
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
 * REST controller for managing Metodology.
 */
@RestController
@RequestMapping("/api")
public class MetodologyResource {

    private final Logger log = LoggerFactory.getLogger(MetodologyResource.class);

    private static final String ENTITY_NAME = "metodology";

    private final MetodologyRepository metodologyRepository;

    private final MetodologySearchRepository metodologySearchRepository;

    public MetodologyResource(MetodologyRepository metodologyRepository, MetodologySearchRepository metodologySearchRepository) {
        this.metodologyRepository = metodologyRepository;
        this.metodologySearchRepository = metodologySearchRepository;
    }

    /**
     * POST  /metodologies : Create a new metodology.
     *
     * @param metodology the metodology to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metodology, or with status 400 (Bad Request) if the metodology has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/metodologies")
    @Timed
    public ResponseEntity<Metodology> createMetodology(@RequestBody Metodology metodology) throws URISyntaxException {
        log.debug("REST request to save Metodology : {}", metodology);
        if (metodology.getId() != null) {
            throw new BadRequestAlertException("A new metodology cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Metodology result = metodologyRepository.save(metodology);
        metodologySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/metodologies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /metodologies : Updates an existing metodology.
     *
     * @param metodology the metodology to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated metodology,
     * or with status 400 (Bad Request) if the metodology is not valid,
     * or with status 500 (Internal Server Error) if the metodology couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/metodologies")
    @Timed
    public ResponseEntity<Metodology> updateMetodology(@RequestBody Metodology metodology) throws URISyntaxException {
        log.debug("REST request to update Metodology : {}", metodology);
        if (metodology.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Metodology result = metodologyRepository.save(metodology);
        metodologySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, metodology.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /metodologies : Updates an existing metodology.
     *
     * @param metodology the metodology to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated metodology,
     * or with status 400 (Bad Request) if the metodology is not valid,
     * or with status 500 (Internal Server Error) if the metodology couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PatchMapping("/metodologies")
    @Timed
    public ResponseEntity<Metodology> _updateMetodology(@RequestBody Metodology metodology) throws URISyntaxException {
        log.debug("REST request to update Metodology : {}", metodology);
        if (metodology.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Metodology result = metodologyRepository.save(metodology);
        metodologySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, metodology.getId().toString()))
            .body(result);
    }

    /**
     * GET  /metodologies : get all the metodologies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of metodologies in body
     */
    @GetMapping("/metodologies")
    @Timed
    public List<Metodology> getAllMetodologies() {
        log.debug("REST request to get all Metodologies");
        return metodologyRepository.findAll();
    }

    /**
     * GET  /metodologies/:id : get the "id" metodology.
     *
     * @param id the id of the metodology to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the metodology, or with status 404 (Not Found)
     */
    @GetMapping("/metodologies/{id}")
    @Timed
    public ResponseEntity<Metodology> getMetodology(@PathVariable Long id) {
        log.debug("REST request to get Metodology : {}", id);
        Optional<Metodology> metodology = metodologyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(metodology);
    }

    /**
     * DELETE  /metodologies/:id : delete the "id" metodology.
     *
     * @param id the id of the metodology to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/metodologies/{id}")
    @Timed
    public ResponseEntity<Void> deleteMetodology(@PathVariable Long id) {
        log.debug("REST request to delete Metodology : {}", id);

        metodologyRepository.deleteById(id);
        metodologySearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/metodologies?query=:query : search for the metodology corresponding
     * to the query.
     *
     * @param query the query of the metodology search
     * @return the result of the search
     */
    @GetMapping("/_search/metodologies")
    @Timed
    public List<Metodology> searchMetodologies(@RequestParam String query) {
        log.debug("REST request to search Metodologies for query {}", query);
        return StreamSupport
            .stream(metodologySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
