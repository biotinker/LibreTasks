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

/**
 * Provides date & time filter functionality.
 */
public class OmniDate implements DataType {
  private Date value;
  private static String[] filters = { "before", "after" };
  private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public OmniDate(Date date) {
    this.value = date;
  }

  /**
   * 
   * @param date
   *          Must be in (yyyy-MM-dd hh:mm:ss) format.
   * @throws DataTypeValidationException
   *           when the string format is invalid.
   */
  public OmniDate(String date) throws DataTypeValidationException {
    this.value = getDate(date);
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
      throw new DataTypeValidationException(
          "Invalid value provided for date.  Must be of format 'yyyy-MM-dd HH:mm:ss'. "
              + e.getMessage());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.nyu.cs.omnidroid.core.datatypes.DataType#matchFilter(java.lang.String,
   * java.lang.String)
   */
  public boolean matchFilter(String filterType, String userDefinedValue)
      throws IllegalArgumentException {
    if (filterType.equals("before")) {
      return this.before(userDefinedValue);
    } else if (filterType.equals("after")) {
      return this.after(userDefinedValue);
    }
    throw new IllegalArgumentException("Invalid filter type " + filterType + " provided.");
  }

  /**
   * Indicates whether or not provided value is before.
   * 
   * @param userDefinedValue
   * @return
   * @throws IllegalArgumentException
   */
  public boolean before(String userDefinedValue) throws IllegalArgumentException {
    try {
      return this.value.before(getDate(userDefinedValue));
    } catch (DataTypeValidationException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Indicates whether or not provided value is after.
   * 
   * @param userDefinedValue
   * @return
   * @throws IllegalArgumentException
   */
  public boolean after(String userDefinedValue) throws IllegalArgumentException {
    try {
      return this.value.after(getDate(userDefinedValue));
    } catch (DataTypeValidationException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.nyu.cs.omnidroid.core.datatypes.DataType#validateUserDefinedValue(java.lang.String,
   * java.lang.String)
   */
  public void validateUserDefinedValue(String filterName, String userInput)
      throws DataTypeValidationException, IllegalArgumentException {
    if (!isValidFilter(filterName)) {
      throw new IllegalArgumentException("Invalid filter type " + filterName + " provided.");
    }
    if (userInput == null) {
      throw new DataTypeValidationException("The user input cannot be null.");
    }
    getDate(userInput);
  }

  public static boolean isValidFilter(String filter) {
    for (String s : filters) {
      if (s.equals(filter))
        return true;
    }
    return false;
  }

  public String toString() {
    return dateFormat.format(this.value);
  }

  public String getValue() {
    return dateFormat.format(this.value);
  }

}
