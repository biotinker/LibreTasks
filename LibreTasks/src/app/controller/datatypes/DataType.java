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

import libretasks.app.controller.util.DataTypeValidationException;

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
