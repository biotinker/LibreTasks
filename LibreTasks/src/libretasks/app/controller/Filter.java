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
package libretasks.app.controller;

import android.util.Log;
import libretasks.app.controller.datatypes.DataType;
import libretasks.app.controller.datatypes.FactoryDataType;

/**
 * This class contains a user defined filter, which will be checked against an event's attribute
 * data to see if the event matches the user defined {@link Rule}.
 */
public class Filter {
  /** Filter parameters used to construct two OmniType objects and perform a comparison */
  public final String eventAttribute;
  public final String filterOnDataType;
  public final String filter;
  public final String compareWithDataType;
  public final String compareWithData;

  /**
   * Creates a new {@link Filter} on an event's attribute data
   * 
   * @param eventAttribute
   *          the event attribute data field to be filtered
   * @param filterOnDataType
   *          the Omnidroid class name of the data type of the event attribute field
   * @param filter
   *          the comparison to be made between the event attribute and the user filter data
   * @param compareWithDataTyp
   *          the Omnidroid class name of the data type of the user filter data
   * @param compareWithdata
   *          the user defined data to check against the event attribute
   * @throws IllegalArgumentException
   *           if any parameters are null
   */
  public Filter(String eventAttribute, String filterOnDataType, String filter,
      String compareWithDataType, String compareWithdata) {
    if (eventAttribute == null || filterOnDataType == null || filter == null
        || compareWithDataType == null || compareWithdata == null) {
      throw new IllegalArgumentException();
    }
    this.eventAttribute = eventAttribute;
    this.filterOnDataType = filterOnDataType;
    this.filter = filter;
    this.compareWithDataType = compareWithDataType;
    this.compareWithData = compareWithdata;
  }

  /**
   * Compares the data in the event attribute to the user filter data
   * 
   * @param event
   *          the event whose attributes will be used for the comparison
   * @return true if the event data passes the filter, false otherwise
   */
  public boolean match(Event event) {
    String eventAttributeData = event.getAttribute(eventAttribute);
    //the attribute data we get from the event
    DataType leftHandSide = FactoryDataType.createObject(filterOnDataType, eventAttributeData);
    DataType.Filter comparisonFilter = FactoryDataType.getFilterFromString(filterOnDataType,
        filter);
    Log.d("match", "filterOnDataType is : " + filterOnDataType + " comparison is : " + filter);
    DataType rightHandSide = FactoryDataType.createObject(compareWithDataType, compareWithData);
    return leftHandSide.matchFilter(comparisonFilter, rightHandSide);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Filter)) {
      return false;
    }
    Filter that = (Filter) o;
    return that.compareWithData.equals(compareWithData) && that.filter.equals(filter)
        && that.filterOnDataType.equals(filterOnDataType)
        && that.eventAttribute.equals(eventAttribute);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 37 * result + eventAttribute.hashCode();
    result = 37 * result + filterOnDataType.hashCode();
    result = 37 * result + filter.hashCode();
    result = 37 * result + compareWithDataType.hashCode();
    result = 37 * result + compareWithData.hashCode();
    return result;
  }
}
