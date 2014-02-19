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

import org.laatusys.nobabylon.entity.Job;
import org.laatusys.nobabylon.entity.SearchResults;
import org.laatusys.nobabylon.support.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Filters based on a collection of Filters.
 */
public class DefaultFilteringService implements FilteringService {

    private List<Filter> filters;

    public DefaultFilteringService(List<Filter> filters) {
        this.filters = filters;
    }

    public SearchResults filter(SearchResults results) {
        SearchResults filteredResults = new SearchResults();
        filteredResults.setJobs(new ArrayList<Job>());
        filteredResults.setHitCount(0);
        List<Job> jobs = results.getJobs();
        for (Job job : jobs) {
            if (acceptFilters(job)) {
                filteredResults.getJobs().add(job);
                filteredResults.setHitCount(filteredResults.getHitCount() + 1);
            }
        }
        return filteredResults;
    }

    private boolean acceptFilters(Job job) {
        for (Filter filter : filters) {
            if (!filter.accept(job.getDescription())) {
                return false;
            }
        }
        return true;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }
}
