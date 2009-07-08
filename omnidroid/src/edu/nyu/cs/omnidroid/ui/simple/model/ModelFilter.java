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
package edu.nyu.cs.omnidroid.ui.simple.model;

import edu.nyu.cs.omnidroid.core.datatypes.DataType;

/**
 * UI representation of a filter. We'll modify this to match the database model. Filters are
 * associated with an attribute.
 */
public class ModelFilter extends ModelItem {

  private final int mDatabaseId;
  private final ModelAttribute mAttribute;
  private final DataType mFilterData;

  /**
   * filterData may be null.
   */
  public ModelFilter(int databaseId, 
                     String typeName, 
                     String description, 
                     int iconResId,
                     ModelAttribute attribute, 
                     DataType filterData) 
  {
    super(typeName, description, iconResId);
    mDatabaseId = databaseId;
    mAttribute = attribute;
    mFilterData = filterData;
  }

  public int getDatabaseId() {
    return mDatabaseId;
  }

  public ModelAttribute getAttribute() {
    return mAttribute;
  }

  public DataType getFilterData() {
    return mFilterData;
  }

  public String getDescriptionShort() {
    if (getAttribute() == null || getFilterData() == null) {
      return getTypeName();
    } 
    else {
      return getAttribute().getDescriptionShort() + " " + getTypeName() + ": "
          + getFilterData().getValue();
    }
  }
}