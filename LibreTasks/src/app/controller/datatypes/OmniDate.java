/*  
 * Copyright (c) 2016  LibreTasks - https://github.com/biotinker/LibreTasks  
 *  
 *  This file is free software: you may copy, redistribute and/or modify it  
 *  under the terms of the GNU General Public License as published by the  
 *  Free Software Foundation, either version 3 of the License, or (at your  
 *  option) any later version.  
 *  
 *  This file is distributed in the hope that it will be useful, but  
 *  WITHOUT ANY WARRANTY; without even the implied warranty of  
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  
 *  General Public License for more details.  
 *  
 *  You should have received a copy of the GNU General Public License  
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 *  
 * This file incorporates work covered by the following copyright and  
 * permission notice:  
 /*******************************************************************************
 * Copyright 2009 Omnidroid - http://code.google.com/p/omnidroid 
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 *******************************************************************************/
package libretasks.app.controller.datatypes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import libretasks.app.controller.util.DataTypeValidationException;

/**
 * Provides date & time filter functionality.
 */
public class OmniDate extends DataType {
  
  public static final int MINUTES_IN_HOUR = 60;
  public static final int SECONDS_IN_MINUTE = 60;
  public static final int SECONDS_IN_HOUR = 3600;
  private Date value;
  public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  /* data type name to be stored in db */
  public static final String DB_NAME = "Date";
  
  //TODO(Roger):put display name into resource(pass in context to construct).
  public enum Filter implements DataType.Filter {
    //for OmniDate
    //is and is_not only compares down to minute and ignore date.
    IS_EVERYDAY("is (daily)"), IS_NOT_EVERYDAY("is not (daily)"), 
    BEFORE("is before"), AFTER("is after"), 
    BEFORE_EVERYDAY("is before (daily)"), AFTER_EVERYDAY("is after (daily)"), 
    //for OmniTimePeriod
    DURING("is during"), DURING_EVERYDAY("is during (daily)"), EXCEPT("is not during"), 
    EXCEPT_EVERYDAY("is not during (daily)"), 
    //for OmniDayOfWeek
    ISDAYOFWEEK("is day of week");
    
    public final String displayName;
    
    Filter(String displayName) {
      this.displayName = displayName;
    }
  }

  public OmniDate(Date date) {
    this.value = date;
  }

  /**
   * 
   * @param str
   *          the filter name.
   * @return Filter
   * @throws IllegalArgumentException
   *           when the filter with the given name does not exist.
   */
  public static Filter getFilterFromString(String str) throws IllegalArgumentException {
    return Filter.valueOf(str.toUpperCase());
  }

  /**
   * 
   * @param date
   *          Must be in (yyyy-MM-dd hh:mm:ss) format.
   * @throws DataTypeValidationException
   *           when the string format is invalid.
   */
  public OmniDate(String date) throws DataTypeValidationException {
    value = getDate(date);
  }

  /**
   * 
   * @param date
   *          Must be in (yyyy-MM-dd HH:mm:ss) format.
   * @return Date
   * @throws DataTypeValidationException
   *           when the string format is invalid.
   */
  public static Date getDate(String date) throws DataTypeValidationException {
    dateFormat.setLenient(true);
    try {
      return dateFormat.parse(date);
    } catch (ParseException e) {
      throw new DataTypeValidationException("Invalid value '" + date
          + "' provided for date.  Must be of format 'yyyy-MM-dd HH:mm:ss'. " + e.getMessage());
    }
  }

  public boolean matchFilter(Filter filter, OmniDate compareValue) {
    switch (filter) {
    case IS_EVERYDAY:
      return is(compareValue);
    case IS_NOT_EVERYDAY:
      return isNot(compareValue);
    case AFTER:
      return after(compareValue);
    case BEFORE:
      return before(compareValue);
    case AFTER_EVERYDAY:
      return afterEveryday(compareValue);
    case BEFORE_EVERYDAY:
      return beforeEveryday(compareValue);
    default:
      return false;
    }
  }
  
  public boolean matchFilter(Filter filter, OmniTimePeriod compareValue) {
    switch (filter) {
    case DURING :
      return compareValue.during(this);
    case DURING_EVERYDAY :
      return compareValue.duringEveryday(this);
    case EXCEPT :
      return compareValue.except(this);
    case EXCEPT_EVERYDAY :
      return compareValue.exceptEveryday(this);
    default:
      return false;
    }
  }

  public boolean matchFilter(Filter filter, OmniDayOfWeek compareValue) {
    switch (filter) {
    case ISDAYOFWEEK:
      return isDayOfWeek(compareValue);
    default:
      return false;
    }
  }

  /**
   * Indicates whether or not provided value is before.
   * 
   * @param userDefinedValue
   * @return true if the value is before, false otherwise
   * @throws IllegalArgumentException
   */
  public boolean before(String userDefinedValue) throws IllegalArgumentException {
    try {
      return value.before(getDate(userDefinedValue));
    } catch (DataTypeValidationException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }
  
  /**
   * Indicates whether or not provided value has the same hour and minute as the 
   * <code>compareDate</code>.
   * 
   * @param compareDate
   * @return
   */
  public boolean is(OmniDate compareDate) {
    return (value.getHours() == compareDate.value.getHours() &&
        value.getMinutes() == compareDate.value.getMinutes());
  }
  
  /**
   * Indicates whether or not provided value has the same hour and minute as the 
   * <code>compareDate</code>.
   * 
   * @param compareDate
   * @return
   */
  public boolean isNot(OmniDate compareDate) {
    return !is(compareDate);
  }

  /**
   * return true if this time is before the <code>compareDate</code>
   * 
   * @param compareDate
   * @return
   */
  public boolean before(OmniDate compareDate) {
    return value.before(compareDate.getDate());
  }
  
  /**
   * return true if this time is after the <code>compareDate</code>
   * 
   * @param compareDate
   * @return
   */
  public boolean after(OmniDate compareDate) {
    return value.after(compareDate.getDate());
  }
  
  /**
   * only compare the hour minute and second inclusive.
   * 
   * @param compareDate
   * @return
   */
  public boolean beforeEveryday(OmniDate compareDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(compareDate.getDate());
    int compareHour = calendar.get(Calendar.HOUR_OF_DAY);
    int compareMinute = calendar.get(Calendar.MINUTE);
    int compareSecond = calendar.get(Calendar.SECOND);
    int compareSecondInDay = compareHour * SECONDS_IN_HOUR + compareMinute * SECONDS_IN_MINUTE + compareSecond;
    calendar.setTime(value);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    int secondInDay = hour * SECONDS_IN_HOUR + minute * SECONDS_IN_MINUTE + second;
    
    if (secondInDay <= compareSecondInDay) {
      return true;
    }
    return false;
  }
  
  /**
   * only compare the hour minute and second
   * @param compareDate
   * @return
   */
  public boolean afterEveryday(OmniDate compareDate) {
    return !beforeEveryday(compareDate);
  }

  public boolean isDayOfWeek(OmniDayOfWeek compareValue) {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(value);
    return calendar.get(GregorianCalendar.DAY_OF_WEEK) == compareValue.getDayOfWeek();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.nyu.cs.omnidroid.core.datatypes.DataType#validateUserDefinedValue(DataType.Filter,
   * java.lang.String)
   */
  public static void validateUserDefinedValue(DataType.Filter filter, String userInput)
      throws DataTypeValidationException, IllegalArgumentException {
    if (!(filter instanceof Filter)) {
      throw new IllegalArgumentException("Invalid filter type '" + filter.toString()
          + "' provided.");
    }
    if (userInput == null) {
      throw new DataTypeValidationException("The user input cannot be null.");
    }
    switch ((Filter) filter) {
    case IS_EVERYDAY:
    case IS_NOT_EVERYDAY:
    case BEFORE:
    case AFTER:
    case BEFORE_EVERYDAY:
    case AFTER_EVERYDAY:
      getDate(userInput);
      break;
    case ISDAYOFWEEK:
      new OmniDayOfWeek(userInput);
      break;
    case DURING:
    case DURING_EVERYDAY:
    case EXCEPT:
    case EXCEPT_EVERYDAY:
      new OmniTimePeriod(userInput);
    default:
      throw new DataTypeValidationException("Filter for " + filter.toString()
          + " not yet supported.");
    }
  }

  /**
   * Indicates whether or not the given filter is supported by the data type.
   * 
   * @param filter
   * @return true if the filter is supported, false otherwise.
   */
  public static boolean isValidFilter(String filter) {
    try {
      getFilterFromString(filter);
    } catch (IllegalArgumentException e) {
      return false;
    }

    return true;
  }

  /**
   * @return string representation of OmniDate object. The format for the string is 'yyyy-MM-dd
   *         HH:mm:ss'.
   * 
   */
  public String toString() {
    return dateFormat.format(value);
  }

  public String getValue() {
    return dateFormat.format(value);
  }

  public Date getDate() {
    return value;
  }

  @Override
  public boolean matchFilter(DataType.Filter filter, DataType userDefinedValue)
      throws IllegalArgumentException {
    if(filter == null || !(filter instanceof Filter)){
      throw new IllegalArgumentException("Invalid filter "+filter+" provided.");
    }
    if (userDefinedValue == null) {
      throw new IllegalArgumentException("userDefinedValue is null.");
    }
    if (userDefinedValue instanceof OmniDate) {
      return matchFilter((Filter) filter, (OmniDate) userDefinedValue);
    } else if(userDefinedValue instanceof OmniDayOfWeek) {
      return matchFilter((Filter) filter, (OmniDayOfWeek) userDefinedValue);
    } else if (userDefinedValue instanceof OmniTimePeriod) {
      return matchFilter((Filter)filter, (OmniTimePeriod)userDefinedValue);
    }
    throw new IllegalArgumentException("Matching filter not found for the datatype " + 
        userDefinedValue.getClass().toString()+ ". ");
  }

}
