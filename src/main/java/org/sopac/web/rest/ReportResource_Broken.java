package org.sopac.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

/**
 * ReportResource controller
 */
@Controller
@RequestMapping(value = "/report")
//@Secured({AuthoritiesConstants.ANONYMOUS, AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN})
public class ReportResource_Broken {

    private final Logger log = LoggerFactory.getLogger(ReportResource_Broken.class);

    /**
     * GET generate
     */
    @GetMapping(value = "/generate", produces = MediaType.TEXT_HTML_VALUE)
    public String generate() {
        return "<b>generate</b>";
    }

    /**
     * GET test
     */
    @GetMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
    public String test() {
        return "test";
    }

}
