package org.sopac.web.rest;

import org.sopac.ClimatefinanceApp;

import org.sopac.domain.Project;
import org.sopac.repository.ProjectRepository;
import org.sopac.repository.search.ProjectSearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.sopac.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.sopac.domain.enumeration.ProjectType;
import org.sopac.domain.enumeration.FundingBasis;
import org.sopac.domain.enumeration.Currency;
import org.sopac.domain.enumeration.Laterality;
import org.sopac.domain.enumeration.Status;
import org.sopac.domain.enumeration.Modality;
/**
 * Test class for the ProjectResource REST controller.
 *
 * @see ProjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClimatefinanceApp.class)
public class ProjectResourceIntTest {

    private static final String DEFAULT_PROJECT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_DESCRIPTION = "BBBBBBBBBB";

    private static final ProjectType DEFAULT_PROJECT_TYPE = ProjectType.CCA;
    private static final ProjectType UPDATED_PROJECT_TYPE = ProjectType.CCM;

    private static final FundingBasis DEFAULT_FUNDING_BASIS = FundingBasis.NATIONAL;
    private static final FundingBasis UPDATED_FUNDING_BASIS = FundingBasis.REGIONAL;

    private static final Double DEFAULT_TOTAL_FUNDING_AMOUNT = 1D;
    private static final Double UPDATED_TOTAL_FUNDING_AMOUNT = 2D;

    private static final Currency DEFAULT_TOTAL_FUNDING_CURRENCY = Currency.USD;
    private static final Currency UPDATED_TOTAL_FUNDING_CURRENCY = Currency.EUR;

    private static final Double DEFAULT_ESTIMATED_COUNTRY_ALLOCATION = 1D;
    private static final Double UPDATED_ESTIMATED_COUNTRY_ALLOCATION = 2D;

    private static final String DEFAULT_TIME_FRAME = "AAAAAAAAAA";
    private static final String UPDATED_TIME_FRAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRINCIPAL_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_PRINCIPAL_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDITIONAL_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_MINISTRY = "AAAAAAAAAA";
    private static final String UPDATED_MINISTRY = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_STAKEHOLDERS = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_STAKEHOLDERS = "BBBBBBBBBB";

    private static final Laterality DEFAULT_LATERALITY = Laterality.BILATERAL;
    private static final Laterality UPDATED_LATERALITY = Laterality.MULTILATERAL;

    private static final Boolean DEFAULT_APPROPRIATED = false;
    private static final Boolean UPDATED_APPROPRIATED = true;

    private static final String DEFAULT_WEIGHTING_PERCENTAGE = "AAAAAAAAAA";
    private static final String UPDATED_WEIGHTING_PERCENTAGE = "BBBBBBBBBB";

    private static final String DEFAULT_INKIND_PERCENTAGE = "AAAAAAAAAA";
    private static final String UPDATED_INKIND_PERCENTAGE = "BBBBBBBBBB";

    private static final Double DEFAULT_CLIMATE_CHANGE_ADAPTATION = 1D;
    private static final Double UPDATED_CLIMATE_CHANGE_ADAPTATION = 2D;

    private static final Double DEFAULT_CLIMATE_CHANGE_MITIGATION = 1D;
    private static final Double UPDATED_CLIMATE_CHANGE_MITIGATION = 2D;

    private static final Double DEFAULT_DISASTER_RISK_REDUCTION = 1D;
    private static final Double UPDATED_DISASTER_RISK_REDUCTION = 2D;

    private static final Double DEFAULT_DISASTER_RISK_MITIGATION = 1D;
    private static final Double UPDATED_DISASTER_RISK_MITIGATION = 2D;

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    private static final Status DEFAULT_STATUS = Status.PIPELINE;
    private static final Status UPDATED_STATUS = Status.CURRENT;

    private static final Modality DEFAULT_MODALITY = Modality.ON_BUDGET;
    private static final Modality UPDATED_MODALITY = Modality.OFF_BUDGET;

    private static final LocalDate DEFAULT_START_YEAR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_YEAR = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_YEAR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_YEAR = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectSearchRepository projectSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectMockMvc;

    private Project project;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectResource projectResource = new ProjectResource(projectRepository, projectSearchRepository);
        this.restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
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
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
            .projectTitle(DEFAULT_PROJECT_TITLE)
            .projectDescription(DEFAULT_PROJECT_DESCRIPTION)
            .projectType(DEFAULT_PROJECT_TYPE)
            .fundingBasis(DEFAULT_FUNDING_BASIS)
            .totalFundingAmount(DEFAULT_TOTAL_FUNDING_AMOUNT)
            .totalFundingCurrency(DEFAULT_TOTAL_FUNDING_CURRENCY)
            .estimatedCountryAllocation(DEFAULT_ESTIMATED_COUNTRY_ALLOCATION)
            .timeFrame(DEFAULT_TIME_FRAME)
            .principalSource(DEFAULT_PRINCIPAL_SOURCE)
            .additionalSource(DEFAULT_ADDITIONAL_SOURCE)
            .ministry(DEFAULT_MINISTRY)
            .otherStakeholders(DEFAULT_OTHER_STAKEHOLDERS)
            .laterality(DEFAULT_LATERALITY)
            .appropriated(DEFAULT_APPROPRIATED)
            .weightingPercentage(DEFAULT_WEIGHTING_PERCENTAGE)
            .inkindPercentage(DEFAULT_INKIND_PERCENTAGE)
            .climateChangeAdaptation(DEFAULT_CLIMATE_CHANGE_ADAPTATION)
            .climateChangeMitigation(DEFAULT_CLIMATE_CHANGE_MITIGATION)
            .disasterRiskReduction(DEFAULT_DISASTER_RISK_REDUCTION)
            .disasterRiskMitigation(DEFAULT_DISASTER_RISK_MITIGATION)
            .total(DEFAULT_TOTAL)
            .status(DEFAULT_STATUS)
            .modality(DEFAULT_MODALITY)
            .startYear(DEFAULT_START_YEAR)
            .endYear(DEFAULT_END_YEAR)
            .active(DEFAULT_ACTIVE)
            .notes(DEFAULT_NOTES);
        return project;
    }

    @Before
    public void initTest() {
        projectSearchRepository.deleteAll();
        project = createEntity(em);
    }

    @Test
    @Transactional
    public void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project
        restProjectMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectTitle()).isEqualTo(DEFAULT_PROJECT_TITLE);
        assertThat(testProject.getProjectDescription()).isEqualTo(DEFAULT_PROJECT_DESCRIPTION);
        assertThat(testProject.getProjectType()).isEqualTo(DEFAULT_PROJECT_TYPE);
        assertThat(testProject.getFundingBasis()).isEqualTo(DEFAULT_FUNDING_BASIS);
        assertThat(testProject.getTotalFundingAmount()).isEqualTo(DEFAULT_TOTAL_FUNDING_AMOUNT);
        assertThat(testProject.getTotalFundingCurrency()).isEqualTo(DEFAULT_TOTAL_FUNDING_CURRENCY);
        assertThat(testProject.getEstimatedCountryAllocation()).isEqualTo(DEFAULT_ESTIMATED_COUNTRY_ALLOCATION);
        assertThat(testProject.getTimeFrame()).isEqualTo(DEFAULT_TIME_FRAME);
        assertThat(testProject.getPrincipalSource()).isEqualTo(DEFAULT_PRINCIPAL_SOURCE);
        assertThat(testProject.getAdditionalSource()).isEqualTo(DEFAULT_ADDITIONAL_SOURCE);
        assertThat(testProject.getMinistry()).isEqualTo(DEFAULT_MINISTRY);
        assertThat(testProject.getOtherStakeholders()).isEqualTo(DEFAULT_OTHER_STAKEHOLDERS);
        assertThat(testProject.getLaterality()).isEqualTo(DEFAULT_LATERALITY);
        assertThat(testProject.isAppropriated()).isEqualTo(DEFAULT_APPROPRIATED);
        assertThat(testProject.getWeightingPercentage()).isEqualTo(DEFAULT_WEIGHTING_PERCENTAGE);
        assertThat(testProject.getInkindPercentage()).isEqualTo(DEFAULT_INKIND_PERCENTAGE);
        assertThat(testProject.getClimateChangeAdaptation()).isEqualTo(DEFAULT_CLIMATE_CHANGE_ADAPTATION);
        assertThat(testProject.getClimateChangeMitigation()).isEqualTo(DEFAULT_CLIMATE_CHANGE_MITIGATION);
        assertThat(testProject.getDisasterRiskReduction()).isEqualTo(DEFAULT_DISASTER_RISK_REDUCTION);
        assertThat(testProject.getDisasterRiskMitigation()).isEqualTo(DEFAULT_DISASTER_RISK_MITIGATION);
        assertThat(testProject.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testProject.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProject.getModality()).isEqualTo(DEFAULT_MODALITY);
        assertThat(testProject.getStartYear()).isEqualTo(DEFAULT_START_YEAR);
        assertThat(testProject.getEndYear()).isEqualTo(DEFAULT_END_YEAR);
        assertThat(testProject.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testProject.getNotes()).isEqualTo(DEFAULT_NOTES);

        // Validate the Project in Elasticsearch
        Project projectEs = projectSearchRepository.findOne(testProject.getId());
        assertThat(projectEs).isEqualToIgnoringGivenFields(testProject);
    }

    @Test
    @Transactional
    public void createProjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project with an existing ID
        project.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectTitle").value(hasItem(DEFAULT_PROJECT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].projectDescription").value(hasItem(DEFAULT_PROJECT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].projectType").value(hasItem(DEFAULT_PROJECT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fundingBasis").value(hasItem(DEFAULT_FUNDING_BASIS.toString())))
            .andExpect(jsonPath("$.[*].totalFundingAmount").value(hasItem(DEFAULT_TOTAL_FUNDING_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalFundingCurrency").value(hasItem(DEFAULT_TOTAL_FUNDING_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].estimatedCountryAllocation").value(hasItem(DEFAULT_ESTIMATED_COUNTRY_ALLOCATION.doubleValue())))
            .andExpect(jsonPath("$.[*].timeFrame").value(hasItem(DEFAULT_TIME_FRAME.toString())))
            .andExpect(jsonPath("$.[*].principalSource").value(hasItem(DEFAULT_PRINCIPAL_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].additionalSource").value(hasItem(DEFAULT_ADDITIONAL_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].ministry").value(hasItem(DEFAULT_MINISTRY.toString())))
            .andExpect(jsonPath("$.[*].otherStakeholders").value(hasItem(DEFAULT_OTHER_STAKEHOLDERS.toString())))
            .andExpect(jsonPath("$.[*].laterality").value(hasItem(DEFAULT_LATERALITY.toString())))
            .andExpect(jsonPath("$.[*].appropriated").value(hasItem(DEFAULT_APPROPRIATED.booleanValue())))
            .andExpect(jsonPath("$.[*].weightingPercentage").value(hasItem(DEFAULT_WEIGHTING_PERCENTAGE.toString())))
            .andExpect(jsonPath("$.[*].inkindPercentage").value(hasItem(DEFAULT_INKIND_PERCENTAGE.toString())))
            .andExpect(jsonPath("$.[*].climateChangeAdaptation").value(hasItem(DEFAULT_CLIMATE_CHANGE_ADAPTATION.doubleValue())))
            .andExpect(jsonPath("$.[*].climateChangeMitigation").value(hasItem(DEFAULT_CLIMATE_CHANGE_MITIGATION.doubleValue())))
            .andExpect(jsonPath("$.[*].disasterRiskReduction").value(hasItem(DEFAULT_DISASTER_RISK_REDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].disasterRiskMitigation").value(hasItem(DEFAULT_DISASTER_RISK_MITIGATION.doubleValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].modality").value(hasItem(DEFAULT_MODALITY.toString())))
            .andExpect(jsonPath("$.[*].startYear").value(hasItem(DEFAULT_START_YEAR.toString())))
            .andExpect(jsonPath("$.[*].endYear").value(hasItem(DEFAULT_END_YEAR.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.projectTitle").value(DEFAULT_PROJECT_TITLE.toString()))
            .andExpect(jsonPath("$.projectDescription").value(DEFAULT_PROJECT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.projectType").value(DEFAULT_PROJECT_TYPE.toString()))
            .andExpect(jsonPath("$.fundingBasis").value(DEFAULT_FUNDING_BASIS.toString()))
            .andExpect(jsonPath("$.totalFundingAmount").value(DEFAULT_TOTAL_FUNDING_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.totalFundingCurrency").value(DEFAULT_TOTAL_FUNDING_CURRENCY.toString()))
            .andExpect(jsonPath("$.estimatedCountryAllocation").value(DEFAULT_ESTIMATED_COUNTRY_ALLOCATION.doubleValue()))
            .andExpect(jsonPath("$.timeFrame").value(DEFAULT_TIME_FRAME.toString()))
            .andExpect(jsonPath("$.principalSource").value(DEFAULT_PRINCIPAL_SOURCE.toString()))
            .andExpect(jsonPath("$.additionalSource").value(DEFAULT_ADDITIONAL_SOURCE.toString()))
            .andExpect(jsonPath("$.ministry").value(DEFAULT_MINISTRY.toString()))
            .andExpect(jsonPath("$.otherStakeholders").value(DEFAULT_OTHER_STAKEHOLDERS.toString()))
            .andExpect(jsonPath("$.laterality").value(DEFAULT_LATERALITY.toString()))
            .andExpect(jsonPath("$.appropriated").value(DEFAULT_APPROPRIATED.booleanValue()))
            .andExpect(jsonPath("$.weightingPercentage").value(DEFAULT_WEIGHTING_PERCENTAGE.toString()))
            .andExpect(jsonPath("$.inkindPercentage").value(DEFAULT_INKIND_PERCENTAGE.toString()))
            .andExpect(jsonPath("$.climateChangeAdaptation").value(DEFAULT_CLIMATE_CHANGE_ADAPTATION.doubleValue()))
            .andExpect(jsonPath("$.climateChangeMitigation").value(DEFAULT_CLIMATE_CHANGE_MITIGATION.doubleValue()))
            .andExpect(jsonPath("$.disasterRiskReduction").value(DEFAULT_DISASTER_RISK_REDUCTION.doubleValue()))
            .andExpect(jsonPath("$.disasterRiskMitigation").value(DEFAULT_DISASTER_RISK_MITIGATION.doubleValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.modality").value(DEFAULT_MODALITY.toString()))
            .andExpect(jsonPath("$.startYear").value(DEFAULT_START_YEAR.toString()))
            .andExpect(jsonPath("$.endYear").value(DEFAULT_END_YEAR.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        projectSearchRepository.save(project);
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findOne(project.getId());
        // Disconnect from session so that the updates on updatedProject are not directly saved in db
        em.detach(updatedProject);
        updatedProject
            .projectTitle(UPDATED_PROJECT_TITLE)
            .projectDescription(UPDATED_PROJECT_DESCRIPTION)
            .projectType(UPDATED_PROJECT_TYPE)
            .fundingBasis(UPDATED_FUNDING_BASIS)
            .totalFundingAmount(UPDATED_TOTAL_FUNDING_AMOUNT)
            .totalFundingCurrency(UPDATED_TOTAL_FUNDING_CURRENCY)
            .estimatedCountryAllocation(UPDATED_ESTIMATED_COUNTRY_ALLOCATION)
            .timeFrame(UPDATED_TIME_FRAME)
            .principalSource(UPDATED_PRINCIPAL_SOURCE)
            .additionalSource(UPDATED_ADDITIONAL_SOURCE)
            .ministry(UPDATED_MINISTRY)
            .otherStakeholders(UPDATED_OTHER_STAKEHOLDERS)
            .laterality(UPDATED_LATERALITY)
            .appropriated(UPDATED_APPROPRIATED)
            .weightingPercentage(UPDATED_WEIGHTING_PERCENTAGE)
            .inkindPercentage(UPDATED_INKIND_PERCENTAGE)
            .climateChangeAdaptation(UPDATED_CLIMATE_CHANGE_ADAPTATION)
            .climateChangeMitigation(UPDATED_CLIMATE_CHANGE_MITIGATION)
            .disasterRiskReduction(UPDATED_DISASTER_RISK_REDUCTION)
            .disasterRiskMitigation(UPDATED_DISASTER_RISK_MITIGATION)
            .total(UPDATED_TOTAL)
            .status(UPDATED_STATUS)
            .modality(UPDATED_MODALITY)
            .startYear(UPDATED_START_YEAR)
            .endYear(UPDATED_END_YEAR)
            .active(UPDATED_ACTIVE)
            .notes(UPDATED_NOTES);

        restProjectMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProject)))
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getProjectTitle()).isEqualTo(UPDATED_PROJECT_TITLE);
        assertThat(testProject.getProjectDescription()).isEqualTo(UPDATED_PROJECT_DESCRIPTION);
        assertThat(testProject.getProjectType()).isEqualTo(UPDATED_PROJECT_TYPE);
        assertThat(testProject.getFundingBasis()).isEqualTo(UPDATED_FUNDING_BASIS);
        assertThat(testProject.getTotalFundingAmount()).isEqualTo(UPDATED_TOTAL_FUNDING_AMOUNT);
        assertThat(testProject.getTotalFundingCurrency()).isEqualTo(UPDATED_TOTAL_FUNDING_CURRENCY);
        assertThat(testProject.getEstimatedCountryAllocation()).isEqualTo(UPDATED_ESTIMATED_COUNTRY_ALLOCATION);
        assertThat(testProject.getTimeFrame()).isEqualTo(UPDATED_TIME_FRAME);
        assertThat(testProject.getPrincipalSource()).isEqualTo(UPDATED_PRINCIPAL_SOURCE);
        assertThat(testProject.getAdditionalSource()).isEqualTo(UPDATED_ADDITIONAL_SOURCE);
        assertThat(testProject.getMinistry()).isEqualTo(UPDATED_MINISTRY);
        assertThat(testProject.getOtherStakeholders()).isEqualTo(UPDATED_OTHER_STAKEHOLDERS);
        assertThat(testProject.getLaterality()).isEqualTo(UPDATED_LATERALITY);
        assertThat(testProject.isAppropriated()).isEqualTo(UPDATED_APPROPRIATED);
        assertThat(testProject.getWeightingPercentage()).isEqualTo(UPDATED_WEIGHTING_PERCENTAGE);
        assertThat(testProject.getInkindPercentage()).isEqualTo(UPDATED_INKIND_PERCENTAGE);
        assertThat(testProject.getClimateChangeAdaptation()).isEqualTo(UPDATED_CLIMATE_CHANGE_ADAPTATION);
        assertThat(testProject.getClimateChangeMitigation()).isEqualTo(UPDATED_CLIMATE_CHANGE_MITIGATION);
        assertThat(testProject.getDisasterRiskReduction()).isEqualTo(UPDATED_DISASTER_RISK_REDUCTION);
        assertThat(testProject.getDisasterRiskMitigation()).isEqualTo(UPDATED_DISASTER_RISK_MITIGATION);
        assertThat(testProject.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testProject.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProject.getModality()).isEqualTo(UPDATED_MODALITY);
        assertThat(testProject.getStartYear()).isEqualTo(UPDATED_START_YEAR);
        assertThat(testProject.getEndYear()).isEqualTo(UPDATED_END_YEAR);
        assertThat(testProject.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testProject.getNotes()).isEqualTo(UPDATED_NOTES);

        // Validate the Project in Elasticsearch
        Project projectEs = projectSearchRepository.findOne(testProject.getId());
        assertThat(projectEs).isEqualToIgnoringGivenFields(testProject);
    }

    @Test
    @Transactional
    public void updateNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Create the Project

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        projectSearchRepository.save(project);
        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Get the project
        restProjectMockMvc.perform(delete("/api/projects/{id}", project.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean projectExistsInEs = projectSearchRepository.exists(project.getId());
        assertThat(projectExistsInEs).isFalse();

        // Validate the database is empty
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        projectSearchRepository.save(project);

        // Search the project
        restProjectMockMvc.perform(get("/api/_search/projects?query=id:" + project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectTitle").value(hasItem(DEFAULT_PROJECT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].projectDescription").value(hasItem(DEFAULT_PROJECT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].projectType").value(hasItem(DEFAULT_PROJECT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fundingBasis").value(hasItem(DEFAULT_FUNDING_BASIS.toString())))
            .andExpect(jsonPath("$.[*].totalFundingAmount").value(hasItem(DEFAULT_TOTAL_FUNDING_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalFundingCurrency").value(hasItem(DEFAULT_TOTAL_FUNDING_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].estimatedCountryAllocation").value(hasItem(DEFAULT_ESTIMATED_COUNTRY_ALLOCATION.doubleValue())))
            .andExpect(jsonPath("$.[*].timeFrame").value(hasItem(DEFAULT_TIME_FRAME.toString())))
            .andExpect(jsonPath("$.[*].principalSource").value(hasItem(DEFAULT_PRINCIPAL_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].additionalSource").value(hasItem(DEFAULT_ADDITIONAL_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].ministry").value(hasItem(DEFAULT_MINISTRY.toString())))
            .andExpect(jsonPath("$.[*].otherStakeholders").value(hasItem(DEFAULT_OTHER_STAKEHOLDERS.toString())))
            .andExpect(jsonPath("$.[*].laterality").value(hasItem(DEFAULT_LATERALITY.toString())))
            .andExpect(jsonPath("$.[*].appropriated").value(hasItem(DEFAULT_APPROPRIATED.booleanValue())))
            .andExpect(jsonPath("$.[*].weightingPercentage").value(hasItem(DEFAULT_WEIGHTING_PERCENTAGE.toString())))
            .andExpect(jsonPath("$.[*].inkindPercentage").value(hasItem(DEFAULT_INKIND_PERCENTAGE.toString())))
            .andExpect(jsonPath("$.[*].climateChangeAdaptation").value(hasItem(DEFAULT_CLIMATE_CHANGE_ADAPTATION.doubleValue())))
            .andExpect(jsonPath("$.[*].climateChangeMitigation").value(hasItem(DEFAULT_CLIMATE_CHANGE_MITIGATION.doubleValue())))
            .andExpect(jsonPath("$.[*].disasterRiskReduction").value(hasItem(DEFAULT_DISASTER_RISK_REDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].disasterRiskMitigation").value(hasItem(DEFAULT_DISASTER_RISK_MITIGATION.doubleValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].modality").value(hasItem(DEFAULT_MODALITY.toString())))
            .andExpect(jsonPath("$.[*].startYear").value(hasItem(DEFAULT_START_YEAR.toString())))
            .andExpect(jsonPath("$.[*].endYear").value(hasItem(DEFAULT_END_YEAR.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Project.class);
        Project project1 = new Project();
        project1.setId(1L);
        Project project2 = new Project();
        project2.setId(project1.getId());
        assertThat(project1).isEqualTo(project2);
        project2.setId(2L);
        assertThat(project1).isNotEqualTo(project2);
        project1.setId(null);
        assertThat(project1).isNotEqualTo(project2);
    }
}
