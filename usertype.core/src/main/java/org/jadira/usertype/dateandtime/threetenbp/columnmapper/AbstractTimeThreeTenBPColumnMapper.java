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
package org.jadira.usertype.dateandtime.threetenbp.columnmapper;

import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

import org.jadira.usertype.spi.shared.AbstractTimeColumnMapper;
import org.jadira.usertype.spi.shared.DatabaseZoneConfigured;
import org.jadira.usertype.spi.shared.DstSafeTimeType;
import org.threeten.bp.ZoneOffset;

/**
 * @deprecated Jadira now depends on Java 8 so you are recommended to switch to the threeten package types
 */
@Deprecated
public abstract class AbstractTimeThreeTenBPColumnMapper<T> extends AbstractTimeColumnMapper<T> implements DatabaseZoneConfigured<ZoneOffset> {

    private static final long serialVersionUID = -7670411089210984705L;
	
    private ZoneOffset databaseZone = ZoneOffset.of("Z");
    
	public AbstractTimeThreeTenBPColumnMapper() {
	}

	public AbstractTimeThreeTenBPColumnMapper(ZoneOffset databaseZone) {
		this.databaseZone = databaseZone;
	}

    
    @Override
    public void setDatabaseZone(ZoneOffset databaseZone) {
        this.databaseZone = databaseZone;
    }

    protected ZoneOffset getDatabaseZone() {
        return databaseZone;
    }
	
    @Override
    public final DstSafeTimeType getHibernateType() {
    	
    	if (databaseZone == null) {
    		return DstSafeTimeType.INSTANCE;
    	}
    	
    	Calendar cal = resolveCalendar(databaseZone);
    	if (cal == null) {
    		throw new IllegalStateException("Could not map Zone " + databaseZone + " to Calendar");
    	}
    	
    	return new DstSafeTimeType(cal);
    }

	private Calendar resolveCalendar(ZoneOffset databaseZone) {
		
		String id = databaseZone.getId();
		if (Arrays.binarySearch(TimeZone.getAvailableIDs(), id) != -1) {
			return Calendar.getInstance(TimeZone.getTimeZone(id));
		} else {
			return null;
		}
	}
}
