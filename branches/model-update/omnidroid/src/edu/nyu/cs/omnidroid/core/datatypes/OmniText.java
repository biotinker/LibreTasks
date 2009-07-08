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

import java.util.regex.Pattern;

/**
 * Provides data type that can be used to provide text filters.
 */
public class OmniText implements DataType {
  private String value;
  private static String[] filters = { "contains", "equals" };

  public OmniText(String str) {
    this.value = str;
  }

  public OmniText(Object obj) {
    this.value = obj.toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.nyu.cs.omnidroid.core.datatypes.DataType#matchFilter(java.lang.String,
   * java.lang.String)
   */
  public boolean matchFilter(String filterType, String userDefinedValue)
      throws IllegalArgumentException {
    if (filterType.equals("contains")) {
      return value.matches("(?i:.*" + Pattern.quote(userDefinedValue) + ".*)");
    } else if (filterType.equals("equals")) {
      return value.equalsIgnoreCase(userDefinedValue);
    }
    throw new IllegalArgumentException("Invalid filter type " + filterType + " provided.");
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
  }

  public static boolean isValidFilter(String filter) {
    for (String s : filters) {
      if (s.equals(filter))
        return true;
    }
    return false;
  }

  public String getValue() {
    return this.value;
  }
  
  public String toString(){
    return this.value;
  }
}
