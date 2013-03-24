/*
 *  Copyright 2010, 2011 Christopher Pheby
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jadira.usertype.dateandtime.joda.columnmapper;

import java.sql.Timestamp;

import org.jadira.usertype.dateandtime.joda.util.Formatter;
import org.jadira.usertype.dateandtime.joda.util.ZoneHelper;
import org.jadira.usertype.spi.shared.AbstractTimestampColumnMapper;
import org.jadira.usertype.spi.shared.ColumnMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

public class TimestampColumnLocalDateTimeMapper extends AbstractTimestampColumnMapper<LocalDateTime> implements ColumnMapper<LocalDateTime, Timestamp>  {

    private static final long serialVersionUID = -7670411089210984705L;

    private DateTimeZone databaseZone = DateTimeZone.UTC;

    public TimestampColumnLocalDateTimeMapper() {
    }
    
    public TimestampColumnLocalDateTimeMapper(DateTimeZone databaseZone) {
    	this.databaseZone = databaseZone;
    }
    
    @Override
    public LocalDateTime fromNonNullString(String s) {
        return new LocalDateTime(s);
    }

    @Override
    public LocalDateTime fromNonNullValue(Timestamp value) {

        DateTimeZone currentDatabaseZone = databaseZone == null ? ZoneHelper.getDefault() : databaseZone;
    	
    	DateTime dt = Formatter.TIMESTAMP_FORMATTER.withZone(currentDatabaseZone).parseDateTime(value.toString()); 
        return dt.toLocalDateTime();
    }
    
    @Override
    public String toNonNullString(LocalDateTime value) {
        return value.toString();
    }

    @Override
    public Timestamp toNonNullValue(LocalDateTime value) {
        
        String formattedTimestamp = Formatter.TIMESTAMP_FORMATTER.print(value);
        if (formattedTimestamp.endsWith(".")) {
            formattedTimestamp = formattedTimestamp.substring(0, formattedTimestamp.length() - 1);
        }

        final Timestamp timestamp = Timestamp.valueOf(formattedTimestamp);
        return timestamp;
    }
    
    public void setDatabaseZone(DateTimeZone databaseZone) {
        this.databaseZone = databaseZone;
    }
}
