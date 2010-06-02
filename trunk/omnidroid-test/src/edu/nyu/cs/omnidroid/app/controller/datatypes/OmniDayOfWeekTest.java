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

import java.util.GregorianCalendar;

import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDayOfWeek;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate.Filter;
import edu.nyu.cs.omnidroid.app.controller.util.DataTypeValidationException;

import junit.framework.TestCase;

/**
 *
 */
public class OmniDayOfWeekTest extends TestCase {
  private OmniDayOfWeek dayOfWeek;

  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    dayOfWeek = new OmniDayOfWeek("Sunday");
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDayOfWeek#matchFilter(
   *  edu.nyu.cs.omnidroid.app.controller.datatypes.DataType.Filter, 
   *  edu.nyu.cs.omnidroid.app.controller.datatypes.DataType)}
   * .
   */
  public void testMatchFilter() {
    try {
      dayOfWeek.matchFilter(Filter.AFTER, dayOfWeek);
    } catch (IllegalArgumentException e) {
      return;
    }
    fail("Expected IllegalArgumentException but did not receive it.");
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDayOfWeek#getValue()}.
   */
  public void testGetValue() {
    assertTrue(dayOfWeek.getValue().equalsIgnoreCase("Sunday"));
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDayOfWeek#toString()}.
   */
  public void testToString() {
    assertTrue(dayOfWeek.getValue().equalsIgnoreCase("Sunday"));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDayOfWeek#OmniDayOfWeek(java.lang.String)}.
   */
  public void testOmniDayOfWeek_illegalValue() {
    try{
      new OmniDayOfWeek("Wrong value");
    } catch (IllegalArgumentException ex){
      return;
    }
    fail("Expected IllegalArgumentException but did not receive it.");
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDayOfWeek#getDayOfWeek()}.
   */
  public void testGetDayOfWeek() {
    assertEquals(dayOfWeek.getDayOfWeek(), GregorianCalendar.SUNDAY);
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDayOfWeek#validateUserDefinedValue(
   *  edu.nyu.cs.omnidroid.app.controller.datatypes.DataType.Filter, java.lang.String)}
   * .
   */
  public void testValidateUserDefinedValueFilterString_illegalValue() {
    try {
      OmniDayOfWeek.validateUserDefinedValue(Filter.AFTER, "Value");
    } catch (IllegalArgumentException e) {
      return;
    } catch (DataTypeValidationException e) {
      return;
    }
    fail("Expected IllegalArgumentException or DataTypeValidationException "
        + " but did not receive it.");
  }

}
