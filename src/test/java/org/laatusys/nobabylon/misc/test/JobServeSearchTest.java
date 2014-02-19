/*
    No-Babylon a job search engine with filtering ability

    Copyright (C) 2012-2014 ferenc.jdev@gmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.laatusys.nobabylon.misc.test;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.laatusys.jobserve.schema.JobSearchResults;
import org.laatusys.nobabylon.entity.SearchResults;
import org.laatusys.nobabylon.service.JobServeJobSearchService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobServeSearchTest {

    private JobServeJobSearchService searchService = new JobServeJobSearchService();

    @Test
    public void testSearch() throws Exception {
        RestTemplate restTemplate = mock(RestTemplate.class);
        JobSearchResults results = loadSampleResult();
        ResponseEntity<JobSearchResults> response = new ResponseEntity<JobSearchResults>(results, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), (HttpEntity) anyObject(), eq(JobSearchResults.class))).thenReturn(response);

        ReflectionTestUtils.setField(searchService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(searchService, "apiUrlBase", "<none>");
        ReflectionTestUtils.setField(searchService, "jobServiceAuthToken", "<none>");


        SearchResults jobs = searchService.findJobs("DEU", "Java");
        assertEquals(205, jobs.getHitCount());
        assertEquals(20, jobs.getJobs().size());
    }

    private JobSearchResults loadSampleResult() throws IOException, JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance("org.laatusys.jobserve.schema");
        StringReader reader = new StringReader(FileUtils.readFileToString(new File(getClass().getResource("/sample.xml").getFile())));
        JAXBElement<JobSearchResults> results = (JAXBElement) jaxbContext.createUnmarshaller().unmarshal(reader);
        return results.getValue();
    }


}
