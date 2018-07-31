package org.sopac.web.rest;

import org.sopac.ClimatefinanceApp;

import org.sopac.domain.Disbursement;
import org.sopac.repository.DisbursementRepository;
import org.sopac.repository.search.DisbursementSearchRepository;
import org.sopac.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.sopac.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DisbursementResource REST controller.
 *
 * @see DisbursementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClimatefinanceApp.class)
public class DisbursementResourceIntTest {

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Autowired
    private DisbursementRepository disbursementRepository;

    @Autowired
    private DisbursementSearchRepository disbursementSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDisbursementMockMvc;

    private Disbursement disbursement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DisbursementResource disbursementResource = new DisbursementResource(disbursementRepository, disbursementSearchRepository);
        this.restDisbursementMockMvc = MockMvcBuilders.standaloneSetup(disbursementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disbursement createEntity(EntityManager em) {
        Disbursement disbursement = new Disbursement()
            .year(DEFAULT_YEAR)
            .amount(DEFAULT_AMOUNT);
        return disbursement;
    }

    @Before
    public void initTest() {
        disbursementSearchRepository.deleteAll();
        disbursement = createEntity(em);
    }

    @Test
    @Transactional
    public void createDisbursement() throws Exception {
        int databaseSizeBeforeCreate = disbursementRepository.findAll().size();

        // Create the Disbursement
        restDisbursementMockMvc.perform(post("/api/disbursements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disbursement)))
            .andExpect(status().isCreated());

        // Validate the Disbursement in the database
        List<Disbursement> disbursementList = disbursementRepository.findAll();
        assertThat(disbursementList).hasSize(databaseSizeBeforeCreate + 1);
        Disbursement testDisbursement = disbursementList.get(disbursementList.size() - 1);
        assertThat(testDisbursement.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testDisbursement.getAmount()).isEqualTo(DEFAULT_AMOUNT);

        // Validate the Disbursement in Elasticsearch
        Disbursement disbursementEs = disbursementSearchRepository.findOne(testDisbursement.getId());
        assertThat(disbursementEs).isEqualToIgnoringGivenFields(testDisbursement);
    }

    @Test
    @Transactional
    public void createDisbursementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = disbursementRepository.findAll().size();

        // Create the Disbursement with an existing ID
        disbursement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisbursementMockMvc.perform(post("/api/disbursements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disbursement)))
            .andExpect(status().isBadRequest());

        // Validate the Disbursement in the database
        List<Disbursement> disbursementList = disbursementRepository.findAll();
        assertThat(disbursementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDisbursements() throws Exception {
        // Initialize the database
        disbursementRepository.saveAndFlush(disbursement);

        // Get all the disbursementList
        restDisbursementMockMvc.perform(get("/api/disbursements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disbursement.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getDisbursement() throws Exception {
        // Initialize the database
        disbursementRepository.saveAndFlush(disbursement);

        // Get the disbursement
        restDisbursementMockMvc.perform(get("/api/disbursements/{id}", disbursement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(disbursement.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDisbursement() throws Exception {
        // Get the disbursement
        restDisbursementMockMvc.perform(get("/api/disbursements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDisbursement() throws Exception {
        // Initialize the database
        disbursementRepository.saveAndFlush(disbursement);
        disbursementSearchRepository.save(disbursement);
        int databaseSizeBeforeUpdate = disbursementRepository.findAll().size();

        // Update the disbursement
        Disbursement updatedDisbursement = disbursementRepository.findOne(disbursement.getId());
        // Disconnect from session so that the updates on updatedDisbursement are not directly saved in db
        em.detach(updatedDisbursement);
        updatedDisbursement
            .year(UPDATED_YEAR)
            .amount(UPDATED_AMOUNT);

        restDisbursementMockMvc.perform(put("/api/disbursements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDisbursement)))
            .andExpect(status().isOk());

        // Validate the Disbursement in the database
        List<Disbursement> disbursementList = disbursementRepository.findAll();
        assertThat(disbursementList).hasSize(databaseSizeBeforeUpdate);
        Disbursement testDisbursement = disbursementList.get(disbursementList.size() - 1);
        assertThat(testDisbursement.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testDisbursement.getAmount()).isEqualTo(UPDATED_AMOUNT);

        // Validate the Disbursement in Elasticsearch
        Disbursement disbursementEs = disbursementSearchRepository.findOne(testDisbursement.getId());
        assertThat(disbursementEs).isEqualToIgnoringGivenFields(testDisbursement);
    }

    @Test
    @Transactional
    public void updateNonExistingDisbursement() throws Exception {
        int databaseSizeBeforeUpdate = disbursementRepository.findAll().size();

        // Create the Disbursement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDisbursementMockMvc.perform(put("/api/disbursements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disbursement)))
            .andExpect(status().isCreated());

        // Validate the Disbursement in the database
        List<Disbursement> disbursementList = disbursementRepository.findAll();
        assertThat(disbursementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDisbursement() throws Exception {
        // Initialize the database
        disbursementRepository.saveAndFlush(disbursement);
        disbursementSearchRepository.save(disbursement);
        int databaseSizeBeforeDelete = disbursementRepository.findAll().size();

        // Get the disbursement
        restDisbursementMockMvc.perform(delete("/api/disbursements/{id}", disbursement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean disbursementExistsInEs = disbursementSearchRepository.exists(disbursement.getId());
        assertThat(disbursementExistsInEs).isFalse();

        // Validate the database is empty
        List<Disbursement> disbursementList = disbursementRepository.findAll();
        assertThat(disbursementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDisbursement() throws Exception {
        // Initialize the database
        disbursementRepository.saveAndFlush(disbursement);
        disbursementSearchRepository.save(disbursement);

        // Search the disbursement
        restDisbursementMockMvc.perform(get("/api/_search/disbursements?query=id:" + disbursement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disbursement.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disbursement.class);
        Disbursement disbursement1 = new Disbursement();
        disbursement1.setId(1L);
        Disbursement disbursement2 = new Disbursement();
        disbursement2.setId(disbursement1.getId());
        assertThat(disbursement1).isEqualTo(disbursement2);
        disbursement2.setId(2L);
        assertThat(disbursement1).isNotEqualTo(disbursement2);
        disbursement1.setId(null);
        assertThat(disbursement1).isNotEqualTo(disbursement2);
    }
}
