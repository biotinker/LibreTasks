/*******************************************************************************
 * Copyright 2009 OmniDroid - http://code.google.com/p/omnidroid 
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
package edu.nyu.cs.omnidroid.core.datatypes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import edu.nyu.cs.omnidroid.util.DataTypeValidationException;

/**
 * Provides date & time filter functionality.
 */
public class OmniDate extends DataType {
  private Date value;
  public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public enum Filter implements DataType.Filter {
    BEFORE, AFTER, ISDAYOFWEEK;
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
    case AFTER:
      return after(compareValue);
    case BEFORE:
      return before(compareValue);
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

  public boolean before(OmniDate compareDate) {
    return value.before(compareDate.getDate());
  }

  public boolean after(OmniDate compareDate) {
    return value.after(compareDate.getDate());
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
    if (filter instanceof Filter) {
      throw new IllegalArgumentException("Invalid filter type '" + filter.toString()
          + "' provided.");
    }
    if (userInput == null) {
      throw new DataTypeValidationException("The user input cannot be null.");
    }

    getDate(userInput);
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
    if(!(filter instanceof Filter)){
      throw new IllegalArgumentException("Invalid filter "+filter.toString()+" provided.");
    }
    if(userDefinedValue instanceof OmniDate){
      return matchFilter((Filter) filter, (OmniDate) userDefinedValue);
    }else if(userDefinedValue instanceof OmniDayOfWeek){
      return matchFilter((Filter) filter, (OmniDayOfWeek) userDefinedValue);
    }
    throw new IllegalArgumentException("Matching filter not found for the datatype " + 
        userDefinedValue.getClass().toString()+ ". ");
  }

}
