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
 * UI representation of a base class for Event/Filter/ Attribute/Action. We'll modify this to match
 * the database model. These should be lightweight representation of these objects.
 */
public abstract class ModelItem {

  protected final String mTypeName;
  protected final String mDescription;
  protected final int mIconResId;

  public ModelItem(String typeName, 
                   String description, 
                   int iconResId) 
  {
    mTypeName = typeName;
    mDescription = description;
    mIconResId = iconResId;
  }

  public String getTypeName() {
    return mTypeName;
  }

  public String getDescription() {
    return mDescription;
  }

  public int getIconResId() {
    return mIconResId;
  }

  public abstract String getDescriptionShort();

  public String toString() {
    String str = mTypeName + ", " + mDescription + ", " + mIconResId;
    return str;
  }
}