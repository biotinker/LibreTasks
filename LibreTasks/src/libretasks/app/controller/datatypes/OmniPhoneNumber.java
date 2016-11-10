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
/**
 * 
 */
package libretasks.app.controller.datatypes;

import static android.telephony.PhoneNumberUtils.*;

import libretasks.app.controller.util.DataTypeValidationException;

/**
 * Provides filtering capabilities for phone number.
 */
public class OmniPhoneNumber extends DataType {
  private String value;
  
  /* data type name to be stored in db */
  public static final String DB_NAME = "PhoneNumber";
  
  public enum Filter implements DataType.Filter {
    EQUALS("equals") , NOTEQUALS("not equals");
    
    public final String displayName;
    
    Filter(String displayName) {
     this.displayName = displayName; 
    }
  }

  public OmniPhoneNumber(String phoneNumber) throws DataTypeValidationException {
    if (!isAReferenceTag(phoneNumber)) {
      if (!isWellFormedSmsAddress(phoneNumber)) {
        throw new DataTypeValidationException("Invalid phone number " + phoneNumber + " provided.");
      }
      value = formatNumber(phoneNumber);
    }
    value = phoneNumber;
  }

  /**
   * Check if the phone number is actually a reference to a data from the intent or external
   * parameter.
   */
  private boolean isAReferenceTag(String str) {
    if (str == null) {
      return false;
    }
    return str.indexOf("<") == 0 && str.lastIndexOf(">") == (str.length() - 1);
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

  /*
   * (non-Javadoc)
   * 
   * @see edu.nyu.cs.omnidroid.core.datatypes.DataType#matchFilter(java.lang.String,
   * java.lang.String)
   */
  public boolean matchFilter(DataType.Filter filter, DataType userDefinedValue)
      throws IllegalArgumentException {
    if (!(filter instanceof Filter)) {
      throw new IllegalArgumentException("Invalid filter type '" + filter.toString()
          + "' provided.");
    }
    if (userDefinedValue instanceof OmniPhoneNumber) {
      return matchFilter((Filter) filter, (OmniPhoneNumber) userDefinedValue);
    } else {
      throw new IllegalArgumentException("Matching filter not found for the datatype "
          + userDefinedValue.getClass().toString() + ". ");
    }
  }

  public boolean matchFilter(Filter filter, OmniPhoneNumber comparisonValue) {
    switch (filter) {
    case EQUALS:
      return compare(value, comparisonValue.value);
    case NOTEQUALS: 
      return !compare(value, comparisonValue.value); 
    default:
      return false;
    }
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
    new OmniPhoneNumber(userInput);
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
   * Provides the string representation of the OmniPhoneNumber. The number is formatted using
   * android's SDK.
   */
  public String toString() {
    return value;
  }

  public String getValue() {
    return this.value;
  }
}
