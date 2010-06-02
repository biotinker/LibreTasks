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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.nyu.cs.omnidroid.app.controller.datatypes.DataType;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDayOfWeek;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate.Filter;
import edu.nyu.cs.omnidroid.app.controller.util.DataTypeValidationException;

import junit.framework.TestCase;

public class OmniDateTest extends TestCase {
  private OmniDate omniDate;
  private static String TODAY = "2009-05-11 11:00:00";
  private static String YESTERDAY = "2009-05-10 11:00:00";
  private static String TOMORROW = "2009-05-12 11:00:00";
  private static String APRIL1998 = "1998-04-04 14:24:30";

  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  @Override
  protected void setUp() throws Exception {
      omniDate = new OmniDate(TODAY);
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate#OmniDate(java.util.Date)}.
   */
  public void testOmniDateDate() {
    new OmniDate(new Date());
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate#OmniDate(java.lang.String)}
   * .
   * 
   * @throws DataTypeValidationException
   * @throws ParseException 
   */
  public void testOmniDateString() throws DataTypeValidationException, ParseException {
    Date date = OmniDate.dateFormat.parse(APRIL1998);
    OmniDate omniDate = new OmniDate(APRIL1998);
    assertEquals(omniDate.getDate(), date);
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate#OmniDate(java.lang.String)}
   * . Tests to see if the function throws the
   */
  public void testOmniDateString_invalidFormat() {
    try {
      new OmniDate("5/11/1983 14:24:30");
    } catch (DataTypeValidationException e) {
      return;
    }
    fail("Should have thrown invalid DataTypeValidationException.");
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate#getDate(java.lang.String)}.
   * @throws Exception 
   */
  public void testGetDate() throws Exception {
      Date date = OmniDate.getDate(APRIL1998);
      assertEquals(date, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(APRIL1998));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate#matchFilter(DataType.Filter, OmniDate)}
   * .
   * @throws DataTypeValidationException 
   */
  public void testMatchFilter_after() throws DataTypeValidationException {
    assertTrue(omniDate.matchFilter(Filter.AFTER, new OmniDate(YESTERDAY)));
    assertFalse(omniDate.matchFilter(Filter.AFTER, new OmniDate(TOMORROW)));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate#matchFilter(DataType.Filter, OmniDate)}
   * .
   * @throws DataTypeValidationException 
   */
  public void testMatchFilter_before() throws DataTypeValidationException {
    assertTrue(omniDate.matchFilter(Filter.BEFORE, new OmniDate(TOMORROW)));
    assertFalse(omniDate.matchFilter(Filter.BEFORE, new OmniDate(YESTERDAY)));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate#matchFilter(DataType.Filter, OmniDayOfWeek)}
   * .
   * @throws DataTypeValidationException 
   */
  public void testMatchFilter_isDayOfWeek() {
    assertTrue( omniDate.matchFilter(Filter.ISDAYOFWEEK, new OmniDayOfWeek("MONDAY")));
    assertFalse( omniDate.matchFilter(Filter.ISDAYOFWEEK, new OmniDayOfWeek("SUNDAY")));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate#validateUserDefinedValue(DataType.Filter,
   * java.lang.String)}.
   * 
   * @throws DataTypeValidationException
   * @throws IllegalArgumentException
   */
  public void testValidateUserDefinedValue() throws IllegalArgumentException,
      DataTypeValidationException {
    OmniDate.validateUserDefinedValue(Filter.AFTER, TOMORROW);
    OmniDate.validateUserDefinedValue(Filter.BEFORE, YESTERDAY);
    OmniDate.validateUserDefinedValue(Filter.ISDAYOFWEEK, "sunday");
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate#validateUserDefinedValue(DataType.Filter, 
   *  java.lang.String)}.
   */
  public void testValidateUserDefinedValue_invalidInput() {
    try {
      OmniDate.validateUserDefinedValue(Filter.AFTER, "adsfadsf");
    } catch (IllegalArgumentException e) {
      return;
    } catch (DataTypeValidationException e) {
      return;
    }
    fail("Should have thrown an exception.");
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate#isValidFilter(java.lang.String)}.
   */
  public void testIsValidFilter() {
    assertTrue(OmniDate.isValidFilter("after"));
    assertTrue(OmniDate.isValidFilter("before"));
    assertTrue(OmniDate.isValidFilter("isdayofweek"));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate#isValidFilter(java.lang.String)}.
   */
  public void testIsValidFilter_invalidFilter() {
    assertFalse(OmniDate.isValidFilter("wrong value"));
  }
  
  public void testGetFilterFromString() {
    assertEquals(OmniDate.getFilterFromString("after"), Filter.AFTER);
    assertEquals(OmniDate.getFilterFromString("before"), Filter.BEFORE);
    assertEquals(OmniDate.getFilterFromString("isDayOfWeek"), Filter.ISDAYOFWEEK);
  }
  
  public void testGetFilterFromString_invalidArgument(){
    try {
      OmniDate.getFilterFromString("Wrong Filter name");
    } catch (IllegalArgumentException e) {
      return;
    }
    fail("Should have thrown IllegalArgumentException as the wrong filter was given.");
  }

}
