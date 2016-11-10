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
import java.util.Date;

import libretasks.app.controller.util.DataTypeValidationException;

/**
 * Represent a time period, Provides time period filter functionality.
 *
 */
public class OmniTimePeriod extends DataType {
  private Date startTime;
  private Date endTime;
  
  public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final String OmniTimePeriodOpenTag = "<omniTimePeriod>";
  private static final String OmniTimePeriodCloseTag = "</omniTimePeriod>";
  private static final String StartTimeOpenTag = "<startTime>";
  private static final String StartTimeCloseTag = "</startTime>";
  private static final String EndTimeOpenTag = "<endTime>";
  private static final String EndTimeCloseTag = "</endTime>";
  
  /* data type name to be stored in db */
  public static final String DB_NAME = "TimePeriod";
  
  public enum Filter implements DataType.Filter {
    DURING("during"), DURING_EVERYDAY("during (daily)"), EXCEPT("not during"), 
    EXCEPT_EVERYDAY("not during (daily)");
    
    public final String displayName;
    
    Filter(String displayName) {
     this.displayName = displayName; 
    }
  }
  
  public OmniTimePeriod(Date startTime, Date endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }
  
  public OmniTimePeriod(String startTime, String endTime) throws DataTypeValidationException {
    this.startTime = getDate(startTime);
    this.endTime = getDate(endTime);
  }
  
  public OmniTimePeriod(String omniTimePeriodString) throws DataTypeValidationException {
    OmniTimePeriod otp = parseOmniTimePeriodString(omniTimePeriodString);
    this.startTime = otp.startTime;
    this.endTime = otp.endTime;
  }
  
  public int getStartHour() {
    return startTime.getHours();
  }
  
  public int getStartMinute() {
    return startTime.getMinutes();
  }
  
  public int getEndHour() {
    return endTime.getHours();
  }
  
  public int getEndMinute() {
    return endTime.getMinutes();
  }
  
  /**
   * Formatted String for <code>startTime</code>.
   * @return
   */
  public String getStartTimeString() {
    return dateFormat.format(startTime);
  }
  
  /**
   * Formatted String for <code>endTime</code>.
   * @return
   */
  public String getEndTimeString() {
    return dateFormat.format(endTime);
  }
  
  /**
   * 
   * @param str
   *          the filter name.
   * @return Filter
   * @throws IllegalArgumentException
   *           when the filter with the given name does not exist.
   */
  public static Filter getFilterFromString(String filterString) throws IllegalArgumentException {
    return Filter.valueOf(filterString.toUpperCase());
  }
  
  /**
   * Convert a date from String representation to <code>Date</code>
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
  
  private OmniTimePeriod parseOmniTimePeriodString(String omniTimePeriodString)
        throws DataTypeValidationException {
    String body = parseTagValue(omniTimePeriodString, OmniTimePeriodOpenTag, OmniTimePeriodCloseTag);
    Date startTime = getDate(parseTagValue(body, StartTimeOpenTag, StartTimeCloseTag));
    Date endTime = getDate(parseTagValue(body, EndTimeOpenTag, EndTimeCloseTag));
    return new OmniTimePeriod(startTime, endTime);
  }
  
  
  /**
   * Parses out the text located between first occurrences of the open and closed tags.
   * 
   * @param parseString
   *          string to parse
   * @param openTag
   *          the opening tag
   * @param closeTag
   *          the closing tag
   * @return text located between first occurrences of the first open and closed tags, or null if
   *         proper tags are not found.
   */
  private static String parseTagValue(String parseString, String openTag, String closeTag) {
    // TODO (dvo203): Replace by standard XML parsing methods.
    int beg, end;

    beg = parseString.indexOf(openTag);
    end = parseString.indexOf(closeTag);
    if (beg < 0 || end < 0) {
      return null;
    }
    if (beg > end) {
      return null;
    }
    if (beg + openTag.length() == end) {
      return "";
    }

    return parseString.substring(beg + openTag.length(), end);
  }
  
  public String getValue() {
    StringBuilder sb = new StringBuilder();
    sb.append("Start: ").append(dateFormat.format(startTime))
      .append("; End: ").append(dateFormat.format(endTime));
    return sb.toString();
  }

  public boolean matchFilter(DataType.Filter filter, DataType userDefinedValue)
      throws IllegalArgumentException {
    if (! (filter instanceof Filter)) {
      throw new IllegalArgumentException("Invalid filter "+filter.toString()+" provided.");
    }
    if (! (userDefinedValue instanceof OmniDate)) {
      throw new IllegalArgumentException("Matching filter not found for the datatype " + 
          userDefinedValue.getClass().toString()+ ". ");
    }
    switch ((Filter)filter) {
    case DURING:
      return during((OmniDate)userDefinedValue);
    case DURING_EVERYDAY:
      return duringEveryday((OmniDate)userDefinedValue);
    case EXCEPT:
      return except((OmniDate)userDefinedValue);
    case EXCEPT_EVERYDAY:
      return exceptEveryday((OmniDate)userDefinedValue);
    default:
      return false;
    }
  }

  public boolean exceptEveryday(OmniDate userDefinedValue) {
    return !duringEveryday(userDefinedValue);
  }

  /**
   * This method only compares hour, minute and second, that is it only compares time within a day.
   * 
   * @param userDefinedValue
   * @return
   */
  public boolean duringEveryday(OmniDate userDefinedValue) {
    if (userDefinedValue.afterEveryday(new OmniDate(startTime)) && 
        userDefinedValue.beforeEveryday(new OmniDate(endTime))) {
      return true;
    }
    return false;
  }

  public boolean except(OmniDate userDefinedValue) {
    return !during(userDefinedValue);
  }

  public boolean during(OmniDate userDefinedValue) {
    return (userDefinedValue.getDate().after(startTime) && 
            userDefinedValue.getDate().before(endTime));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(OmniTimePeriodOpenTag)
        .append(StartTimeOpenTag).append(dateFormat.format(startTime)).append(StartTimeCloseTag)
        .append(EndTimeOpenTag).append(dateFormat.format(endTime)).append(EndTimeCloseTag)
      .append(OmniTimePeriodCloseTag);
    return sb.toString();
  }
  
  /**
   * get a formatted string representation for the given time
   * @return
   */
  public static String buildTimeString(int year, int month, int day, int hour, int minute, int second) {
    return String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
  }

}
