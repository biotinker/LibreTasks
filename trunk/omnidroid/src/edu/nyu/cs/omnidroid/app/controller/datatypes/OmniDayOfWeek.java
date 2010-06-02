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
package edu.nyu.cs.omnidroid.app.controller.datatypes;

import edu.nyu.cs.omnidroid.app.controller.util.DataTypeValidationException;

import java.util.GregorianCalendar;

/**
 * Provides data type for storing day of the week.
 */
public class OmniDayOfWeek extends DataType {
  private DayOfWeek value;
  
  /* data type name to be stored in db */
  public static final String DB_NAME = "DayOfWeek";
    
  private enum DayOfWeek {
    SUNDAY(GregorianCalendar.SUNDAY, "Sunday"), MONDAY(GregorianCalendar.MONDAY, "Monday"), TUESDAY(
        GregorianCalendar.TUESDAY, "Tuesday"), WEDNESDAY(GregorianCalendar.WEDNESDAY, "Wednesday"), 
        THURSDAY(GregorianCalendar.THURSDAY, "Thursday"), FRIDAY(GregorianCalendar.FRIDAY, "Friday")
        , SATURDAY(GregorianCalendar.SATURDAY, "Saturday");

    private final int num;
    private final String name;

    DayOfWeek(int num, String name) {
      this.num = num;
      this.name = name;
    }

    /**
     * @return the num
     */
    public int getNum() {
      return num;
    }

    /**
     * @return the name
     */
    public String getName() {
      return name;
    }
  }

  public OmniDayOfWeek(String day) {
    value = DayOfWeek.valueOf(day.toUpperCase());
  }

  /**
   * 
   * @return the Gregorian calendar's constant representing the day of week.
   */
  public int getDayOfWeek() {
    return value.getNum();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.nyu.cs.omnidroid.core.datatypes.DataType#getValue()
   */
  @Override
  public String getValue() {
    return value.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.nyu.cs.omnidroid.core.datatypes.DataType#toString()
   */
  @Override
  public String toString() {
    return value.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.nyu.cs.omnidroid.core.datatypes.DataType#validateUserDefinedValue(DataType.Filter,
   * java.lang.String)
   */
  public static void validateUserDefinedValue(DataType.Filter filter, String userInput)
      throws DataTypeValidationException, IllegalArgumentException {
    throw new DataTypeValidationException("This data type does not allow filter "
        + filter.toString());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.nyu.cs.omnidroid.core.datatypes.DataType#matchFilter(java.lang.String,
   * java.lang.String)
   */
  @Override
  public boolean matchFilter(Filter filter, DataType userDefinedValue)
      throws IllegalArgumentException {
    throw new IllegalArgumentException("This data type does not allow filter "
        + filter.toString());
  }

}
