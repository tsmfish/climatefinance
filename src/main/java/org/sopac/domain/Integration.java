package org.sopac.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import org.sopac.domain.enumeration.Platform;

import org.sopac.domain.enumeration.Schedule;

/**
 * A Integration.
 */
@Entity
@Table(name = "integration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "integration")
public class Integration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform")
    private Platform platform;

    @Column(name = "driver")
    private String driver;

    @Column(name = "username")
    private String username;

    @Column(name = "jhi_password")
    private String password;

    @Column(name = "flat_folder")
    private String flatFolder;

    @Column(name = "flat_file")
    private String flatFile;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule")
    private Schedule schedule;

    @Column(name = "active")
    private Boolean active;

    @Lob
    @Column(name = "mapping")
    private String mapping;

    @ManyToOne
    private Country country;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Platform getPlatform() {
        return platform;
    }

    public Integration platform(Platform platform) {
        this.platform = platform;
        return this;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getDriver() {
        return driver;
    }

    public Integration driver(String driver) {
        this.driver = driver;
        return this;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUsername() {
        return username;
    }

    public Integration username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public Integration password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFlatFolder() {
        return flatFolder;
    }

    public Integration flatFolder(String flatFolder) {
        this.flatFolder = flatFolder;
        return this;
    }

    public void setFlatFolder(String flatFolder) {
        this.flatFolder = flatFolder;
    }

    public String getFlatFile() {
        return flatFile;
    }

    public Integration flatFile(String flatFile) {
        this.flatFile = flatFile;
        return this;
    }

    public void setFlatFile(String flatFile) {
        this.flatFile = flatFile;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Integration schedule(Schedule schedule) {
        this.schedule = schedule;
        return this;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Boolean isActive() {
        return active;
    }

    public Integration active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getMapping() {
        return mapping;
    }

    public Integration mapping(String mapping) {
        this.mapping = mapping;
        return this;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public Country getCountry() {
        return country;
    }

    public Integration country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
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
        Integration integration = (Integration) o;
        if (integration.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), integration.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Integration{" +
            "id=" + getId() +
            ", platform='" + getPlatform() + "'" +
            ", driver='" + getDriver() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", flatFolder='" + getFlatFolder() + "'" +
            ", flatFile='" + getFlatFile() + "'" +
            ", schedule='" + getSchedule() + "'" +
            ", active='" + isActive() + "'" +
            ", mapping='" + getMapping() + "'" +
            "}";
    }
}
