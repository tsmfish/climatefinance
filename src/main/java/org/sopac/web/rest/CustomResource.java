package org.sopac.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopac.domain.Country;
import org.sopac.domain.DetailedSector;
import org.sopac.domain.Project;
import org.sopac.domain.Sector;
import org.sopac.domain.enumeration.*;
import org.sopac.domain.enumeration.Currency;
import org.sopac.repository.CountryRepository;
import org.sopac.repository.DetailedSectorRepository;
import org.sopac.repository.ProjectRepository;
import org.sopac.repository.SectorRepository;
import org.sopac.repository.search.ProjectSearchRepository;
import org.sopac.security.AuthoritiesConstants;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;

import com.monitorjbl.xlsx.StreamingReader;


/**
 * Custom controller
 */
@RestController
@Secured({AuthoritiesConstants.ANONYMOUS, AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN})
@RequestMapping("/api/custom")
public class CustomResource {

    private final Logger log = LoggerFactory.getLogger(CustomResource.class);

    private final CountryRepository countryRepository;

    private final SectorRepository sectorRepository;

    private final DetailedSectorRepository detailedSectorRepository;

    private final ProjectRepository projectRepository;

    private final ProjectSearchRepository projectSearchRepository;

    public CustomResource(CountryRepository countryRepository, SectorRepository sectorRepository, DetailedSectorRepository detailedSectorRepository, ProjectRepository projectRepository, ProjectSearchRepository projectSearchRepository) {
        this.countryRepository = countryRepository;
        this.sectorRepository = sectorRepository;
        this.detailedSectorRepository = detailedSectorRepository;
        this.projectRepository = projectRepository;
        this.projectSearchRepository = projectSearchRepository;
    }


    @GetMapping("/sourcecount")
    public String sourceCount() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<GenericCount> list = new ArrayList<>();

            Set<String> genericSet = new HashSet();
            for (Project p : projectRepository.findAll()) {
                genericSet.add(p.getPrincipalSource());
            }

            Map<String, Integer> m = new HashMap<>();
            for (String c : genericSet) {
                m.put(c, 0);
            }

            for (Project p : projectRepository.findAll()) {
                String c = p.getPrincipalSource();
                m.compute(c, (k, v) -> v + 1);
            }

            for (String generic : m.keySet()) {
                GenericCount gc = new GenericCount();
                gc.name = generic;
                gc.value = m.get(generic);
                if (generic != null)
                    list.add(gc);
            }
            return mapper.writeValueAsString(list);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }


    @GetMapping("/typecount")
    public String typeCount() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<GenericCount> list = new ArrayList<>();

            Set<String> genericSet = new HashSet();
            for (Project p : projectRepository.findAll()) {
                genericSet.add(p.getProjectType().name());
            }

            Map<String, Integer> m = new HashMap<>();
            for (String c : genericSet) {
                m.put(c, 0);
            }

            for (Project p : projectRepository.findAll()) {
                String c = p.getProjectType().name();
                m.compute(c, (k, v) -> v + 1);
            }

            for (String generic : m.keySet()) {
                GenericCount gc = new GenericCount();
                gc.name = generic;
                gc.value = m.get(generic);
                list.add(gc);
            }
            return mapper.writeValueAsString(list);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }

    @GetMapping("/sectorcount")
    public String sectorCount() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<GenericCount> list = new ArrayList<>();

            Set<String> sectorSet = new HashSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getSector() != null)
                    sectorSet.add(p.getSector().getName());
            }

            Map<String, Integer> m = new HashMap<>();
            for (String c : sectorSet) {
                m.put(c, 0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getSector() != null) {
                    String c = p.getSector().getName();
                    m.compute(c, (k, v) -> v + 1);
                }
            }

            for (String sector : m.keySet()) {
                GenericCount sc = new GenericCount();
                sc.name = sector;
                sc.value = m.get(sector);
                list.add(sc);
            }
            return mapper.writeValueAsString(list);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }

    @GetMapping("/validcountries")
    public String getValidCountries() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<Long, String> countries = new HashMap<>();
            for (Project p : projectRepository.findAll()) {
                if (!countries.containsKey(p.getCountry().getId())) {
                    countries.put(p.getCountry().getId(), p.getCountry().getName());
                }
            }

            ArrayList<ValidCountry> validCountries = new ArrayList<>();
            countries.forEach((id, c)->{
                ValidCountry vc = new ValidCountry();
                vc.id = id;
                vc.country = c;
                validCountries.add(vc);

            });

            return mapper.writeValueAsString(validCountries);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }

    @GetMapping("/sectorcountbycountry")
    public String sectorCountByCountry(long countryId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<GenericCount> list = new ArrayList<>();

            Set<String> sectorSet = new HashSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getCountry().getId() == countryId) {
                    if (p.getSector() != null) sectorSet.add(p.getSector().getName());
                }
            }

            Map<String, Integer> m = new HashMap<>();
            for (String c : sectorSet) {
                m.put(c, 0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getCountry().getId() == countryId) {
                    if (p.getSector() != null) {
                        String c = p.getSector().getName();
                        m.compute(c, (k, v) -> v + 1);
                    }
                }
            }

            for (String sector : m.keySet()) {
                GenericCount sc = new GenericCount();
                sc.name = sector;
                sc.value = m.get(sector);
                list.add(sc);
            }
            return mapper.writeValueAsString(list);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }


    @GetMapping("/countrycount")
    public String countryCount() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<CountryCount> list = new ArrayList<>();

            Set<String> countrySet = new HashSet();
            for (Project p : projectRepository.findAll()) {
                countrySet.add(p.getCountry().getName());
            }

            Map<String, Integer> m = new HashMap<>();
            for (String c : countrySet) {
                m.put(c, 0);
            }

            for (Project p : projectRepository.findAll()) {
                String c = p.getCountry().getName();
                m.compute(c, (k, v) -> v + 1);
            }

            for (String country : m.keySet()) {
                CountryCount cc = new CountryCount();
                cc.country = country;
                cc.count = m.get(country);
                list.add(cc);
            }

            return mapper.writeValueAsString(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }


    @GetMapping("/count")
    public String count() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String count = String.valueOf(projectRepository.count());
            return mapper.writeValueAsString(count);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }

    /**
     * GET init
     */
    @GetMapping("/init")
    public String init() {

        SortedMap<String, String> countries = new TreeMap();
        countries.put("FJ", "Fiji Islands");
        countries.put("VU", "Vanuatu");
        countries.put("PL", "Palau");
        countries.put("SB", "Solomon Islands");
        countries.put("TV", "Tuvalu");
        countries.put("TO", "Tonga");
        countries.put("WS", "Samoa");
        countries.put("MH", "Marshall Islands");

        String[] sectors = {"Agriculture", "Community", "Disaster Risk", "Education", "Energy", "Environment", "Forestry", "Governance", "Health", "ICT", "Infrastructure", "Social", "Transport", "Tourism", "Utility", "Other"};

        String[] detailedSectors = {"Conservation Biodiversity", "Disaster Risk Reduction", "Disaster Risk Mitigation", "Enabling Environment", "Renewable Energy", "Food Security", "ICT", "Livelihood Options", "Oceans and Marine", "Transport", "Waste Management", "Water and Sanitation", "Other"};


        //countries
        if (countryRepository.count() == 0) {
            for (String cc : countries.keySet()) {
                Country c = new Country();
                c.setCode(cc);
                c.setName(countries.get(cc));
                countryRepository.save(c);
            }
        }

        //sectors
        if (sectorRepository.count() == 0) {
            for (String sector : sectors) {
                Sector s = new Sector();
                s.setName(sector);
                sectorRepository.save(s);
            }
        }

        //detailedSector
        if (detailedSectorRepository.count() == 0) {
            for (String sector : detailedSectors) {
                DetailedSector s = new DetailedSector();
                s.setName(sector);
                detailedSectorRepository.save(s);
            }
        }

        if (projectRepository.count() == 0) {
            InputStream is = null;
            String path = "/home/sachin/Documents/ClimateFinance/";

            //solomons
            try {
                is = new FileInputStream(new File(path + "sb.xlsx"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Workbook workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(is);
            Sheet sheet = workbook.getSheetAt(0);
            int r_index = 0;
            for (Row r : sheet) {
                if (r_index != 0) {
                    int c_index = 0;
                    Project p = new Project();
                    p.setCountry(countryRepository.findAll().get(3));
                    p.setActive(true);

                    for (Cell c : r) {
                        if (c_index == 0) p.setProjectTitle(c.getStringCellValue());
                        if (c_index == 1) p.setProjectDescription(c.getStringCellValue());
                        if (p.getProjectDescription() == null || p.getProjectDescription().equals(""))
                            p.setProjectDescription(p.getProjectTitle());

                        if (c_index == 2) p.setWeightingPercentage(c.getStringCellValue());

                        if (c_index == 5) {
                            String st = c.getStringCellValue().toLowerCase();
                            for (Sector s : sectorRepository.findAll()) {
                                if (st.contains(s.getName().toLowerCase())) p.setSector(s);
                            }
                        }

                        c_index++;
                    }
                    if (p.getProjectTitle() != null) {
                        if (!p.getProjectTitle().equals("")) projectRepository.save(p);
                    }
                }
                r_index++;
            }

            //tonga
            try {
                is = new FileInputStream(new File(path + "to.xlsx"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(is);
            sheet = workbook.getSheetAt(0);
            r_index = 0;
            for (Row r : sheet) {
                if (r_index != 0) {
                    int c_index = 0;
                    Project p = new Project();
                    p.setCountry(countryRepository.findAll().get(4));
                    p.setActive(true);

                    for (Cell c : r) {
                        if (c_index == 1) p.setProjectTitle(c.getStringCellValue());
                        if (p.getProjectDescription() == null || p.getProjectDescription().equals(""))
                            p.setProjectDescription(p.getProjectTitle());

                        if (c_index == 3) p.setPrincipalSource(c.getStringCellValue());
                        p.setWeightingPercentage("0.8");
                        p.setFundingBasis(FundingBasis.NATIONAL);

                        if (c_index == 4) p.setAdditionalSource(c.getStringCellValue());

                        if (c_index == 0) p.setMinistry(c.getStringCellValue().toUpperCase());

                        if (c_index == 5) {
                            p.setProjectType(ProjectType.CCA);
                            if (c.getStringCellValue().equals("CCM")) p.setProjectType(ProjectType.CCM);
                            if (c.getStringCellValue().equals("DRR")) p.setProjectType(ProjectType.DRR);
                            if (c.getStringCellValue().equals("DRM")) p.setProjectType(ProjectType.DRM);
                            if (c.getStringCellValue().toLowerCase().equals("enabling"))
                                p.setProjectType(ProjectType.ENABLING);
                        }

                        if (c_index == 7) {
                            String amt = c.getStringCellValue().trim();
                            if (amt.equals("-")) amt = "0";
                            if (amt.equals("")) amt = "0";
                            if (amt.startsWith("-")) amt = "0";
                            amt = amt.replaceAll(",", "").trim();
                            p.setTotalFundingAmount(Double.valueOf(amt));
                            p.setTotalFundingCurrency(Currency.USD);
                        }

                        if (c_index == 6) {
                            String st = c.getStringCellValue().toLowerCase();
                            for (Sector s : sectorRepository.findAll()) {
                                if (st.contains(s.getName().toLowerCase())) p.setSector(s);
                            }
                        }

                        c_index++;
                    }
                    if (p.getProjectTitle() != null) {
                        if (!p.getProjectTitle().equals("")) projectRepository.save(p);
                    }
                }
                r_index++;
            }


            //rmi
            try {
                is = new FileInputStream(new File(path + "rmi.xlsx"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(is);
            sheet = workbook.getSheetAt(0);
            r_index = 0;
            for (Row r : sheet) {
                if (r_index != 0) {
                    int c_index = 0;
                    Project p = new Project();
                    p.setCountry(countryRepository.findAll().get(1));
                    p.setActive(true);

                    for (Cell c : r) {
                        if (c_index == 0) p.setProjectTitle(c.getStringCellValue());
                        if (c_index == 1) p.setProjectDescription(c.getStringCellValue());
                        if (p.getProjectDescription() == null || p.getProjectDescription().equals(""))
                            p.setProjectDescription(p.getProjectTitle());

                        if (c_index == 7) p.setPrincipalSource(c.getStringCellValue());

                        if (c_index == 12) {
                            String st = c.getStringCellValue().toLowerCase();
                            for (Sector s : sectorRepository.findAll()) {
                                if (st.contains(s.getName().toLowerCase())) p.setSector(s);
                            }
                        }

                        if (c_index == 13) {
                            String st = c.getStringCellValue().toLowerCase();
                            for (DetailedSector s : detailedSectorRepository.findAll()) {
                                if (st.contains(s.getName().toLowerCase())) p.setDetailedSector(s);
                            }
                        }

                        c_index++;
                    }
                    if (p.getProjectTitle() != null) {
                        if (!p.getProjectTitle().equals("")) projectRepository.save(p);
                    }
                }
                r_index++;
            }


            //palau
            try {
                is = new FileInputStream(new File(path + "pl.xlsx"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(is);
            sheet = workbook.getSheetAt(0);
            r_index = 0;
            for (Row r : sheet) {
                if (r_index != 0) {
                    int c_index = 0;
                    Project p = new Project();
                    p.setCountry(countryRepository.findAll().get(2));
                    p.setActive(true);

                    for (Cell c : r) {
                        if (c_index == 1) p.setProjectTitle(c.getStringCellValue());
                        if (c_index == 2) p.setProjectDescription(c.getStringCellValue());

                        if (c_index == 3) {
                            p.setFundingBasis(FundingBasis.REGIONAL);
                            if (c.getStringCellValue().trim().toLowerCase().equals("national"))
                                p.setFundingBasis(FundingBasis.NATIONAL);
                        }

                        if (c_index == 4) {
                            String tmp = c.getStringCellValue();
                            tmp = tmp.replaceAll(",", "").trim();

                            if (!tmp.equals("")) {
                                if (StringUtils.isNumeric(tmp.substring(0, 1))) {
                                    p.setTotalFundingAmount(Double.valueOf(tmp.trim()));
                                } else {
                                    tmp = tmp.substring(3, tmp.length()).trim();
                                    p.setTotalFundingAmount(Double.valueOf(tmp.trim()));
                                }
                            }
                        }

                        if (c_index == 4) {
                            p.setTotalFundingCurrency(Currency.USD);
                            String tmp = c.getStringCellValue();
                            tmp = tmp.replaceAll(",", "").trim();
                            if (!tmp.equals("")) {
                                if (!StringUtils.isNumeric(tmp.substring(0, 1))) {
                                    tmp = tmp.substring(0, 3).trim();
                                    //System.err.println("CUR:" + tmp);
                                    if (tmp.toLowerCase().equals("jpy")) p.setTotalFundingCurrency(Currency.JPY);
                                    if (tmp.toLowerCase().equals("eur")) p.setTotalFundingCurrency(Currency.EUR);
                                    if (tmp.toLowerCase().equals("aud")) p.setTotalFundingCurrency(Currency.AUD);
                                    if (tmp.toLowerCase().equals("fjd")) p.setTotalFundingCurrency(Currency.FJD);
                                }
                            }
                        }

                        if (c_index == 5) {
                            if (!c.getStringCellValue().trim().equals(""))
                                p.setEstimatedCountryAllocation(Double.valueOf(c.getStringCellValue().replaceAll(",", "").trim()));
                        }

                        if (c_index == 6) {
                            p.setTimeFrame(c.getStringCellValue().trim());
                        }

                        if (c_index == 8) {
                            p.setPrincipalSource(c.getStringCellValue().trim());
                        }

                        if (c_index == 9) {
                            p.setAdditionalSource(c.getStringCellValue().trim());
                        }
                        if (c_index == 10) {
                            p.setLaterality(Laterality.BILATERAL);
                            if (c.getStringCellValue().toLowerCase().trim().startsWith("multi"))
                                p.setLaterality(Laterality.MULTILATERAL);
                        }
                        if (c_index == 11) {
                            p.setMinistry(c.getStringCellValue().trim());
                        }
                        if (c_index == 12) {
                            p.setOtherStakeholders(c.getStringCellValue().trim());
                        }
                        if (c_index == 13) {
                            String tmp = c.getStringCellValue().toLowerCase();
                            for (Sector s : sectorRepository.findAll()) {
                                if (s.getName().toLowerCase().equals(tmp)) p.setSector(s);
                            }
                        }
                        if (c_index == 14) {
                            String tmp = c.getStringCellValue().toLowerCase();
                            for (DetailedSector s : detailedSectorRepository.findAll()) {
                                if (s.getName().toLowerCase().equals(tmp)) p.setDetailedSector(s);
                            }
                        }
                        if (c_index == 15) {
                            p.setWeightingPercentage(c.getStringCellValue().toString());
                        }
                        if (c_index == 16) {
                            if (!c.getStringCellValue().equals(""))
                                p.setInkindPercentage(c.getStringCellValue().trim());
                        }
                        if (c_index == 17) {
                            try {
                                if (!c.getStringCellValue().trim().equals(""))
                                    p.setClimateChangeAdaptation(Double.valueOf(c.getStringCellValue().replaceAll(",", "").trim()));
                            } catch (Exception ex) {
                                System.err.println("ERR: " + c.getStringCellValue());
                                System.err.println(ex.getMessage());
                            }
                        }
                        if (c_index == 18) {
                            try {
                                if (!c.getStringCellValue().trim().equals(""))
                                    p.setClimateChangeMitigation(Double.valueOf(c.getStringCellValue().replaceAll(",", "").trim()));
                            } catch (Exception ex) {
                                System.err.println("ERR: " + c.getStringCellValue());
                                System.err.println(ex.getMessage());
                            }
                        }
                        if (c_index == 19) {
                            try {
                                if (!c.getStringCellValue().trim().equals(""))
                                    p.setDisasterRiskReduction(Double.valueOf(c.getStringCellValue().replaceAll(",", "").trim()));
                            } catch (Exception ex) {
                                System.err.println("ERR: " + c.getStringCellValue());
                                System.err.println(ex.getMessage());
                            }
                        }
                        if (c_index == 20) {
                            try {
                                if (!c.getStringCellValue().trim().equals(""))
                                    p.setDisasterRiskMitigation(Double.valueOf(c.getStringCellValue().replaceAll(",", "").trim()));
                            } catch (Exception ex) {
                                System.err.println("ERR: " + c.getStringCellValue());
                                System.err.println(ex.getMessage());
                            }
                        }
                        if (c_index == 21) {
                            try {
                                if (!c.getStringCellValue().trim().equals(""))
                                    p.setTotal(Double.valueOf(c.getStringCellValue().replaceAll(",", "").trim()));
                            } catch (Exception ex) {
                                System.err.println("ERR: " + c.getStringCellValue());
                                System.err.println(ex.getMessage());
                            }
                        }

                        //if (c_index == 22) {
                        //    p.setReflectedInBudget(Reflected.NO);
                        //    if (c.getStringCellValue().trim().toLowerCase().equals("yes")) {
                        //        p.setReflectedInBudget(Reflected.YES);
                        //    }
                        //}

                        if (c_index == 23) {
                            p.setStatus(Status.CURRENT);
                            if (c.getStringCellValue().trim().toLowerCase().startsWith("pipe"))
                                p.setStatus(Status.PIPELINE);
                            if (c.getStringCellValue().trim().toLowerCase().startsWith("comp"))
                                p.setStatus(Status.COMPLETED);
                        }

                        if (c_index == 24) {
                            p.setNotes(c.getStringCellValue());
                        }


                        c_index++;
                    }
                    if (p.getProjectTitle() != null) {
                        if (!p.getProjectTitle().equals("")) projectRepository.save(p);
                    }
                }
                r_index++;
            }


            //vanuatu
            try {
                is = new FileInputStream(new File(path + "vu.xlsx"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(is);
            sheet = workbook.getSheetAt(0);
            r_index = 0;
            for (Row r : sheet) {
                if (r_index != 0) {
                    int c_index = 0;
                    Project p = new Project();
                    p.setCountry(countryRepository.findAll().get(6));
                    p.setActive(true);

                    for (Cell c : r) {
                        if (c_index == 1) p.setProjectTitle(c.getStringCellValue());
                        if (c_index == 2) p.setProjectDescription(c.getStringCellValue());
                        if (c_index == 3) p.setWeightingPercentage(c.getStringCellValue());
                        if (c_index == 4) {
                            p.setProjectType(ProjectType.CCA);
                            if (c.getStringCellValue().equals("CCM")) p.setProjectType(ProjectType.CCM);
                            if (c.getStringCellValue().equals("DRR")) p.setProjectType(ProjectType.DRR);
                            if (c.getStringCellValue().equals("DRM")) p.setProjectType(ProjectType.DRM);
                            if (c.getStringCellValue().toLowerCase().equals("enabling"))
                                p.setProjectType(ProjectType.ENABLING);
                        }
                        if (c_index == 5) {
                            String tmp = c.getStringCellValue().toLowerCase();
                            for (Sector s : sectorRepository.findAll()) {
                                if (s.getName().toLowerCase().equals(tmp)) p.setSector(s);
                            }
                        }
                        if (c_index == 6) {
                            p.setMinistry(c.getStringCellValue());
                        }
                        if (c_index == 7) {
                            p.setOtherStakeholders("None");

                        }
                        if (c_index == 8) {
                            p.setPrincipalSource(c.getStringCellValue());

                        }
                        if (c_index == 9) {
                            p.setAdditionalSource(c.getStringCellValue());

                        }
                        if (c_index == 10) {
                            p.setLaterality(Laterality.BILATERAL);
                            if (c.getStringCellValue().toLowerCase().startsWith("multi"))
                                p.setLaterality(Laterality.MULTILATERAL);

                        }
                        if (c_index == 11) {
                            p.setFundingBasis(FundingBasis.NATIONAL);
                            if (c.getStringCellValue().toLowerCase().startsWith("region"))
                                p.setFundingBasis(FundingBasis.REGIONAL);

                        }
                        if (c_index == 13) {
                            p.setAppropriated(true);
                            if (c.getStringCellValue().toLowerCase().trim().equals("no")) p.setAppropriated(false);

                        }
                        if (c_index == 14) {
                            p.setNotes(c.getStringCellValue());


                        }
                        if (c_index == 15) {
                            p.setTotalFundingCurrency(Currency.VT);
                            p.setTotalFundingAmount(Double.valueOf(c.getStringCellValue().replaceAll(",", "").trim()));

                        }
                        if (c_index == 16) {
                            p.setEstimatedCountryAllocation(Double.valueOf(c.getStringCellValue().replaceAll(",", "").trim()));
                        }


                        c_index++;
                    }
                    if (p.getProjectTitle() != null) {
                        if (!p.getProjectTitle().equals("")) projectRepository.save(p);
                    }

                }
                r_index++;
            }


        }

        return "\r\nInit Finished.\r\n";
    }

    /**
     * GET clean
     */
    @GetMapping("/clean")
    public String clean() {

        for (Project p : projectRepository.findAll()) {
            if (p.getProjectType() == null || p.getProjectType().equals("")) {
                p.setProjectType(ProjectType.CCA);
                projectRepository.save(p);
            }
        }

        return "cleaned";
    }

}
