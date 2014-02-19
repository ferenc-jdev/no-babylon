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

package org.laatusys.nobabylon.service;

import org.joda.time.DateTime;
import org.laatusys.jobserve.schema.JobSearchResults;
import org.laatusys.nobabylon.entity.Job;
import org.laatusys.nobabylon.entity.SearchResults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JobServeJobSearchService implements JobSearchService {

    @Value("${jobserve.auth.token}")
    private String jobServiceAuthToken;

    @Value("${jobserve.api.url.base}")
    private String apiUrlBase;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public SearchResults findJobs(String country, String keywords) {
        String url = buildUrl(country, keywords);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
        HttpEntity<byte[]> entity = new HttpEntity<byte[]>(headers);
        ResponseEntity<JobSearchResults> response = restTemplate.exchange(url, HttpMethod.GET, entity, JobSearchResults.class);
        return convert(response.getBody());
    }



    private SearchResults convert(JobSearchResults jobSearchResults) {
        SearchResults searchResults = new SearchResults();
        searchResults.setHitCount(jobSearchResults.getJobCount());
        List<Job> jobs = new ArrayList<Job>();
        List<org.laatusys.jobserve.schema.Job> jobList = jobSearchResults.getJobs().getValue().getJob();
        for (org.laatusys.jobserve.schema.Job job : jobList) {
            Job jobDescription = new Job();
            jobDescription.setAdvertiser(job.getAdvertiserName().getValue());
            jobDescription.setCountry(job.getCountry().getValue());
            jobDescription.setTitle(job.getShortDescription().getValue());
            jobDescription.setDescription(job.getHtmlDescription().getValue());
            jobDescription.setPermalink(job.getPermalink().getValue());
            jobDescription.setSalary(job.getSalary().getValue());
            jobDescription.setLocation(job.getLocation().getValue().getText().getValue());
            jobDescription.setPostedTime(new DateTime(job.getDatePosted().getValue().toGregorianCalendar().getTime()));
            jobs.add(jobDescription);
        }
        searchResults.setJobs(jobs);
        return searchResults;
    }

    private String buildUrl(String country, String keywords) {
        StringBuilder urlBuilder = new StringBuilder(apiUrlBase);
        urlBuilder.append("?Token=").append(jobServiceAuthToken);
        urlBuilder.append("&").append("Locations[0][Country]").append("=").append(country);
        urlBuilder.append("&Skills=").append(keywords);
        return urlBuilder.toString();
    }

}
