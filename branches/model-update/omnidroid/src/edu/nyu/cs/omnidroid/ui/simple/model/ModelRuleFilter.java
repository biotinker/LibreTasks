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
 * An instance of a filter whose data has been set by the end-user.
 * When a user picks a <code>ModelFilter</code> filter to apply to
 * an attribute, we prompt them for input data about how to use the
 * filter. Their supplied information is stored in <code>mFilterData</code>
 * and the original filter type is stored in <code>mModemFilter</code>.
 * 
 * So this class contains the filter template, and whatever data the
 * user supplied for it.
 */
public class ModelRuleFilter extends ModelItem {

  /** Our database instance ID, set when our parent rule is saved in the database. */
  private final int mDatabaseId;
  
  /** Our filter type. */
  private final ModelFilter mModelFilter;
  
  /** Filter data entered by the end user. */
  private DataType mFilterData;


  public ModelRuleFilter(int databaseId, 
                         ModelFilter modelFilter,
                         DataType filterData) 
  {
    super(modelFilter.getTypeName(), modelFilter.getDescription(), modelFilter.getIconResId());
    mDatabaseId = databaseId;
    mModelFilter = modelFilter;
    mFilterData = filterData;
  }

  public int getDatabaseId() {
    return mDatabaseId;
  }
  
  public ModelFilter getModelFilter() {
	  return mModelFilter;
  }

  public DataType getData() {
    return mFilterData;
  }

  public String getDescriptionShort() {
    return mModelFilter.getAttribute().getDescriptionShort() 
      + " " + mModelFilter.getTypeName() 
      + ": " + getData().getValue();
  }
}