package org.sopac.web.rest;

import org.sopac.ClimatefinanceApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Test class for the ReportResource REST controller.
 *
 * @see ReportResource_Broken
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClimatefinanceApp.class)
public class ReportResourceIntTest {

    private MockMvc restMockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ReportResource_Broken reportResource = new ReportResource_Broken();
        restMockMvc = MockMvcBuilders
            .standaloneSetup(reportResource)
            .build();
    }

    /**
    * Test generate
    */
    @Test
    public void testGenerate() throws Exception {
        restMockMvc.perform(get("/api/report/generate"))
            .andExpect(status().isOk());
    }
    /**
    * Test test
    */
    @Test
    public void testTest() throws Exception {
        restMockMvc.perform(get("/api/report/test"))
            .andExpect(status().isOk());
    }

}
