package org.sopac.web.rest;

import org.sopac.ClimatefinanceApp;

import org.sopac.domain.Integration;
import org.sopac.repository.IntegrationRepository;
import org.sopac.repository.search.IntegrationSearchRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.sopac.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.sopac.domain.enumeration.Platform;
import org.sopac.domain.enumeration.Schedule;
/**
 * Test class for the IntegrationResource REST controller.
 *
 * @see IntegrationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClimatefinanceApp.class)
public class IntegrationResourceIntTest {

    private static final Platform DEFAULT_PLATFORM = Platform.MSSQL;
    private static final Platform UPDATED_PLATFORM = Platform.POSTGRESQL;

    private static final String DEFAULT_DRIVER = "AAAAAAAAAA";
    private static final String UPDATED_DRIVER = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_FLAT_FOLDER = "AAAAAAAAAA";
    private static final String UPDATED_FLAT_FOLDER = "BBBBBBBBBB";

    private static final String DEFAULT_FLAT_FILE = "AAAAAAAAAA";
    private static final String UPDATED_FLAT_FILE = "BBBBBBBBBB";

    private static final Schedule DEFAULT_SCHEDULE = Schedule.DAILY;
    private static final Schedule UPDATED_SCHEDULE = Schedule.BIWEEKLY;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_MAPPING = "AAAAAAAAAA";
    private static final String UPDATED_MAPPING = "BBBBBBBBBB";

    @Autowired
    private IntegrationRepository integrationRepository;

    @Autowired
    private IntegrationSearchRepository integrationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIntegrationMockMvc;

    private Integration integration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IntegrationResource integrationResource = new IntegrationResource(integrationRepository, integrationSearchRepository);
        this.restIntegrationMockMvc = MockMvcBuilders.standaloneSetup(integrationResource)
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
    public static Integration createEntity(EntityManager em) {
        Integration integration = new Integration()
            .platform(DEFAULT_PLATFORM)
            .driver(DEFAULT_DRIVER)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .flatFolder(DEFAULT_FLAT_FOLDER)
            .flatFile(DEFAULT_FLAT_FILE)
            .schedule(DEFAULT_SCHEDULE)
            .active(DEFAULT_ACTIVE)
            .mapping(DEFAULT_MAPPING);
        return integration;
    }

    @Before
    public void initTest() {
        integrationSearchRepository.deleteAll();
        integration = createEntity(em);
    }

    @Test
    @Transactional
    public void createIntegration() throws Exception {
        int databaseSizeBeforeCreate = integrationRepository.findAll().size();

        // Create the Integration
        restIntegrationMockMvc.perform(post("/api/integrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(integration)))
            .andExpect(status().isCreated());

        // Validate the Integration in the database
        List<Integration> integrationList = integrationRepository.findAll();
        assertThat(integrationList).hasSize(databaseSizeBeforeCreate + 1);
        Integration testIntegration = integrationList.get(integrationList.size() - 1);
        assertThat(testIntegration.getPlatform()).isEqualTo(DEFAULT_PLATFORM);
        assertThat(testIntegration.getDriver()).isEqualTo(DEFAULT_DRIVER);
        assertThat(testIntegration.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testIntegration.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testIntegration.getFlatFolder()).isEqualTo(DEFAULT_FLAT_FOLDER);
        assertThat(testIntegration.getFlatFile()).isEqualTo(DEFAULT_FLAT_FILE);
        assertThat(testIntegration.getSchedule()).isEqualTo(DEFAULT_SCHEDULE);
        assertThat(testIntegration.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testIntegration.getMapping()).isEqualTo(DEFAULT_MAPPING);

        // Validate the Integration in Elasticsearch
        Integration integrationEs = integrationSearchRepository.findOne(testIntegration.getId());
        assertThat(integrationEs).isEqualToIgnoringGivenFields(testIntegration);
    }

    @Test
    @Transactional
    public void createIntegrationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = integrationRepository.findAll().size();

        // Create the Integration with an existing ID
        integration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIntegrationMockMvc.perform(post("/api/integrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(integration)))
            .andExpect(status().isBadRequest());

        // Validate the Integration in the database
        List<Integration> integrationList = integrationRepository.findAll();
        assertThat(integrationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllIntegrations() throws Exception {
        // Initialize the database
        integrationRepository.saveAndFlush(integration);

        // Get all the integrationList
        restIntegrationMockMvc.perform(get("/api/integrations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(integration.getId().intValue())))
            .andExpect(jsonPath("$.[*].platform").value(hasItem(DEFAULT_PLATFORM.toString())))
            .andExpect(jsonPath("$.[*].driver").value(hasItem(DEFAULT_DRIVER.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].flatFolder").value(hasItem(DEFAULT_FLAT_FOLDER.toString())))
            .andExpect(jsonPath("$.[*].flatFile").value(hasItem(DEFAULT_FLAT_FILE.toString())))
            .andExpect(jsonPath("$.[*].schedule").value(hasItem(DEFAULT_SCHEDULE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].mapping").value(hasItem(DEFAULT_MAPPING.toString())));
    }

    @Test
    @Transactional
    public void getIntegration() throws Exception {
        // Initialize the database
        integrationRepository.saveAndFlush(integration);

        // Get the integration
        restIntegrationMockMvc.perform(get("/api/integrations/{id}", integration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(integration.getId().intValue()))
            .andExpect(jsonPath("$.platform").value(DEFAULT_PLATFORM.toString()))
            .andExpect(jsonPath("$.driver").value(DEFAULT_DRIVER.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.flatFolder").value(DEFAULT_FLAT_FOLDER.toString()))
            .andExpect(jsonPath("$.flatFile").value(DEFAULT_FLAT_FILE.toString()))
            .andExpect(jsonPath("$.schedule").value(DEFAULT_SCHEDULE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.mapping").value(DEFAULT_MAPPING.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIntegration() throws Exception {
        // Get the integration
        restIntegrationMockMvc.perform(get("/api/integrations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIntegration() throws Exception {
        // Initialize the database
        integrationRepository.saveAndFlush(integration);
        integrationSearchRepository.save(integration);
        int databaseSizeBeforeUpdate = integrationRepository.findAll().size();

        // Update the integration
        Integration updatedIntegration = integrationRepository.findOne(integration.getId());
        // Disconnect from session so that the updates on updatedIntegration are not directly saved in db
        em.detach(updatedIntegration);
        updatedIntegration
            .platform(UPDATED_PLATFORM)
            .driver(UPDATED_DRIVER)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .flatFolder(UPDATED_FLAT_FOLDER)
            .flatFile(UPDATED_FLAT_FILE)
            .schedule(UPDATED_SCHEDULE)
            .active(UPDATED_ACTIVE)
            .mapping(UPDATED_MAPPING);

        restIntegrationMockMvc.perform(put("/api/integrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIntegration)))
            .andExpect(status().isOk());

        // Validate the Integration in the database
        List<Integration> integrationList = integrationRepository.findAll();
        assertThat(integrationList).hasSize(databaseSizeBeforeUpdate);
        Integration testIntegration = integrationList.get(integrationList.size() - 1);
        assertThat(testIntegration.getPlatform()).isEqualTo(UPDATED_PLATFORM);
        assertThat(testIntegration.getDriver()).isEqualTo(UPDATED_DRIVER);
        assertThat(testIntegration.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testIntegration.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testIntegration.getFlatFolder()).isEqualTo(UPDATED_FLAT_FOLDER);
        assertThat(testIntegration.getFlatFile()).isEqualTo(UPDATED_FLAT_FILE);
        assertThat(testIntegration.getSchedule()).isEqualTo(UPDATED_SCHEDULE);
        assertThat(testIntegration.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testIntegration.getMapping()).isEqualTo(UPDATED_MAPPING);

        // Validate the Integration in Elasticsearch
        Integration integrationEs = integrationSearchRepository.findOne(testIntegration.getId());
        assertThat(integrationEs).isEqualToIgnoringGivenFields(testIntegration);
    }

    @Test
    @Transactional
    public void updateNonExistingIntegration() throws Exception {
        int databaseSizeBeforeUpdate = integrationRepository.findAll().size();

        // Create the Integration

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIntegrationMockMvc.perform(put("/api/integrations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(integration)))
            .andExpect(status().isCreated());

        // Validate the Integration in the database
        List<Integration> integrationList = integrationRepository.findAll();
        assertThat(integrationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIntegration() throws Exception {
        // Initialize the database
        integrationRepository.saveAndFlush(integration);
        integrationSearchRepository.save(integration);
        int databaseSizeBeforeDelete = integrationRepository.findAll().size();

        // Get the integration
        restIntegrationMockMvc.perform(delete("/api/integrations/{id}", integration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean integrationExistsInEs = integrationSearchRepository.exists(integration.getId());
        assertThat(integrationExistsInEs).isFalse();

        // Validate the database is empty
        List<Integration> integrationList = integrationRepository.findAll();
        assertThat(integrationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIntegration() throws Exception {
        // Initialize the database
        integrationRepository.saveAndFlush(integration);
        integrationSearchRepository.save(integration);

        // Search the integration
        restIntegrationMockMvc.perform(get("/api/_search/integrations?query=id:" + integration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(integration.getId().intValue())))
            .andExpect(jsonPath("$.[*].platform").value(hasItem(DEFAULT_PLATFORM.toString())))
            .andExpect(jsonPath("$.[*].driver").value(hasItem(DEFAULT_DRIVER.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].flatFolder").value(hasItem(DEFAULT_FLAT_FOLDER.toString())))
            .andExpect(jsonPath("$.[*].flatFile").value(hasItem(DEFAULT_FLAT_FILE.toString())))
            .andExpect(jsonPath("$.[*].schedule").value(hasItem(DEFAULT_SCHEDULE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].mapping").value(hasItem(DEFAULT_MAPPING.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Integration.class);
        Integration integration1 = new Integration();
        integration1.setId(1L);
        Integration integration2 = new Integration();
        integration2.setId(integration1.getId());
        assertThat(integration1).isEqualTo(integration2);
        integration2.setId(2L);
        assertThat(integration1).isNotEqualTo(integration2);
        integration1.setId(null);
        assertThat(integration1).isNotEqualTo(integration2);
    }
}
