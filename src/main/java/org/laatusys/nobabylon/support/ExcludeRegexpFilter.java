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

package org.laatusys.nobabylon.support;

import java.util.regex.Pattern;

public class ExcludeRegexpFilter implements Filter {

    private final Pattern pattern;

    public ExcludeRegexpFilter(String regexp, boolean caseSensitive) {
        pattern = caseSensitive ? Pattern.compile(regexp) : Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
    }

    public ExcludeRegexpFilter(String regexp) {
        this(regexp, false);
    }

    @Override
    public boolean accept(String description) {
        return !pattern.matcher(description).find();
    }
}
