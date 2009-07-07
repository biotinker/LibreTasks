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

import junit.framework.TestCase;

/**
 *
 */
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
   * {@link edu.nyu.cs.omnidroid.core.datatypes.OmniPhoneNumber#matchFilter(java.lang.String, java.lang.String)}
   * .
   */
  public void testMatchFilter_equals() {
    assertTrue(omniPhoneNumber.matchFilter("equals", "555-555-5555"));
    assertTrue(omniPhoneNumber.matchFilter("equals", "1-5555555555"));
    assertFalse(omniPhoneNumber.matchFilter("equals", "123-345-2342"));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.core.datatypes.OmniPhoneNumber#matchFilter(java.lang.String, java.lang.String)}
   * .
   */
  public void testMatchFilter_invalidArgument() {
    try {
      omniPhoneNumber.matchFilter("wrong filter", "1");
    } catch (IllegalArgumentException e) {
      return;
    }
    fail("Should have thrown IllegalArgumentException.");
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.core.datatypes.OmniPhoneNumber#validateUserDefinedValue(java.lang.String, java.lang.String)}
   * .
   */
  public void testValidateUserDefinedValue() {
    try {
      omniPhoneNumber.validateUserDefinedValue("equals", "123456");
    } catch (IllegalArgumentException e) {
      fail(e.getMessage());
    } catch (DataTypeValidationException e) {
      fail(e.getMessage());
    }
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
    assertNotNull(omniPhoneNumber.toString());
  }

}
