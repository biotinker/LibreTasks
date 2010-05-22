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

package edu.nyu.cs.omnidroid.app.core.datatypes;

import java.io.IOException;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.Suppress;

import edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea.Filter;
import edu.nyu.cs.omnidroid.app.external.attributes.EventMonitoringService;
import edu.nyu.cs.omnidroid.app.util.DataTypeValidationException;

/**
 * OmniArea test class.
 * 
 * IMPORTANT NOTE: Must be executed on the Android Device and not emulator.
 */
public class OmniAreaTest extends AndroidTestCase {
  private OmniArea oaNYU, oaCurrent, oaPortAuthority;
  private static final String NYU_ADDRESS = "251 Mercer St, New York, NY";
  private static final double NYU_RADIUS = 0.3;
  private static final double NYU_LONGITUDE = -73.9957865;
  private static final double NYU_LATITUDE = 40.7279793;
  private static final String PORT_AUTHORITY_ADDRESS = "625 8th Ave, New York, NY";
  private static final double PORT_AUTHORITY_RADIUS = 0.125;
  private static final double PORT_AUTHORITY_LONGITUDE = -73.9906634;
  private static final double PORT_AUTHORITY_LATITUDE = 40.756073;
  private static final String CURRENT_LOCATION_ADDRESS = "2 5th ave, New York, NY";
  private static final double CURRENT_LOCATION_RADIUS = 0.003106856;
  private static final double CURRENT_LOCATION_LONGITUDE = -73.997017;
  private static final double CURRENT_LOCATION_LATITUDE = 40.73198;

  /*
   * (non-Javadoc)
   * 
   * @see android.test.AndroidTestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();

    oaNYU = new OmniArea(NYU_ADDRESS, NYU_LONGITUDE, NYU_LATITUDE, NYU_RADIUS);
    oaCurrent = new OmniArea(CURRENT_LOCATION_ADDRESS, CURRENT_LOCATION_LONGITUDE,
        CURRENT_LOCATION_LATITUDE, CURRENT_LOCATION_RADIUS);
    oaPortAuthority = new OmniArea(PORT_AUTHORITY_ADDRESS, PORT_AUTHORITY_LONGITUDE,
        PORT_AUTHORITY_LATITUDE, PORT_AUTHORITY_RADIUS);
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#getFilterFromString(java.lang.String)}.
   */
  public void testGetFilterFromString() {
    // Valid
    assertEquals(OmniArea.getFilterFromString("near"), Filter.NEAR);
    assertEquals(OmniArea.getFilterFromString("away"), Filter.AWAY);

    // Invalid
    try {
      OmniArea.getFilterFromString("Wrong Filter name");
    } catch (IllegalArgumentException e) {
      return;
    }
    fail("Should have thrown IllegalArgumentException as the wrong filter was given.");
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#OmniArea(java.lang.String)}
   * . Test method for {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#toString()} .
   * 
   * @throws DataTypeValidationException
   */
  public void testConstructorString() throws DataTypeValidationException {
    // Valid
    String omniAreaString = oaNYU.toString();
    OmniArea omniArea = new OmniArea(omniAreaString);
    assertEquals(omniArea, oaNYU);

    // Invalid
    try {
      new OmniArea("asdfasdf");
    } catch (DataTypeValidationException e) {
      return;
    }
    fail("Should have thrown invalid DataTypeValidationException.");
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#equals(java.lang.Object)} .
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#OmniArea 
   * (java.lang.String, double, double, double)} .
   * Test method for {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#hashCode()} .
   * 
   * @throws DataTypeValidationException
   */
  public void testEqualsObject() throws DataTypeValidationException {
    // Valid
    OmniArea oaNYU1 = new OmniArea(NYU_ADDRESS, NYU_LONGITUDE, NYU_LATITUDE, NYU_RADIUS);
    assertEquals(oaNYU, oaNYU1);
    assertEquals(oaNYU.hashCode(), oaNYU1.hashCode());

    // Invalid
    assertNotSame(oaNYU, oaCurrent);
    assertNotSame(oaNYU.hashCode(), oaCurrent.hashCode());
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.core.datatypes.OmniArea#OmniArea 
   * (edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea)} .
   * @throws DataTypeValidationException 
   */
  public void testConstructorOmniArea() throws DataTypeValidationException {
    OmniArea omniArea = new OmniArea(oaNYU);

    assertEquals(omniArea.getUserInput(), NYU_ADDRESS);
    assertEquals(omniArea.getLongitude(), NYU_LONGITUDE);
    assertEquals(omniArea.getLatitude(), NYU_LATITUDE);
    assertEquals(omniArea.getProximityDistance(), NYU_RADIUS);
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.core.datatypes.OmniArea#getUserInput()}.
   */
  public void testGetUserInput() {
    assertEquals(oaNYU.getUserInput(), NYU_ADDRESS);
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#getOmniArea (android.content.Context, 
   * java.lang.String, double)} .
   * 
   * @throws IOException
   * @throws IllegalArgumentException
   */
  public void testGetOmniArea() throws IllegalArgumentException, IOException {
    /*
     * getOmniArea requires Geocoder, which does not work correctly on emulator. Therefore the test
     * will pass automatically if EXECUTING_ON_EMULATOR flag is true.
     */
    if(EventMonitoringService.EXECUTING_ON_EMULATOR) {
      return;
    }
    
    // Valid
    OmniArea omniArea = OmniArea.getOmniArea(getContext(), NYU_ADDRESS, NYU_RADIUS);
    assertEquals(oaNYU, omniArea);

    // Invalid
    try {
      omniArea = OmniArea.getOmniArea(getContext(), NYU_ADDRESS, -NYU_RADIUS);
      fail("IllegalArgumentException should have been thrown.");
    } catch (IllegalArgumentException e) {
    }

    try {
      omniArea = OmniArea.getOmniArea(getContext(), "", NYU_RADIUS);
      fail("IllegalArgumentException should have been thrown.");
    } catch (IllegalArgumentException e) {
    }
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#matchFilter 
   * (edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea.Filter,
   *  edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea)}
   * . Test method for
   * {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#getDistance 
   * (edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea, edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea)}
   * .
   */
  public void testMatchFilterFilterOmniArea() throws DataTypeValidationException {
    assertTrue(oaCurrent.matchFilter(Filter.NEAR, oaNYU));
    assertFalse(oaCurrent.matchFilter(Filter.NEAR, oaPortAuthority));
    assertTrue(oaCurrent.matchFilter(Filter.AWAY, oaPortAuthority));
    assertFalse(oaCurrent.matchFilter(Filter.AWAY, oaNYU));
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#getLatitude()}.
   */
  public void testGetLatitude() {
    assertEquals(oaNYU.getLatitude(), NYU_LATITUDE);
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#getLongitude()}.
   */
  public void testGetLongitude() {
    assertEquals(oaNYU.getLongitude(), NYU_LONGITUDE);
  }

  /**
   * Test method for {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#getProximityDistance()}.
   */
  public void testGetProximityDistance() {
    assertEquals(oaNYU.getProximityDistance(), NYU_RADIUS);
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#validateUserDefinedValue 
   * (edu.nyu.cs.omnidroid.app.core.datatypes.DataType.Filter, java.lang.String)}
   * .
   */
  public void testValidateUserDefinedValueFilterString() {
    // Invalid
    try {
      OmniArea.validateUserDefinedValue(Filter.NEAR, null);
    } catch (IllegalArgumentException e) {
      return;
    } catch (DataTypeValidationException e) {
      return;
    }

    try {
      OmniArea.validateUserDefinedValue(OmniDate.Filter.AFTER, NYU_ADDRESS);
    } catch (IllegalArgumentException e) {
      return;
    } catch (DataTypeValidationException e) {
      return;
    }

    fail("Should have thrown an exception.");
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#isValidFilter(java.lang.String)}.
   */
  public void testIsValidFilter() {
    // Valid
    assertTrue(OmniArea.isValidFilter("near"));
    assertTrue(OmniArea.isValidFilter("away"));

    // Invalid
    assertFalse(OmniArea.isValidFilter("wrong value"));
  }

  /**
   * Test method for
   * {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#matchFilter 
   * (edu.nyu.cs.omnidroid.app.core.datatypes.DataType.Filter,
   *  edu.nyu.cs.omnidroid.app.core.datatypes.DataType)}
   * . Test method for
   * {@link edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea#getDistance 
   * (edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea, edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea)}
   * .
   */
  public void testMatchFilterFilterDataType() {
    assertTrue(oaCurrent.matchFilter((DataType.Filter) Filter.NEAR, (DataType) oaNYU));
    assertFalse(oaCurrent.matchFilter((DataType.Filter) Filter.NEAR, (DataType) oaPortAuthority));
    assertTrue(oaCurrent.matchFilter((DataType.Filter) Filter.AWAY, (DataType) oaPortAuthority));
    assertFalse(oaCurrent.matchFilter((DataType.Filter) Filter.AWAY, (DataType) oaNYU));
    try {
      oaCurrent.matchFilter(OmniDate.Filter.AFTER, (DataType) oaNYU);
    } catch (IllegalArgumentException e) {
      return;
    }
    fail("Should have thrown an exception.");
  }

}
