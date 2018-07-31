package org.sopac.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.sopac.domain.enumeration.ProjectType;

import org.sopac.domain.enumeration.FundingBasis;

import org.sopac.domain.enumeration.Currency;

import org.sopac.domain.enumeration.Laterality;

import org.sopac.domain.enumeration.Status;

import org.sopac.domain.enumeration.Modality;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 1250)
    @Column(name = "project_title", length = 1250)
    private String projectTitle;

    @Lob
    @Column(name = "project_description")
    private String projectDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_type")
    private ProjectType projectType;

    @Enumerated(EnumType.STRING)
    @Column(name = "funding_basis")
    private FundingBasis fundingBasis;

    @Column(name = "total_funding_amount")
    private Double totalFundingAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "total_funding_currency")
    private Currency totalFundingCurrency;

    @Column(name = "estimated_country_allocation")
    private Double estimatedCountryAllocation;

    @Column(name = "time_frame")
    private String timeFrame;

    @Column(name = "principal_source")
    private String principalSource;

    @Column(name = "additional_source")
    private String additionalSource;

    @Column(name = "ministry")
    private String ministry;

    @Column(name = "other_stakeholders")
    private String otherStakeholders;

    @Enumerated(EnumType.STRING)
    @Column(name = "laterality")
    private Laterality laterality;

    @Column(name = "appropriated")
    private Boolean appropriated;

    @Column(name = "weighting_percentage")
    private String weightingPercentage;

    @Column(name = "inkind_percentage")
    private String inkindPercentage;

    @Column(name = "climate_change_adaptation")
    private Double climateChangeAdaptation;

    @Column(name = "climate_change_mitigation")
    private Double climateChangeMitigation;

    @Column(name = "disaster_risk_reduction")
    private Double disasterRiskReduction;

    @Column(name = "disaster_risk_mitigation")
    private Double disasterRiskMitigation;

    @Column(name = "total")
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "modality")
    private Modality modality;

    @Column(name = "start_year")
    private LocalDate startYear;

    @Column(name = "end_year")
    private LocalDate endYear;

    @Column(name = "active")
    private Boolean active;

    @Lob
    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JsonIgnoreProperties("projects")
    private Country country;

    @ManyToOne
    @JsonIgnoreProperties("projects")
    private Sector sector;

    @ManyToOne
    @JsonIgnoreProperties("projects")
    private DetailedSector detailedSector;

    @OneToMany(mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Disbursement> disbursements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public Project projectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
        return this;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public Project projectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
        return this;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public Project projectType(ProjectType projectType) {
        this.projectType = projectType;
        return this;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public FundingBasis getFundingBasis() {
        return fundingBasis;
    }

    public Project fundingBasis(FundingBasis fundingBasis) {
        this.fundingBasis = fundingBasis;
        return this;
    }

    public void setFundingBasis(FundingBasis fundingBasis) {
        this.fundingBasis = fundingBasis;
    }

    public Double getTotalFundingAmount() {
        return totalFundingAmount;
    }

    public Project totalFundingAmount(Double totalFundingAmount) {
        this.totalFundingAmount = totalFundingAmount;
        return this;
    }

    public void setTotalFundingAmount(Double totalFundingAmount) {
        this.totalFundingAmount = totalFundingAmount;
    }

    public Currency getTotalFundingCurrency() {
        return totalFundingCurrency;
    }

    public Project totalFundingCurrency(Currency totalFundingCurrency) {
        this.totalFundingCurrency = totalFundingCurrency;
        return this;
    }

    public void setTotalFundingCurrency(Currency totalFundingCurrency) {
        this.totalFundingCurrency = totalFundingCurrency;
    }

    public Double getEstimatedCountryAllocation() {
        return estimatedCountryAllocation;
    }

    public Project estimatedCountryAllocation(Double estimatedCountryAllocation) {
        this.estimatedCountryAllocation = estimatedCountryAllocation;
        return this;
    }

    public void setEstimatedCountryAllocation(Double estimatedCountryAllocation) {
        this.estimatedCountryAllocation = estimatedCountryAllocation;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public Project timeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
        return this;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public String getPrincipalSource() {
        return principalSource;
    }

    public Project principalSource(String principalSource) {
        this.principalSource = principalSource;
        return this;
    }

    public void setPrincipalSource(String principalSource) {
        this.principalSource = principalSource;
    }

    public String getAdditionalSource() {
        return additionalSource;
    }

    public Project additionalSource(String additionalSource) {
        this.additionalSource = additionalSource;
        return this;
    }

    public void setAdditionalSource(String additionalSource) {
        this.additionalSource = additionalSource;
    }

    public String getMinistry() {
        return ministry;
    }

    public Project ministry(String ministry) {
        this.ministry = ministry;
        return this;
    }

    public void setMinistry(String ministry) {
        this.ministry = ministry;
    }

    public String getOtherStakeholders() {
        return otherStakeholders;
    }

    public Project otherStakeholders(String otherStakeholders) {
        this.otherStakeholders = otherStakeholders;
        return this;
    }

    public void setOtherStakeholders(String otherStakeholders) {
        this.otherStakeholders = otherStakeholders;
    }

    public Laterality getLaterality() {
        return laterality;
    }

    public Project laterality(Laterality laterality) {
        this.laterality = laterality;
        return this;
    }

    public void setLaterality(Laterality laterality) {
        this.laterality = laterality;
    }

    public Boolean isAppropriated() {
        return appropriated;
    }

    public Project appropriated(Boolean appropriated) {
        this.appropriated = appropriated;
        return this;
    }

    public void setAppropriated(Boolean appropriated) {
        this.appropriated = appropriated;
    }

    public String getWeightingPercentage() {
        return weightingPercentage;
    }

    public Project weightingPercentage(String weightingPercentage) {
        this.weightingPercentage = weightingPercentage;
        return this;
    }

    public void setWeightingPercentage(String weightingPercentage) {
        this.weightingPercentage = weightingPercentage;
    }

    public String getInkindPercentage() {
        return inkindPercentage;
    }

    public Project inkindPercentage(String inkindPercentage) {
        this.inkindPercentage = inkindPercentage;
        return this;
    }

    public void setInkindPercentage(String inkindPercentage) {
        this.inkindPercentage = inkindPercentage;
    }

    public Double getClimateChangeAdaptation() {
        return climateChangeAdaptation;
    }

    public Project climateChangeAdaptation(Double climateChangeAdaptation) {
        this.climateChangeAdaptation = climateChangeAdaptation;
        return this;
    }

    public void setClimateChangeAdaptation(Double climateChangeAdaptation) {
        this.climateChangeAdaptation = climateChangeAdaptation;
    }

    public Double getClimateChangeMitigation() {
        return climateChangeMitigation;
    }

    public Project climateChangeMitigation(Double climateChangeMitigation) {
        this.climateChangeMitigation = climateChangeMitigation;
        return this;
    }

    public void setClimateChangeMitigation(Double climateChangeMitigation) {
        this.climateChangeMitigation = climateChangeMitigation;
    }

    public Double getDisasterRiskReduction() {
        return disasterRiskReduction;
    }

    public Project disasterRiskReduction(Double disasterRiskReduction) {
        this.disasterRiskReduction = disasterRiskReduction;
        return this;
    }

    public void setDisasterRiskReduction(Double disasterRiskReduction) {
        this.disasterRiskReduction = disasterRiskReduction;
    }

    public Double getDisasterRiskMitigation() {
        return disasterRiskMitigation;
    }

    public Project disasterRiskMitigation(Double disasterRiskMitigation) {
        this.disasterRiskMitigation = disasterRiskMitigation;
        return this;
    }

    public void setDisasterRiskMitigation(Double disasterRiskMitigation) {
        this.disasterRiskMitigation = disasterRiskMitigation;
    }

    public Double getTotal() {
        return total;
    }

    public Project total(Double total) {
        this.total = total;
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Status getStatus() {
        return status;
    }

    public Project status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Modality getModality() {
        return modality;
    }

    public Project modality(Modality modality) {
        this.modality = modality;
        return this;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }

    public LocalDate getStartYear() {
        return startYear;
    }

    public Project startYear(LocalDate startYear) {
        this.startYear = startYear;
        return this;
    }

    public void setStartYear(LocalDate startYear) {
        this.startYear = startYear;
    }

    public LocalDate getEndYear() {
        return endYear;
    }

    public Project endYear(LocalDate endYear) {
        this.endYear = endYear;
        return this;
    }

    public void setEndYear(LocalDate endYear) {
        this.endYear = endYear;
    }

    public Boolean isActive() {
        return active;
    }

    public Project active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getNotes() {
        return notes;
    }

    public Project notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Country getCountry() {
        return country;
    }

    public Project country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Sector getSector() {
        return sector;
    }

    public Project sector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public DetailedSector getDetailedSector() {
        return detailedSector;
    }

    public Project detailedSector(DetailedSector detailedSector) {
        this.detailedSector = detailedSector;
        return this;
    }

    public void setDetailedSector(DetailedSector detailedSector) {
        this.detailedSector = detailedSector;
    }

    public Set<Disbursement> getDisbursements() {
        return disbursements;
    }

    public Project disbursements(Set<Disbursement> disbursements) {
        this.disbursements = disbursements;
        return this;
    }

    public Project addDisbursement(Disbursement disbursement) {
        this.disbursements.add(disbursement);
        disbursement.setProject(this);
        return this;
    }

    public Project removeDisbursement(Disbursement disbursement) {
        this.disbursements.remove(disbursement);
        disbursement.setProject(null);
        return this;
    }

    public void setDisbursements(Set<Disbursement> disbursements) {
        this.disbursements = disbursements;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Project project = (Project) o;
        if (project.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", projectTitle='" + getProjectTitle() + "'" +
            ", projectDescription='" + getProjectDescription() + "'" +
            ", projectType='" + getProjectType() + "'" +
            ", fundingBasis='" + getFundingBasis() + "'" +
            ", totalFundingAmount=" + getTotalFundingAmount() +
            ", totalFundingCurrency='" + getTotalFundingCurrency() + "'" +
            ", estimatedCountryAllocation=" + getEstimatedCountryAllocation() +
            ", timeFrame='" + getTimeFrame() + "'" +
            ", principalSource='" + getPrincipalSource() + "'" +
            ", additionalSource='" + getAdditionalSource() + "'" +
            ", ministry='" + getMinistry() + "'" +
            ", otherStakeholders='" + getOtherStakeholders() + "'" +
            ", laterality='" + getLaterality() + "'" +
            ", appropriated='" + isAppropriated() + "'" +
            ", weightingPercentage='" + getWeightingPercentage() + "'" +
            ", inkindPercentage='" + getInkindPercentage() + "'" +
            ", climateChangeAdaptation=" + getClimateChangeAdaptation() +
            ", climateChangeMitigation=" + getClimateChangeMitigation() +
            ", disasterRiskReduction=" + getDisasterRiskReduction() +
            ", disasterRiskMitigation=" + getDisasterRiskMitigation() +
            ", total=" + getTotal() +
            ", status='" + getStatus() + "'" +
            ", modality='" + getModality() + "'" +
            ", startYear='" + getStartYear() + "'" +
            ", endYear='" + getEndYear() + "'" +
            ", active='" + isActive() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
