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
package edu.nyu.cs.omnidroid.app.controller.datatypes;

import edu.nyu.cs.omnidroid.app.controller.util.DataTypeValidationException;

/**
 * Abstract class provides methods that will be supported by all "Omni" data types.
 */
public abstract class DataType {
  public interface Filter {
  };

  /**
   * Given the filter type & compare value, returns boolean indicating whether or not it matches.
   * 
   * @param filterType
   * @param userDefinedValue
   * @return boolean indicating whether or not filter is validated.
   * @throws IllegalArgumentException
   *           when the provided arguments are invalid.
   */
  public abstract boolean matchFilter(Filter filter, DataType userDefinedValue)
      throws IllegalArgumentException;

  /**
   * Validates whether or not the given userInput is valid for the given filter.
   * 
   * @param filter
   * @param userInput
   * @throws DataTypeValidationException
   *           when the validation fails.
   * @throws IllegalArgumentException
   *           when the provided argument are invalid.
   */
  public static void validateUserDefinedValue(Filter filter, String userInput)
      throws DataTypeValidationException, IllegalArgumentException {
  }

  /**
   * 
   * @return user friendly string representation of the data type.
   */
  public abstract String getValue();

  /**
   * 
   * @return string representation of the data type than can be stored and used to initialize the
   *         object.
   */
  public abstract String toString();

  /**
   * 
   * @param filterString
   *          string representing the filter value.
   * @return Filter represented by the string.
   * @throws IllegalArgumentException
   *           when the passed in string is not a valid filter.
   */
  public static Filter getFilterFromString(String filterString) throws IllegalArgumentException {
    return null;
  }
}