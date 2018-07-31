package org.sopac.web.rest;

import org.sopac.ClimatefinanceApp;

import org.sopac.domain.DetailedSector;
import org.sopac.repository.DetailedSectorRepository;
import org.sopac.repository.search.DetailedSectorSearchRepository;
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
 * Test class for the DetailedSectorResource REST controller.
 *
 * @see DetailedSectorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClimatefinanceApp.class)
public class DetailedSectorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private DetailedSectorRepository detailedSectorRepository;

    @Autowired
    private DetailedSectorSearchRepository detailedSectorSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDetailedSectorMockMvc;

    private DetailedSector detailedSector;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DetailedSectorResource detailedSectorResource = new DetailedSectorResource(detailedSectorRepository, detailedSectorSearchRepository);
        this.restDetailedSectorMockMvc = MockMvcBuilders.standaloneSetup(detailedSectorResource)
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
    public static DetailedSector createEntity(EntityManager em) {
        DetailedSector detailedSector = new DetailedSector()
            .name(DEFAULT_NAME);
        return detailedSector;
    }

    @Before
    public void initTest() {
        detailedSectorSearchRepository.deleteAll();
        detailedSector = createEntity(em);
    }

    @Test
    @Transactional
    public void createDetailedSector() throws Exception {
        int databaseSizeBeforeCreate = detailedSectorRepository.findAll().size();

        // Create the DetailedSector
        restDetailedSectorMockMvc.perform(post("/api/detailed-sectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(detailedSector)))
            .andExpect(status().isCreated());

        // Validate the DetailedSector in the database
        List<DetailedSector> detailedSectorList = detailedSectorRepository.findAll();
        assertThat(detailedSectorList).hasSize(databaseSizeBeforeCreate + 1);
        DetailedSector testDetailedSector = detailedSectorList.get(detailedSectorList.size() - 1);
        assertThat(testDetailedSector.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the DetailedSector in Elasticsearch
        DetailedSector detailedSectorEs = detailedSectorSearchRepository.findOne(testDetailedSector.getId());
        assertThat(detailedSectorEs).isEqualToIgnoringGivenFields(testDetailedSector);
    }

    @Test
    @Transactional
    public void createDetailedSectorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = detailedSectorRepository.findAll().size();

        // Create the DetailedSector with an existing ID
        detailedSector.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetailedSectorMockMvc.perform(post("/api/detailed-sectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(detailedSector)))
            .andExpect(status().isBadRequest());

        // Validate the DetailedSector in the database
        List<DetailedSector> detailedSectorList = detailedSectorRepository.findAll();
        assertThat(detailedSectorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDetailedSectors() throws Exception {
        // Initialize the database
        detailedSectorRepository.saveAndFlush(detailedSector);

        // Get all the detailedSectorList
        restDetailedSectorMockMvc.perform(get("/api/detailed-sectors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailedSector.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getDetailedSector() throws Exception {
        // Initialize the database
        detailedSectorRepository.saveAndFlush(detailedSector);

        // Get the detailedSector
        restDetailedSectorMockMvc.perform(get("/api/detailed-sectors/{id}", detailedSector.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(detailedSector.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDetailedSector() throws Exception {
        // Get the detailedSector
        restDetailedSectorMockMvc.perform(get("/api/detailed-sectors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDetailedSector() throws Exception {
        // Initialize the database
        detailedSectorRepository.saveAndFlush(detailedSector);
        detailedSectorSearchRepository.save(detailedSector);
        int databaseSizeBeforeUpdate = detailedSectorRepository.findAll().size();

        // Update the detailedSector
        DetailedSector updatedDetailedSector = detailedSectorRepository.findOne(detailedSector.getId());
        // Disconnect from session so that the updates on updatedDetailedSector are not directly saved in db
        em.detach(updatedDetailedSector);
        updatedDetailedSector
            .name(UPDATED_NAME);

        restDetailedSectorMockMvc.perform(put("/api/detailed-sectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDetailedSector)))
            .andExpect(status().isOk());

        // Validate the DetailedSector in the database
        List<DetailedSector> detailedSectorList = detailedSectorRepository.findAll();
        assertThat(detailedSectorList).hasSize(databaseSizeBeforeUpdate);
        DetailedSector testDetailedSector = detailedSectorList.get(detailedSectorList.size() - 1);
        assertThat(testDetailedSector.getName()).isEqualTo(UPDATED_NAME);

        // Validate the DetailedSector in Elasticsearch
        DetailedSector detailedSectorEs = detailedSectorSearchRepository.findOne(testDetailedSector.getId());
        assertThat(detailedSectorEs).isEqualToIgnoringGivenFields(testDetailedSector);
    }

    @Test
    @Transactional
    public void updateNonExistingDetailedSector() throws Exception {
        int databaseSizeBeforeUpdate = detailedSectorRepository.findAll().size();

        // Create the DetailedSector

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDetailedSectorMockMvc.perform(put("/api/detailed-sectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(detailedSector)))
            .andExpect(status().isCreated());

        // Validate the DetailedSector in the database
        List<DetailedSector> detailedSectorList = detailedSectorRepository.findAll();
        assertThat(detailedSectorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDetailedSector() throws Exception {
        // Initialize the database
        detailedSectorRepository.saveAndFlush(detailedSector);
        detailedSectorSearchRepository.save(detailedSector);
        int databaseSizeBeforeDelete = detailedSectorRepository.findAll().size();

        // Get the detailedSector
        restDetailedSectorMockMvc.perform(delete("/api/detailed-sectors/{id}", detailedSector.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean detailedSectorExistsInEs = detailedSectorSearchRepository.exists(detailedSector.getId());
        assertThat(detailedSectorExistsInEs).isFalse();

        // Validate the database is empty
        List<DetailedSector> detailedSectorList = detailedSectorRepository.findAll();
        assertThat(detailedSectorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDetailedSector() throws Exception {
        // Initialize the database
        detailedSectorRepository.saveAndFlush(detailedSector);
        detailedSectorSearchRepository.save(detailedSector);

        // Search the detailedSector
        restDetailedSectorMockMvc.perform(get("/api/_search/detailed-sectors?query=id:" + detailedSector.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailedSector.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailedSector.class);
        DetailedSector detailedSector1 = new DetailedSector();
        detailedSector1.setId(1L);
        DetailedSector detailedSector2 = new DetailedSector();
        detailedSector2.setId(detailedSector1.getId());
        assertThat(detailedSector1).isEqualTo(detailedSector2);
        detailedSector2.setId(2L);
        assertThat(detailedSector1).isNotEqualTo(detailedSector2);
        detailedSector1.setId(null);
        assertThat(detailedSector1).isNotEqualTo(detailedSector2);
    }
}
