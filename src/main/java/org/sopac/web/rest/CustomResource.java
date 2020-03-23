package org.sopac.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopac.domain.*;
import org.sopac.domain.enumeration.Currency;
import org.sopac.domain.enumeration.*;
import org.sopac.repository.*;
import org.sopac.repository.search.DisbursementSearchRepository;
import org.sopac.repository.search.ProjectSearchRepository;
import org.sopac.security.AuthoritiesConstants;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

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

    private final DisbursementRepository disbursementRepository;

    private final DisbursementSearchRepository disbursementSearchRepository;

    private final MetodologyRepository metodologyRepository;

    public CustomResource(CountryRepository countryRepository, SectorRepository sectorRepository, DetailedSectorRepository detailedSectorRepository, ProjectRepository projectRepository, ProjectSearchRepository projectSearchRepository, DisbursementRepository disbursementRepository, DisbursementSearchRepository disbursementSearchRepository, MetodologyRepository metodologyRepository) {
        this.countryRepository = countryRepository;
        this.sectorRepository = sectorRepository;
        this.detailedSectorRepository = detailedSectorRepository;
        this.projectRepository = projectRepository;
        this.projectSearchRepository = projectSearchRepository;
        this.disbursementRepository = disbursementRepository;
        this.disbursementSearchRepository = disbursementSearchRepository;
        this.metodologyRepository = metodologyRepository;
    }


    @GetMapping("/reindex")
    public boolean reIndex() {
        projectRepository.findAll().forEach(p -> projectSearchRepository.index(p));
        disbursementRepository.findAll().forEach(d -> disbursementSearchRepository.index(d));
        return true;
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


    @GetMapping("/sectorvalue")
    public String sectorValue() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<ValueCount> list = new ArrayList<>();

            Set<String> sectorSet = new HashSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getSector() != null)
                    sectorSet.add(p.getSector().getName());
            }

            Map<String, Double> m = new HashMap<>();
            for (String c : sectorSet) {
                m.put(c, 0.0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getSector() != null) {
                    String c = p.getSector().getName();
                    if (p.getTotal() != null) m.compute(c, (k, v) -> v + (p.getTotal() / 1000000));
                }
            }

            for (String sector : m.keySet()) {
                ValueCount vc = new ValueCount();
                vc.name = sector;
                vc.value = m.get(sector);
                list.add(vc);
            }
            return mapper.writeValueAsString(list);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }

    @GetMapping("/ministrycount")
    public String ministryCount() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<ValueCount> list = new ArrayList<>();

            Set<String> ministrySet = new HashSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getMinistry() != null)
                    ministrySet.add(p.getMinistry());
            }

            Map<String, Integer> m = new HashMap<>();
            for (String c : ministrySet) {
                m.put(c, 0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getMinistry() != null) {
                    String c = p.getMinistry();
                    m.compute(c, (k, v) -> v + 1);
                }
            }

            for (String ministry : m.keySet()) {
                ValueCount vc = new ValueCount();
                vc.name = ministry;
                vc.value = m.get(ministry);
                list.add(vc);
            }
            return mapper.writeValueAsString(list);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }

    @GetMapping("/ministrycountbycountry")
    public String ministryCountByCountry(long countryId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<ValueCount> list = new ArrayList<>();

            Set<String> ministrySet = new HashSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getCountry().getId() == countryId) {
                    if (p.getMinistry() != null) {
                        ministrySet.add(p.getMinistry());
                    }
                }
            }

            Map<String, Integer> m = new HashMap<>();
            for (String c : ministrySet) {
                m.put(c, 0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getCountry().getId() == countryId) {
                    if (p.getMinistry() != null) {
                        String c = p.getMinistry();
                        m.compute(c, (k, v) -> v + 1);
                    }
                }
            }

            for (String ministry : m.keySet()) {
                ValueCount vc = new ValueCount();
                vc.name = ministry;
                vc.value = m.get(ministry);
                list.add(vc);
            }
            return mapper.writeValueAsString(list);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }


    @GetMapping("/sourcevalue")
    public String sourceValue() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<ValueCount> list = new ArrayList<>();

            SortedSet<String> sourceSet = new TreeSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getPrincipalSource() != null)
                    sourceSet.add(p.getPrincipalSource());
            }


            SortedMap<String, Double> m = new TreeMap<>();
            for (String c : sourceSet) {
                m.put(c, 0.0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getPrincipalSource() != null) {
                    String c = p.getPrincipalSource();
                    if (p.getTotal() != null) m.compute(c, (k, v) -> v + (p.getTotal() / 1000000));
                }
            }

            for (String source : m.keySet()) {
                ValueCount vc = new ValueCount();
                vc.name = source;
                vc.value = m.get(source);
                list.add(vc);
            }
            return mapper.writeValueAsString(list);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }

    @GetMapping("/sourcevaluebycountry")
    public String sourceValueByCountry(long countryId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<ValueCount> list = new ArrayList<>();

            SortedSet<String> sourceSet = new TreeSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getCountry().getId() == countryId) {
                    if (p.getPrincipalSource() != null)
                        sourceSet.add(p.getPrincipalSource());
                }
            }

            SortedMap<String, Double> m = new TreeMap<>();
            for (String c : sourceSet) {
                m.put(c, 0.0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getCountry().getId() == countryId) {
                    if (p.getPrincipalSource() != null) {
                        String c = p.getPrincipalSource();
                        if (p.getTotal() != null) m.compute(c, (k, v) -> v + (p.getTotal() / 1000000));
                    }
                }
            }

            for (String source : m.keySet()) {
                ValueCount vc = new ValueCount();
                vc.name = source;
                vc.value = m.get(source);
                list.add(vc);
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
            countries.forEach((id, c) -> {
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

    //project status
    @GetMapping("/projectstatuscount")
    public String projectStatusCount() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<GenericCount> list = new ArrayList<>();

            Set<String> set = new HashSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getStatus() != null) {
                    set.add(p.getStatus().name());
                }
            }

            Map<String, Integer> m = new HashMap<>();
            for (String p : set) {
                m.put(p, 0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getStatus() != null) {
                    String t = p.getStatus().name();
                    m.compute(t, (k, v) -> v + 1);
                }
            }

            for (String p : m.keySet()) {
                GenericCount gc = new GenericCount();
                gc.name = p;
                gc.value = m.get(p);
                list.add(gc);
            }

            return mapper.writeValueAsString(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }

    //detailed sector
    @GetMapping("/detailedsectorcount")
    public String detailedSectorCount() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<GenericCount> list = new ArrayList<>();

            Set<String> set = new HashSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getDetailedSector() != null) {
                    set.add(p.getDetailedSector().getName());
                    //System.out.println("P -> " + p.getProjectType().name());
                }
            }

            Map<String, Integer> m = new HashMap<>();
            for (String p : set) {
                m.put(p, 0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getDetailedSector() != null) {
                    String t = p.getDetailedSector().getName();
                    m.compute(t, (k, v) -> v + 1);
                }
            }

            for (String p : m.keySet()) {
                GenericCount gc = new GenericCount();
                gc.name = p;
                gc.value = m.get(p);
                list.add(gc);
            }

            return mapper.writeValueAsString(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }


    @GetMapping("/projecttypecount")
    public String projectTypeCount() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<GenericCount> list = new ArrayList<>();

            Set<String> set = new HashSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getProjectType() != null) {
                    set.add(p.getProjectType().name());
                    //System.out.println("P -> " + p.getProjectType().name());
                }
            }

            Map<String, Integer> m = new HashMap<>();
            for (String p : set) {
                m.put(p, 0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getProjectType() != null) {
                    String t = p.getProjectType().name();
                    m.compute(t, (k, v) -> v + 1);
                }
            }

            for (String p : m.keySet()) {
                GenericCount gc = new GenericCount();
                gc.name = p;
                gc.value = m.get(p);
                list.add(gc);
            }

            return mapper.writeValueAsString(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }


    @GetMapping("/currencyconvert")
    public String currencyConvert() {
        //total funding amount -> total
        Map<String, Double> rates = new HashMap<String, Double>();
        rates.put("SBD", 0.125020);
        rates.put("VT", 0.00874521);
        rates.put("EUR", 1.13322);
        rates.put("JPY", 0.00904694);
        rates.put("AUD", 0.710004);
        rates.put("USD", 1.0);
        rates.put("FJD", 0.468496);

        for (Project p : projectRepository.findAll()) {
            if (p.getTotalFundingAmount() != null && p.getTotalFundingCurrency() != null) {
                System.out.println(p.getTotalFundingCurrency().name());
                double total = p.getTotalFundingAmount() * rates.get(p.getTotalFundingCurrency().name());
                p.setTotal(total);
                projectRepository.save(p);
            }
        }
        return "null";
    }


    @GetMapping("/countryvaluechart")
    public String countryValueChart() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<ValueCount> list = new ArrayList<>();

            Set<String> countrySet = new HashSet();
            for (Project p : projectRepository.findAll()) {
                countrySet.add(p.getCountry().getName());
            }

            Map<String, Double> m = new HashMap<>();

            for (String c : countrySet) {
                //add dollar value
                double dollarValue = 0;
                for (Project p : projectRepository.findAll()) {
                    if (p.getCountry().getName().equals(c)) {
                        if (p.getTotal() != null) dollarValue = dollarValue + p.getTotal();
                    }
                }
                m.put(c, dollarValue / 1000000); //millions
            }

            for (String country : m.keySet()) {
                ValueCount cc = new ValueCount();
                cc.name = country;
                cc.value = m.get(country);
                list.add(cc);
            }

            return mapper.writeValueAsString(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }

    @GetMapping("/countryvaluechartbycountry")
    public String countryValueChartByCountry(long countryId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<ValueCount> list = new ArrayList<>();

            Set<String> countrySet = new HashSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getCountry().getId() == countryId)
                    countrySet.add(p.getCountry().getName());
            }

            Map<String, Double> m = new HashMap<>();

            for (String c : countrySet) {
                //add dollar value
                double dollarValue = 0;
                for (Project p : projectRepository.findAll()) {
                    if (p.getCountry().getName().equals(c)) {
                        if (p.getTotal() != null) dollarValue = dollarValue + p.getTotal();
                    }
                }
                m.put(c, dollarValue / 1000000); //millions
            }

            for (String country : m.keySet()) {
                ValueCount cc = new ValueCount();
                cc.name = country;
                cc.value = m.get(country);
                list.add(cc);
            }

            return mapper.writeValueAsString(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }


    @GetMapping("/countrycountchart")
    public String countryCountChart() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<GenericCount> list = new ArrayList<>();

            Set<String> countrySet = new HashSet();
            for (Project p : projectRepository.findAll()) {
                countrySet.add(p.getCountry().getName());
            }

            Map<String, Integer> m = new HashMap<>();

            for (String c : countrySet) {
                m.put(c, 0);
            }

            /*
            for (String c : countrySet) {
                //add dollar value
                double dollarValue = 0;
                int count = 0;
                for (Project p : projectRepository.findAll()) {
                    if (p.getCountry().getName().equals(c)) {
                        if (p.getTotal() != null) dollarValue = dollarValue + p.getTotal();
                        count++;
                    }
                }

                m.put(c + " : USD" + String.valueOf(dollarValue), count);
            }
            */

            for (Project p : projectRepository.findAll()) {
                String c = p.getCountry().getName();
                m.compute(c, (k, v) -> v + 1);
            }

            for (String country : m.keySet()) {
                GenericCount cc = new GenericCount();
                cc.name = country;
                cc.value = m.get(country);
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


    //BY COUNTRY

    @GetMapping("/countrycountchartbycountry")
    public String countryCountChartByCountry(long countryId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<GenericCount> list = new ArrayList<>();

            Set<String> countrySet = new HashSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getCountry().getId() == countryId)
                    countrySet.add(p.getCountry().getName());
            }

            Map<String, Integer> m = new HashMap<>();
            for (String c : countrySet) {
                m.put(c, 0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getCountry().getId() == countryId) {
                    String c = p.getCountry().getName();
                    m.compute(c, (k, v) -> v + 1);
                }
            }

            for (String country : m.keySet()) {
                GenericCount cc = new GenericCount();
                cc.name = country;
                cc.value = m.get(country);
                list.add(cc);
            }

            return mapper.writeValueAsString(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }

    @GetMapping("/countbycountry")
    public String countByCountry(long countryId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            //String count = String.valueOf(projectRepository.count());

            int c = 0;
            for (Project p : projectRepository.findAll()) {
                if (p.getCountry().getId() == countryId) c++;
            }
            String count = String.valueOf(c);
            return mapper.writeValueAsString(count);
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

    @GetMapping("/sectorvaluebycountry")
    public String sectorValueByCountry(long countryId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<ValueCount> list = new ArrayList<>();

            Set<String> sectorSet = new HashSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getCountry().getId() == countryId) {
                    if (p.getSector() != null) sectorSet.add(p.getSector().getName());
                }
            }

            Map<String, Double> m = new HashMap<>();
            for (String c : sectorSet) {
                m.put(c, 0.0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getCountry().getId() == countryId) {
                    if (p.getSector() != null) {
                        String c = p.getSector().getName();
                        if (p.getTotal() != null) m.compute(c, (k, v) -> v + (p.getTotal() / 1000000));
                    }
                }
            }

            for (String sector : m.keySet()) {
                ValueCount vc = new ValueCount();
                vc.name = sector;
                vc.value = m.get(sector);
                list.add(vc);
            }
            return mapper.writeValueAsString(list);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }

    @GetMapping("/projecttypecountbycountry")
    public String projectTypeCountByCountry(long countryId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<GenericCount> list = new ArrayList<>();

            Set<String> set = new HashSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getProjectType() != null) {
                    if (p.getCountry().getId() == countryId)
                        set.add(p.getProjectType().name());
                }
            }

            Map<String, Integer> m = new HashMap<>();
            for (String p : set) {
                m.put(p, 0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getProjectType() != null) {
                    if (p.getCountry().getId() == countryId) {
                        String t = p.getProjectType().name();
                        m.compute(t, (k, v) -> v + 1);
                    }
                }
            }

            for (String p : m.keySet()) {
                GenericCount gc = new GenericCount();
                gc.name = p;
                gc.value = m.get(p);
                list.add(gc);
            }

            return mapper.writeValueAsString(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }

    @GetMapping("/detailedsectorcountbycountry")
    public String detailedSectorCountByCountry(long countryId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<GenericCount> list = new ArrayList<>();

            Set<String> set = new HashSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getDetailedSector() != null) {
                    if (p.getCountry().getId() == countryId)
                        set.add(p.getDetailedSector().getName());
                }
            }

            Map<String, Integer> m = new HashMap<>();
            for (String p : set) {
                m.put(p, 0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getDetailedSector() != null) {
                    if (p.getCountry().getId() == countryId) {
                        String t = p.getDetailedSector().getName();
                        m.compute(t, (k, v) -> v + 1);
                    }
                }
            }

            for (String p : m.keySet()) {
                GenericCount gc = new GenericCount();
                gc.name = p;
                gc.value = m.get(p);
                list.add(gc);
            }

            return mapper.writeValueAsString(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }

    @GetMapping("/projectstatuscountbycountry")
    public String projectStatusCountByCountry(long countryId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<GenericCount> list = new ArrayList<>();

            Set<String> set = new HashSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getStatus() != null) {
                    if (p.getCountry().getId() == countryId)
                        set.add(p.getStatus().name());
                }
            }

            Map<String, Integer> m = new HashMap<>();
            for (String p : set) {
                m.put(p, 0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getStatus() != null) {
                    if (p.getCountry().getId() == countryId) {
                        String t = p.getStatus().name();
                        m.compute(t, (k, v) -> v + 1);
                    }
                }
            }

            for (String p : m.keySet()) {
                GenericCount gc = new GenericCount();
                gc.name = p;
                gc.value = m.get(p);
                list.add(gc);
            }

            return mapper.writeValueAsString(list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }

    @GetMapping("/sourcecountbycountry")
    public String sourceCount(long countryId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<GenericCount> list = new ArrayList<>();

            Set<String> genericSet = new HashSet();
            for (Project p : projectRepository.findAll()) {
                if (p.getCountry().getId() == countryId)
                    genericSet.add(p.getPrincipalSource());
            }

            Map<String, Integer> m = new HashMap<>();
            for (String c : genericSet) {
                m.put(c, 0);
            }

            for (Project p : projectRepository.findAll()) {
                if (p.getCountry().getId() == countryId) {
                    String c = p.getPrincipalSource();
                    m.compute(c, (k, v) -> v + 1);
                }
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

        String[] sectors = {"Agriculture", "Community", "Disaster Risk", "Education", "Energy", "Environment", "Forestry", "Fisheries", "Governance", "Health", "ICT", "Infrastructure", "Social", "Transport", "Tourism", "Utility", "Other"};

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
                        if (c_index == 1) {
                            if (c.getStringCellValue() != null) p.setProjectDescription(c.getStringCellValue());
                        }
                        if (p.getProjectDescription() == null || p.getProjectDescription().equals(""))
                            p.setProjectDescription(p.getProjectTitle());

                        if (c_index == 2) {
                            if (c.getStringCellValue() != null)
                                p.setWeightingPercentage(c.getStringCellValue());
                        }

                        if (c_index == 3) {
                            p.setProjectType(ProjectType.CCA);
                            if (c.getStringCellValue().equals("CCM")) p.setProjectType(ProjectType.CCM);
                            if (c.getStringCellValue().equals("DRR")) p.setProjectType(ProjectType.DRR);
                            if (c.getStringCellValue().equals("DRM")) p.setProjectType(ProjectType.DRM);
                            if (c.getStringCellValue().toLowerCase().equals("enabling"))
                                p.setProjectType(ProjectType.ENABLING);
                        }

                        if (c_index == 5) {
                            if (c.getStringCellValue() != null) {
                                String st = c.getStringCellValue().toLowerCase().trim();
                                for (Sector s : sectorRepository.findAll()) {
                                    if (st.contains(s.getName().toLowerCase())) p.setSector(s);
                                    if (s.getName().toLowerCase().contains(st.toLowerCase())) p.setSector(s);
                                }
                            }
                        }

                        if (c_index == 6) {
                            if (c.getStringCellValue() != null) p.setMinistry(c.getStringCellValue());
                        }

                        if (c_index == 8) {
                            if (c.getStringCellValue() != null) p.setPrincipalSource(c.getStringCellValue());
                        }


                        if (c_index == 10) {
                            p.setLaterality(Laterality.BILATERAL);
                            if (c.getStringCellValue().toLowerCase().startsWith("multi"))
                                p.setLaterality(Laterality.MULTILATERAL);

                        }

                        if (c_index == 11) {
                            if (c.getStringCellValue() != null) p.setAdditionalSource(c.getStringCellValue());
                        }

                        if (c_index == 13) {
                            p.setFundingBasis(FundingBasis.NATIONAL);
                            if (c.getStringCellValue().toLowerCase().startsWith("region"))
                                p.setFundingBasis(FundingBasis.REGIONAL);

                        }

                        if (c_index == 14) {
                            if (c.getStringCellValue() != null) p.setTimeFrame(c.getStringCellValue());
                        }

                        if (c_index == 15) {
                            p.setModality(Modality.OFF_BUDGET);
                            if (c.getStringCellValue().toLowerCase().trim().equals("yes"))
                                p.setModality(Modality.ON_BUDGET);
                        }

                        p.setAppropriated(false);


                        p.setTotalFundingCurrency(Currency.SBD);

                        if (c_index == 17) {
                            if (c.getStringCellValue() != null) p.setNotes(c.getStringCellValue());
                        }

                        //System.out.println(p.getProjectTitle());

                        //total cost
                        if (c_index == 19) {
                            if (c.getStringCellValue() != null) {
                                String amt = c.getStringCellValue().trim();
                                if (amt.length() != 0) {
                                    try {
                                        p.setTotal(Double.valueOf(amt));
                                        p.setTotalFundingAmount(Double.valueOf(amt));
                                    } catch (Exception ex) {
                                    }
                                }
                            }
                        }

                        if (c_index == 21) {
                            String notes = "";
                            if (p.getNotes() != null) notes = p.getNotes();
                            notes = notes + ". " + c.getStringCellValue();
                            p.setNotes(notes);
                        }

                        p.setStatus(Status.CURRENT);

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

                        if (c_index == 2) {
                            p.setFundingBasis(FundingBasis.NATIONAL);
                            if (c.getStringCellValue().startsWith("Regional"))
                                p.setFundingBasis(FundingBasis.REGIONAL);
                        }

                        p.setTotalFundingCurrency(Currency.USD);

                        if (c_index == 4) {
                            Double amt = Double.parseDouble(c.getStringCellValue().trim().replaceAll("\"", ""));
                            p.setTotal(amt);
                            p.setTotalFundingAmount(amt);
                        }

                        if (c_index == 5) {
                            p.setTimeFrame(c.getStringCellValue());
                        }

                        if (c_index == 7) p.setPrincipalSource(c.getStringCellValue());

                        if (c_index == 8) p.setAdditionalSource(c.getStringCellValue());

                        if (c_index == 9) {
                            p.setLaterality(Laterality.BILATERAL);
                            if (c.getStringCellValue().startsWith("Multi"))
                                p.setLaterality(Laterality.MULTILATERAL);
                        }

                        p.setProjectType(ProjectType.CCA);

                        if (c_index == 10) {
                            p.setMinistry(c.getStringCellValue());
                        }


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

                        if (c_index == 14) {
                            p.setWeightingPercentage(c.getStringCellValue());
                        }

                        if (c_index == 15) {
                            p.setInkindPercentage(c.getStringCellValue());
                        }

                        if (c_index == 20) {
                            p.setModality(Modality.ON_BUDGET);
                            if (c.getStringCellValue().trim().equals("No")) p.setModality(Modality.OFF_BUDGET);
                        }

                        if (c_index == 23) {
                            p.setStatus(Status.CURRENT);
                            if (c.getStringCellValue().trim().equals("Completed"))
                                p.setStatus(Status.COMPLETED);
                            if (c.getStringCellValue().trim().equals("Pipeline")) p.setStatus(Status.PIPELINE);
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
                                    if (tmp.toLowerCase().equals("jpy"))
                                        p.setTotalFundingCurrency(Currency.JPY);
                                    if (tmp.toLowerCase().equals("eur"))
                                        p.setTotalFundingCurrency(Currency.EUR);
                                    if (tmp.toLowerCase().equals("aud"))
                                        p.setTotalFundingCurrency(Currency.AUD);
                                    if (tmp.toLowerCase().equals("fjd"))
                                        p.setTotalFundingCurrency(Currency.FJD);
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
                            if (c.getStringCellValue().toLowerCase().trim().equals("no"))
                                p.setAppropriated(false);

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

                        p.setTimeFrame("2014-2016");

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
            if (p.getMinistry() == null) {
                p.setMinistry("Unknown");
            } else {
                if (p.getMinistry().trim().equals("")) {
                    p.setMinistry("Unknown");
                }
            }
            //projectRepository.save(p);
        }

        for (Project p : projectRepository.findAll()) {
            String ministry = p.getMinistry();
            ministry = ministry + " (" + p.getCountry().getCode() + ")";
            p.setMinistry(ministry);
            projectRepository.save(p);
        }



        return "cleaned.";
    }


    public String clean_old() {

        //assign detailed sectors
        Map<String, String> m = new HashMap<String, String>();
        m.put("Agriculture", "Food Security");
        m.put("Community", "Livelihood Options");
        m.put("Disaster Risk", "Disaster Risk Reduction");
        m.put("Education", "Enabling Environment");
        m.put("Energy", "Renewable Energy");
        m.put("Environment", "Conservation Biodiversity");
        m.put("Forestry", "Conservation Biodiversity");
        m.put("Fisheries", "Conservation Biodiversity");
        m.put("Governance", "");
        m.put("Health", "Livelihood Options");
        m.put("ICT", "ICT");
        m.put("Infrastructure", "Transport");
        m.put("Social", "Other");
        m.put("Transport", "Transport");
        m.put("Tourism", "Livelihood Options");
        m.put("Utility", "Renewable Energy");
        m.put("Other", "Other");
        m.put("Water", "Water and Sanitation");
        m.put("Waste Management", "Water and Sanitation");
        m.put("Communications", "ICT");


        for (Project p : projectRepository.findAll()) {
            //if (p.getStatus() == null) {
            int random = (int) (Math.random() * 3 + 1);
            if (random == 1) p.setStatus(Status.COMPLETED);
            if (random == 2) p.setStatus(Status.CURRENT);
            if (random == 3) p.setStatus(Status.PIPELINE);
            projectRepository.save(p);
            //}


            if (p.getDetailedSector() == null) {
                if (p.getSector() != null) {
                    String ds_name = m.get(p.getSector().getName()).toString();
                    for (DetailedSector ds : detailedSectorRepository.findAll()) {
                        if (ds.getName().equals(ds_name)) {
                            p.setDetailedSector(ds);
                            projectRepository.save(p);
                        }
                    }
                }
            }
        }


        //setup empty disbursements
        disbursementRepository.deleteAll();
        for (Project p : projectRepository.findAll()) {
            IntStream.range(2014, 2018).forEach(year -> {
                Disbursement d = new Disbursement();
                d.setAmount(0.00);
                d.setYear(year);
                d.setProject(p);
                disbursementRepository.save(d);
            });
        }



        /*
        int total = 0;
        for (Project p : projectRepository.findAll()) {
            if (p.getTotal() == null) {
                p.setTotal(p.getTotalFundingAmount());
                total++;
                projectRepository.save(p);
            }
        }
        */


        /*
        for (Project p : projectRepository.findAll()) {
            if (p.getProjectType() == null || p.getProjectType().equals("")) {
                p.setProjectType(ProjectType.CCA);
                projectRepository.save(p);
            }

            if (p.getPrincipalSource() != null) {
                if (p.getPrincipalSource().startsWith("1_World")) {
                    p.setPrincipalSource("World Bank");
                    projectRepository.save(p);
                }

                if (p.getPrincipalSource().startsWith("2_Australia")) {
                    p.setPrincipalSource("Australia");
                    projectRepository.save(p);
                }

                if (p.getPrincipalSource().startsWith("1_ADB")) {
                    p.setPrincipalSource("ADB");
                    projectRepository.save(p);
                }

                if (p.getPrincipalSource().startsWith("2 New")) {
                    p.setPrincipalSource("New Zealand");
                    projectRepository.save(p);
                }

                if (p.getPrincipalSource().startsWith("ADB")) {
                    p.setPrincipalSource("ADB");
                    projectRepository.save(p);
                }

                if (p.getPrincipalSource().startsWith("Australia")) {
                    p.setPrincipalSource("Australia");
                    projectRepository.save(p);
                }
                if (p.getPrincipalSource().startsWith("\"")) {
                    p.setPrincipalSource(p.getPrincipalSource().replaceAll("\"", ""));
                    projectRepository.save(p);
                }
            }

        }
        */


        return "cleaned"; // : " + String.valueOf(total);

    }


    /**
     * PATCH  /metodologies : Updates an existing metodology.
     *
     * @param metodology the metodology to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated metodology,
     * or with status 400 (Bad Request) if the metodology is not valid,
     * or with status 500 (Internal Server Error) if the metodology couldn't be updated
     */
    @PostMapping("/methodology")
    public String patchMethodology(@RequestBody Metodology metodology) {
        try {
            if (metodology.getId() == null) {
                List<Metodology> queryResult = metodologyRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
                if (queryResult.size() > 0) {
                    metodology.setId(queryResult.get(0).getId());
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            Metodology result = metodologyRepository.save(metodology);

            savePdf("methodology.pdf", metodology.getMarkdown());

            return mapper.writeValueAsString(result);
        } catch (Exception e) { e.printStackTrace(); }
        return  "null";
    }

    /**
     * GET  /metodologies : get all the metodologies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of metodologies in body
     */
    @GetMapping("/methodology")
    public String getMethodology() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Metodology> queryResult = metodologyRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
            if (queryResult.size() > 0) {
                return mapper.writeValueAsString(queryResult.get(0));
            } else {
                return "null";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "null";
    }

    @GetMapping("/methodologypdf")
//    @GetMapping("/methodology")
    public String getMethodologyPdf() {
        try {
            List<Metodology> queryResult = metodologyRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
            if (queryResult.size() > 0) {
                    savePdf("methodology.pdf", queryResult.get(0).getMarkdown());
                return String.format("{\"file\": \"%s\"}", "methodology.pdf");
            } else {
                return String.format("{\"error\": \"%s\"}", "DB is empty");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }

    private final boolean savePdf(String filename, String markdown) {
        try {
            //                Document document = new Document(PageSize.LETTER);
//                PdfWriter.getInstance(document, new FileOutputStream(fileName));
//                document.open();
//                document.addCreationDate();
//
//                HTMLWorker htmlWorker = new HTMLWorker(document);
//                htmlWorker.parse(new StringReader(markdown));
//                document.close();
//                System.out.println("Done");

//                //Flying Saucer part
//                OutputStream out = new FileOutputStream(fileName);
//                ITextRenderer renderer = new ITextRenderer();
//
//                renderer.setDocumentFromString(markdown.replace("&nbsp;", "&#160;"));
//                renderer.layout();
//                renderer.createPDF(out);
//
//                out.close();


            OutputStream os = new FileOutputStream(filename);
            String source = String.format("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "<title></title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "%s" +
                    "</body>\n" +
                    "</html>\n",
                markdown
                .replace("\b&\b", "\\&")
                .replace("&nbsp;", "&#160;")
                .replace("<table>", "<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\">")

            );


            PdfRendererBuilder builder = new PdfRendererBuilder();
//                    builder.useFastMode();
            builder.withHtmlContent(source, "");
            builder.toStream(os);
            builder.run();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            File outputFile = new File(filename);
            return outputFile.exists() && outputFile.length() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
