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
/**
 * 
 */
package edu.nyu.cs.omnidroid.core.datatypes;

import edu.nyu.cs.omnidroid.core.datatypes.OmniPhoneNumber.Filter;
import edu.nyu.cs.omnidroid.util.DataTypeValidationException;
import junit.framework.TestCase;

public class OmniPhoneNumberTest extends TestCase {
  private OmniPhoneNumber omniPhoneNumber;

  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
    omniPhoneNumber = new OmniPhoneNumber("(555)-555-5555");
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.core.datatypes.OmniPhoneNumber#matchFilter(DataType.Filter, 
   *  OmniPhoneNumber)}.
   * 
   * @throws DataTypeValidationException
   */
  public void testMatchFilter_equals() throws DataTypeValidationException {
    assertTrue(omniPhoneNumber.matchFilter(Filter.EQUALS, new OmniPhoneNumber("555-555-5555")));
    assertTrue(omniPhoneNumber.matchFilter(Filter.EQUALS, new OmniPhoneNumber("1-5555555555")));
    assertFalse(omniPhoneNumber.matchFilter(Filter.EQUALS, new OmniPhoneNumber("123-345-2342")));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.core.datatypes.OmniPhoneNumber#matchFilter(DataType.Filter, 
   *  DataType)}.
   * 
   * @throws DataTypeValidationException
   */
  public void testMatchFilter_invalidArgument() throws DataTypeValidationException {
    try {
      omniPhoneNumber.matchFilter(Filter.EQUALS, new OmniDate("2008-05-11 11:00:00"));
    } catch (IllegalArgumentException e) {
      return;
    }
    fail("Should have thrown IllegalArgumentException.");
  }

  public void testGetFilterFromString() {
    assertEquals(OmniPhoneNumber.getFilterFromString("equals"), Filter.EQUALS);
    assertEquals(OmniPhoneNumber.getFilterFromString("Equals"), Filter.EQUALS);
  }

  public void testGetFilterFromString_invalidArgument(){
    try {
      OmniPhoneNumber.getFilterFromString("Wrong Filter name");
    } catch (IllegalArgumentException e) {
      return;
    }
    fail("Should have thrown IllegalArgumentException as the wrong filter was given.");
  }
  
  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.core.datatypes.OmniPhoneNumber#isValidFilter(java.lang.String)}.
   */
  public void testIsValidFilter() {
    assertTrue(OmniPhoneNumber.isValidFilter("equals"));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.core.datatypes.OmniPhoneNumber#isValidFilter(java.lang.String)}.
   */
  public void testIsValidFilter_invalidFilter() {
    assertFalse(OmniPhoneNumber.isValidFilter("Not a valid filter."));
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.core.datatypes.OmniPhoneNumber#toString()}.
   */
  public void testToString() {
    assertEquals(omniPhoneNumber.toString(), "(555)-555-5555");
  }

}
