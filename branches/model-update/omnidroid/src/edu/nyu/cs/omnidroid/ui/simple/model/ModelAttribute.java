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
 * UI representation of an attribute. Attributes are associated
 * with an event.
 */
public class ModelAttribute extends ModelItem {
	
  private final int mDatabaseId;
  private final int mForeignKeyEventId;
  private final int mDatatype;
	
	
  public ModelAttribute(int databaseId,
                        int foreignKeyEventId,
                        int datatype,
                        String typeName, 
                        String description, 
                        int iconResId) 
  {
    super(typeName, description, iconResId);
    mDatabaseId = databaseId;
    mForeignKeyEventId = foreignKeyEventId;
    mDatatype = datatype;
  }

  public int getDatabaseId() { 
    return mDatabaseId;
  }
	
  public int getForeignKeyEventId() {
    return mForeignKeyEventId;
  }
	
  public int getDatatype() {
    return mDatatype;
  }
	
  public String getDescriptionShort() {
    return getTypeName();
  }
}