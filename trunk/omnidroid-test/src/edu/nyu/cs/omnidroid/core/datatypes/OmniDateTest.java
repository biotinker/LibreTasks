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

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.nyu.cs.omnidroid.util.DataTypeValidationException;

import junit.framework.TestCase;

public class OmniDateTest extends TestCase {
  private OmniDate omniDate;

  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
      omniDate = new OmniDate("2009-05-11 11:00:00");
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.core.datatypes.OmniDate#OmniDate(java.util.Date)}.
   */
  public void testOmniDateDate() {
    new OmniDate(new Date());
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.core.datatypes.OmniDate#OmniDate(java.lang.String)}
   * .
   * @throws DataTypeValidationException 
   */
  public void testOmniDateString() throws DataTypeValidationException {
      new OmniDate("1998-04-04 14:24:30");
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.core.datatypes.OmniDate#OmniDate(java.lang.String)}
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
   * Test method for {@link edu.nyu.cs.omnidroid.core.datatypes.OmniDate#getDate(java.lang.String)}.
   * @throws Exception 
   */
  public void testGetDate() throws Exception {
      Date date = OmniDate.getDate("1998-04-04 14:24:30");
      assertEquals(date, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse("1998-04-04 14:24:30"));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.core.datatypes.OmniDate#matchFilter(java.lang.String, java.lang.String)}
   * .
   */
  public void testMatchFilter_after() {
    assertTrue(omniDate.matchFilter("after", "2009-05-10 1:00:00"));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.core.datatypes.OmniDate#matchFilter(java.lang.String, java.lang.String)}
   * .
   */
  public void testMatchFilter_before() {
    assertTrue(omniDate.matchFilter("before", "2009-05-12 1:00:00"));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.core.datatypes.OmniDate#validateUserDefinedValue(java.lang.String, java.lang.String)}
   * .
   */
  public void testValidateUserDefinedValue_invalidInput() {
    try {
      omniDate.validateUserDefinedValue("after", "adsfadsf");
    } catch (IllegalArgumentException e) {
      return;
    } catch (DataTypeValidationException e) {
      return;
    }
    fail("Should have thrown an exception.");
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.core.datatypes.OmniDate#isValidFilter(java.lang.String)}.
   */
  public void testIsValidFilter() {
    assertTrue(OmniDate.isValidFilter("after"));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.core.datatypes.OmniDate#isValidFilter(java.lang.String)}.
   */
  public void testIsValidFilter_invalidFilter() {
    assertFalse(OmniDate.isValidFilter("wrong value"));
  }
}
