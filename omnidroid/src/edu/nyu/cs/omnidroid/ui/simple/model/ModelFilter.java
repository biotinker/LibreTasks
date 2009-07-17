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


/**
 * UI representation of a filter. 
 */
public class ModelFilter extends ModelItem {

  private final int mDatabaseId;
  
  /** The attribute this filter is associated with. */
  private final ModelAttribute mAttribute;

  
  public ModelFilter(String typeName, 
                     String description, 
                     int iconResId,
                     int databaseId,
                     ModelAttribute attribute) 
  {
    super(typeName, description, iconResId);
    mDatabaseId = databaseId;
    mAttribute = attribute;
  }

  public int getDatabaseId() {
    return mDatabaseId;
  }

  public ModelAttribute getAttribute() {
    return mAttribute;
  }

  public String getDescriptionShort() {
    return getAttribute().getDescriptionShort() + " " + getTypeName();
  }
}