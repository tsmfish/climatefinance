package org.sopac.web.rest;

import org.sopac.ClimatefinanceApp;

import org.sopac.domain.Metodology;
import org.sopac.repository.MetodologyRepository;
import org.sopac.repository.search.MetodologySearchRepository;
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
import java.util.Collections;
import java.util.List;


import static org.sopac.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MetodologyResource REST controller.
 *
 * @see MetodologyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClimatefinanceApp.class)
public class MetodologyResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_MARKDOWN = "AAAAAAAAAA";
    private static final String UPDATED_MARKDOWN = "BBBBBBBBBB";

    @Autowired
    private MetodologyRepository metodologyRepository;


    /**
     * This repository is mocked in the org.sopac.repository.search test package.
     *
     * @see org.sopac.repository.search.MetodologySearchRepositoryMockConfiguration
     */
    @Autowired
    private MetodologySearchRepository mockMetodologySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMetodologyMockMvc;

    private Metodology metodology;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MetodologyResource metodologyResource = new MetodologyResource(metodologyRepository, mockMetodologySearchRepository);
        this.restMetodologyMockMvc = MockMvcBuilders.standaloneSetup(metodologyResource)
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
    public static Metodology createEntity(EntityManager em) {
        Metodology metodology = new Metodology()
            .title(DEFAULT_TITLE)
            .markdown(DEFAULT_MARKDOWN);
        return metodology;
    }

    @Before
    public void initTest() {
        metodology = createEntity(em);
    }

    @Test
    @Transactional
    public void createMetodology() throws Exception {
        int databaseSizeBeforeCreate = metodologyRepository.findAll().size();

        // Create the Metodology
        restMetodologyMockMvc.perform(post("/api/metodologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metodology)))
            .andExpect(status().isCreated());

        // Validate the Metodology in the database
        List<Metodology> metodologyList = metodologyRepository.findAll();
        assertThat(metodologyList).hasSize(databaseSizeBeforeCreate + 1);
        Metodology testMetodology = metodologyList.get(metodologyList.size() - 1);
        assertThat(testMetodology.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMetodology.getMarkdown()).isEqualTo(DEFAULT_MARKDOWN);

        // Validate the Metodology in Elasticsearch
        verify(mockMetodologySearchRepository, times(1)).save(testMetodology);
    }

    @Test
    @Transactional
    public void createMetodologyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = metodologyRepository.findAll().size();

        // Create the Metodology with an existing ID
        metodology.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetodologyMockMvc.perform(post("/api/metodologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metodology)))
            .andExpect(status().isBadRequest());

        // Validate the Metodology in the database
        List<Metodology> metodologyList = metodologyRepository.findAll();
        assertThat(metodologyList).hasSize(databaseSizeBeforeCreate);

        // Validate the Metodology in Elasticsearch
        verify(mockMetodologySearchRepository, times(0)).save(metodology);
    }

    @Test
    @Transactional
    public void getAllMetodologies() throws Exception {
        // Initialize the database
        metodologyRepository.saveAndFlush(metodology);

        // Get all the metodologyList
        restMetodologyMockMvc.perform(get("/api/metodologies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metodology.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].markdown").value(hasItem(DEFAULT_MARKDOWN.toString())));
    }
    

    @Test
    @Transactional
    public void getMetodology() throws Exception {
        // Initialize the database
        metodologyRepository.saveAndFlush(metodology);

        // Get the metodology
        restMetodologyMockMvc.perform(get("/api/metodologies/{id}", metodology.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(metodology.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.markdown").value(DEFAULT_MARKDOWN.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingMetodology() throws Exception {
        // Get the metodology
        restMetodologyMockMvc.perform(get("/api/metodologies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetodology() throws Exception {
        // Initialize the database
        metodologyRepository.saveAndFlush(metodology);

        int databaseSizeBeforeUpdate = metodologyRepository.findAll().size();

        // Update the metodology
        Metodology updatedMetodology = metodologyRepository.findById(metodology.getId()).get();
        // Disconnect from session so that the updates on updatedMetodology are not directly saved in db
        em.detach(updatedMetodology);
        updatedMetodology
            .title(UPDATED_TITLE)
            .markdown(UPDATED_MARKDOWN);

        restMetodologyMockMvc.perform(put("/api/metodologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMetodology)))
            .andExpect(status().isOk());

        // Validate the Metodology in the database
        List<Metodology> metodologyList = metodologyRepository.findAll();
        assertThat(metodologyList).hasSize(databaseSizeBeforeUpdate);
        Metodology testMetodology = metodologyList.get(metodologyList.size() - 1);
        assertThat(testMetodology.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMetodology.getMarkdown()).isEqualTo(UPDATED_MARKDOWN);

        // Validate the Metodology in Elasticsearch
        verify(mockMetodologySearchRepository, times(1)).save(testMetodology);
    }

    @Test
    @Transactional
    public void updateNonExistingMetodology() throws Exception {
        int databaseSizeBeforeUpdate = metodologyRepository.findAll().size();

        // Create the Metodology

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMetodologyMockMvc.perform(put("/api/metodologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metodology)))
            .andExpect(status().isBadRequest());

        // Validate the Metodology in the database
        List<Metodology> metodologyList = metodologyRepository.findAll();
        assertThat(metodologyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Metodology in Elasticsearch
        verify(mockMetodologySearchRepository, times(0)).save(metodology);
    }

    @Test
    @Transactional
    public void deleteMetodology() throws Exception {
        // Initialize the database
        metodologyRepository.saveAndFlush(metodology);

        int databaseSizeBeforeDelete = metodologyRepository.findAll().size();

        // Get the metodology
        restMetodologyMockMvc.perform(delete("/api/metodologies/{id}", metodology.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Metodology> metodologyList = metodologyRepository.findAll();
        assertThat(metodologyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Metodology in Elasticsearch
        verify(mockMetodologySearchRepository, times(1)).deleteById(metodology.getId());
    }

    @Test
    @Transactional
    public void searchMetodology() throws Exception {
        // Initialize the database
        metodologyRepository.saveAndFlush(metodology);
        when(mockMetodologySearchRepository.search(queryStringQuery("id:" + metodology.getId())))
            .thenReturn(Collections.singletonList(metodology));
        // Search the metodology
        restMetodologyMockMvc.perform(get("/api/_search/metodologies?query=id:" + metodology.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metodology.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].markdown").value(hasItem(DEFAULT_MARKDOWN.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Metodology.class);
        Metodology metodology1 = new Metodology();
        metodology1.setId(1L);
        Metodology metodology2 = new Metodology();
        metodology2.setId(metodology1.getId());
        assertThat(metodology1).isEqualTo(metodology2);
        metodology2.setId(2L);
        assertThat(metodology1).isNotEqualTo(metodology2);
        metodology1.setId(null);
        assertThat(metodology1).isNotEqualTo(metodology2);
    }
}
