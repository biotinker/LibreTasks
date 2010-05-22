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
package edu.nyu.cs.omnidroid.app.core.datatypes;

import edu.nyu.cs.omnidroid.app.core.datatypes.OmniText;
import edu.nyu.cs.omnidroid.app.core.datatypes.OmniText.Filter;
import edu.nyu.cs.omnidroid.app.util.DataTypeValidationException;
import junit.framework.TestCase;

public class OmniTextTest extends TestCase {
  private OmniText omniText;

  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
    omniText = new OmniText("test");
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniText#OmniText(java.lang.Object)}
   * .
   */
  public void testOmniTextObject() {
    omniText = new OmniText(Double.valueOf("1234.45"));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniText#matchFilter(DataType.Filter, DataType)}
   * .
   */
  public void testMatchFilter() {
    assertTrue(omniText.matchFilter(Filter.EQUALS, new OmniText("TEST")));
    assertTrue(omniText.matchFilter(Filter.CONTAINS, new OmniText("ES")));
    assertTrue(omniText.matchFilter(Filter.CONTAINS, new OmniText("")));
    assertFalse(omniText.matchFilter(Filter.CONTAINS, new OmniText("KT")));
    omniText = new OmniText("");
    assertTrue(omniText.matchFilter(Filter.CONTAINS, omniText));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniText#validateUserDefinedValue(java.lang.String, java.lang.String)}
   * .
   */
  public void testValidateUserDefinedValue() {
    try {
      OmniText.validateUserDefinedValue(Filter.CONTAINS, null);
    } catch (IllegalArgumentException e) {
      return;
    } catch (DataTypeValidationException e) {
      return;
    }

    fail("Should of have thrown an exception.");
  }

  public void testGetFilterFromString(){
    assertEquals(OmniText.getFilterFromString("equals"), Filter.EQUALS);
    assertEquals(OmniText.getFilterFromString("Equals"), Filter.EQUALS);
    assertEquals(OmniText.getFilterFromString("contains"), Filter.CONTAINS);
    assertEquals(OmniText.getFilterFromString("Contains"), Filter.CONTAINS);
  }
  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniText#isValidFilter(java.lang.String)}.
   */
  public void testIsValidFilter() {
    assertTrue(OmniText.isValidFilter("equals"));
    assertTrue(OmniText.isValidFilter("contains"));
    assertFalse(OmniText.isValidFilter("Invalid Filter"));
  }

}
