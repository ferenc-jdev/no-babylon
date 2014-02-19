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

import org.junit.Before;
import org.junit.Test;
import org.laatusys.nobabylon.entity.Job;
import org.laatusys.nobabylon.entity.SearchResults;
import org.laatusys.nobabylon.service.DefaultFilteringService;
import org.laatusys.nobabylon.support.ExcludeRegexpFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FilterTest {

    private DefaultFilteringService filteringService;

    @Before
    public void init() {
        List filters = Arrays.asList(new ExcludeRegexpFilter("Fluent German"));
        filteringService = new DefaultFilteringService(filters);
    }

    @Test
    public void testFiltering() throws Exception {
        SearchResults searchResults = new SearchResults();
        List<Job> jobs = new ArrayList<Job>();
        jobs.add(createJob("Java Developer, Fluent German"));
        jobs.add(createJob("Java Developer English speaking"));
        searchResults.setJobs(jobs);
        SearchResults filteredResult = filteringService.filter(searchResults);
        assertEquals(1, filteredResult.getJobs().size());
        assertTrue(filteredResult.getJobs().get(0).getDescription().contains("English"));
    }

    private Job createJob(String desc) {
        Job job = new Job();
        job.setDescription(desc);
        return job;
    }


}
