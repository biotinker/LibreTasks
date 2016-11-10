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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import libretasks.app.controller.util.DataTypeValidationException;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

/**
 * Provides location based filtering capabilities.
 */
public class OmniArea extends DataType {
  /** Radius of the area (in miles). */
  private double proximityDistance = Double.NaN;

  private double longitude = Double.NaN;
  private double latitude = Double.NaN;

  /** User provided value. */
  private String userInput;

  public static final double MIN_LONGITUDE = -180;
  public static final double MAX_LONGITUDE = 180;
  public static final double MIN_LATITUDE = -90;
  public static final double MAX_LATITUDE = 90;
  public static final double MILES_IN_A_METER = 0.000621371192;
  private static final String omniAreaOpenTag = "<omniArea>";
  private static final String omniAreaCloseTag = "</omniArea>";
  private static final String latitudeOpenTag = "<latitude>";
  private static final String latitudeCloseTag = "</latitude>";
  private static final String longitudeOpenTag = "<longititude>";
  private static final String longitudeCloseTag = "</longititude>";
  private static final String userInputOpenTag = "<userInput>";
  private static final String userInputCloseTag = "</userInput>";
  private static final String proximityDistanceOpenTag = "<proximityDistance>";
  private static final String proximityDistanceCloseTag = "</proximityDistance>";

  /* data type name to be stored in db */
  public static final String DB_NAME = "Area";
  
  public enum Filter implements DataType.Filter {
    NEAR("near"), AWAY("away");
    
    public final String displayName;
    
    Filter(String displayName) {
      this.displayName = displayName;
    }
  }

  public OmniArea(String omniAreaString) throws DataTypeValidationException {
    OmniArea area = parseOmniArea(omniAreaString);

    init(area);
  }

  public OmniArea(OmniArea area) throws DataTypeValidationException {
    if (area == null) {
      throw new DataTypeValidationException("Parameter area cannot be null.");
    }
    init(area);
  }
  
  private void init(OmniArea area) {
    userInput = area.getUserInput();
    longitude = area.getLongitude();
    latitude = area.getLatitude();
    proximityDistance = area.getProximityDistance();
  }

  /**
   * @param userInput
   *          address entered by user. Stored separately.
   * @param longitude
   *          number degrees east of Greenwich. Values are between -180 and 180. Represents latitude
   *          of userInput.
   * @param latitude
   *          number of degrees above equator. Values are between -90 and 90. Represents longitude
   *          of userInput.
   * @param proximityDistance
   *          the radius of the circular area around the indicated longitude and latitude. Value
   *          must be >= 0.
   * @throws DataTypeValidationException
   *           if values of longitude, latitude, or proximityDistance are improper.
   */
  public OmniArea(String userInput, double longitude, double latitude, double proximityDistance)
      throws DataTypeValidationException {
    if (latitude < MIN_LATITUDE || latitude > MAX_LATITUDE) {
      throw new DataTypeValidationException("Latitude must be between " + MIN_LATITUDE + " and "
          + MAX_LATITUDE + ".");
    }
    if (longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
      throw new DataTypeValidationException("Longitude must be between " + MIN_LONGITUDE + " and "
          + MAX_LONGITUDE + ".");
    }
    if (proximityDistance < 0) {
      throw new DataTypeValidationException("Distance cannot be negative.");
    }

    this.userInput = userInput;
    this.latitude = latitude;
    this.longitude = longitude;
    this.proximityDistance = proximityDistance;
  }

  /**
   * Creates OmniArea object for a given address.
   * 
   * @param context
   *          application context. Must not be null.
   * @param address
   *          address of the location to be identified (a.k.a 1600 Amphitheatre Parkway, Mountain
   *          View, CA 94043).
   * @return OmniArea object for a given address.
   * @throws IOException
   *           if Internet access is unavailable.
   * @throws IllegalArgumentException
   *           if either context or address are null, or address cannot be found.
   */
  public static OmniArea getOmniArea(Context context, String address, double proximityDistance)
      throws IOException, IllegalArgumentException {

    final int MAX_RESULTS_TO_RETURN = 1;
    final int FIRST_RESULT = 0;

    if (context == null) {
      throw new IllegalArgumentException("context cannot be null.");
    }
    if (address == null) {
      throw new IllegalArgumentException("address cannot be null.");
    }

    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
    List<Address> addressResult = geocoder.getFromLocationName(address, MAX_RESULTS_TO_RETURN);
    if (!addressResult.isEmpty()) {
      Address location = addressResult.get(FIRST_RESULT);
      try {
        return new OmniArea(address, location.getLongitude(), location.getLatitude(),
            proximityDistance);
      } catch (DataTypeValidationException e) {
        throw new IllegalArgumentException("address cannot be found.");
      }
    }

    throw new IllegalArgumentException("address cannot be found.");
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object o) throws IllegalArgumentException {
    if (o == null) {
      return false;
    }
    if (!(o instanceof OmniArea)) {
      throw new IllegalArgumentException("Parameter is not an OmniArea");
    }

    OmniArea omniArea = (OmniArea) o;

    if (this.latitude != omniArea.latitude) {
      return false;
    }
    if (this.longitude != omniArea.longitude) {
      return false;
    }
    if (this.proximityDistance != omniArea.proximityDistance) {
      return false;
    }
    if (!this.userInput.equals(omniArea.userInput)) {
      return false;
    }

    return true;
  }

  public String getUserInput() {
    return userInput;
  }

  /**
   * Parses out the text located between first occurrences of the open and closed tags.
   * 
   * @param parseString
   *          string to parse
   * @param openTag
   *          the opening tag
   * @param closeTag
   *          the closing tag
   * @return text located between first occurrences of the first open and closed tags, or null if
   *         proper tags are not found.
   */
  private static String parseTagValue(String parseString, String openTag, String closeTag) {
    // TODO (dvo203): Replace by standard XML parsing methods.
    int beg, end;

    beg = parseString.indexOf(openTag);
    end = parseString.indexOf(closeTag);
    if (beg < 0 || end < 0) {
      return null;
    }
    if (beg > end) {
      return null;
    }
    if (beg + openTag.length() == end) {
      return "";
    }

    return parseString.substring(beg + openTag.length(), end);
  }

  /**
   * Parses out and converts the text located between first occurrences of the open and closed tags
   * into double.
   * 
   * @param parseString
   *          string to parse
   * @param openTag
   *          the opening tag
   * @param closeTag
   *          the closing tag
   * @param exception
   *          exception to throw in case of conversion error.
   * @return double value of the text located between first occurrences of the open and closed tags
   * @throws DataTypeValidationException
   *           if value cannot be converted to double, or proper tags are not found.
   */
  private static double parseDoubleValue(String parseString, String openTag, String closeTag,
      DataTypeValidationException exception) throws DataTypeValidationException {

    String tagValue;
    tagValue = parseStringValue(parseString, openTag, closeTag, exception);

    double doubleValue;
    try {
      doubleValue = Double.parseDouble(tagValue);
    } catch (NumberFormatException e) {
      throw e;
    }

    return doubleValue;
  }

  /**
   * Parses out text located between first occurrences of the open and closed tags.
   * 
   * @param parseString
   *          string to parse
   * @param openTag
   *          the opening tag
   * @param closeTag
   *          the closing tag
   * @param exception
   *          exception to throw in case of conversion error.
   * @return text located between first occurrences of the open and closed tags.
   * @throws DataTypeValidationException
   *           if proper tags are not found.
   */
  private static String parseStringValue(String parseString, String openTag, String closeTag,
      DataTypeValidationException exception) throws DataTypeValidationException {
    // Temporary variable that holds the text of the parsed tag
    String tagValue;
    tagValue = parseTagValue(parseString, openTag, closeTag);
    if (tagValue == null) {
      throw exception;
    }
    return tagValue;
  }

  private static OmniArea parseOmniArea(String omniAreaString) throws DataTypeValidationException {
    final DataTypeValidationException validationFailed = new DataTypeValidationException(
        "String is not an OmniArea.");

    // Parse OmniArea
    String omniAreaBody = parseStringValue(omniAreaString, omniAreaOpenTag, omniAreaCloseTag,
        validationFailed);

    // Parse latitude
    double latitude = parseDoubleValue(omniAreaBody, latitudeOpenTag, latitudeCloseTag,
        validationFailed);

    // Parse longitude
    double longitude = parseDoubleValue(omniAreaBody, longitudeOpenTag, longitudeCloseTag,
        validationFailed);

    // Parse proximityDistance
    double proximityDistance = parseDoubleValue(omniAreaBody, proximityDistanceOpenTag,
        proximityDistanceCloseTag, validationFailed);

    // Parse userInput
    String userInput = parseStringValue(omniAreaBody, userInputOpenTag, userInputCloseTag,
        validationFailed);

    // create and return OmniArea object
    return new OmniArea(userInput, longitude, latitude, proximityDistance);
  }

  /**
   * Returns Filter represented by filterName.
   * 
   * @param filterName
   *          the filter name.
   * @return Filter represented by filterName
   * @throws IllegalArgumentException
   *           when the filter with the given name does not exist.
   */
  public static Filter getFilterFromString(String filterName) throws IllegalArgumentException {
    return Filter.valueOf(filterName.toUpperCase());
  }

  public boolean matchFilter(Filter filter, OmniArea compareValue) {
    switch (filter) {
    case NEAR:
      return isNear(compareValue);
    case AWAY:
      return isAway(compareValue);
    default:
      return false;
    }
  }

  /**
   * Determine distance (in miles) between 2 OmniAreas.
   * 
   * @param pointA
   *          first point
   * @param pointB
   *          second point
   * @return distance (in miles) between pointA and pointB.
   */
  public static double getDistance(OmniArea pointA, OmniArea pointB) {
    final int DISTANCE = 0;
    final int DISTANCE_ONLY = 1;

    float[] results = new float[DISTANCE_ONLY];

    Location.distanceBetween(pointA.getLatitude(), pointA.getLongitude(), pointB.getLatitude(),
        pointB.getLongitude(), results);

    // convert distance from meters to miles
    return results[DISTANCE] * MILES_IN_A_METER;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  private boolean isAway(OmniArea compareValue) {
    return !isNear(compareValue);
  }

  private boolean isNear(OmniArea compareValue) {
    double minimumDistanceToIntersect = compareValue.getProximityDistance() + proximityDistance;
    if (getDistance(this, compareValue) <= minimumDistanceToIntersect) {
      return true;
    }

    return false;
  }

  /** Returns OmniArea radius. */
  public double getProximityDistance() {
    return proximityDistance;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.nyu.cs.omnidroid.core.datatypes.DataType#validateUserDefinedValue (java.lang.String,
   * java.lang.String)
   */
  public static void validateUserDefinedValue(DataType.Filter filter, String userInput)
      throws DataTypeValidationException, IllegalArgumentException {

    if (!(filter instanceof Filter)) {
      throw new IllegalArgumentException("Invalid filter type '" + filter.toString()
          + "' provided.");
    }
    if (userInput == null) {
      throw new DataTypeValidationException("The user input cannot be null.");
    }
    // TODO (dvo203): The value must be validated here.
  }

  /**
   * Indicates whether or not the given filter is supported by the data type.
   * 
   * @param filter
   * @return true if the filter is supported, false otherwise.
   */
  public static boolean isValidFilter(String filter) {
    try {
      getFilterFromString(filter);
    } catch (IllegalArgumentException e) {
      return false;
    }

    return true;
  }

  /**
   * Provides the string representation of the OmniLocation for storage and later recreation.
   */
  public String toString() {
    // TODO (dvo203): Replace by standard XML construction methods.
    return omniAreaOpenTag + latitudeOpenTag + latitude + latitudeCloseTag + longitudeOpenTag
        + longitude + longitudeCloseTag + userInputOpenTag + userInput + userInputCloseTag
        + proximityDistanceOpenTag + proximityDistance + proximityDistanceCloseTag
        + omniAreaCloseTag;
  }

  // TODO (dvo203): move string to strings.xml
  public String getValue() {
    return "within " + proximityDistance + " miles of " + userInput;
  }

  @Override
  public boolean matchFilter(DataType.Filter filter, DataType userDefinedValue)
      throws IllegalArgumentException {
    if (!(filter instanceof Filter)) {
      throw new IllegalArgumentException("Invalid filter " + filter.toString() + " provided.");
    }
    if (userDefinedValue instanceof OmniArea) {
      return matchFilter((Filter) filter, (OmniArea) userDefinedValue);
    }

    throw new IllegalArgumentException("Matching filter not found for the datatype "
        + userDefinedValue.getClass().toString() + ". ");
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return userInput.hashCode() + (int)proximityDistance + (int)longitude + (int)latitude;
  }

}
