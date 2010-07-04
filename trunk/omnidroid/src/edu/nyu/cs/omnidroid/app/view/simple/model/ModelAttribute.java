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
package edu.nyu.cs.omnidroid.app.view.simple.model;

/**
 * UI representation of an attribute. Attributes are associated with an event.
 */
public class ModelAttribute extends ModelItem {

  private final long foreignKeyEventId;
  private final long datatype;

  public ModelAttribute(long databaseId, long foreignKeyEventId, long datatype, String typeName,
      String description, int iconResId) {
    super(typeName, description, iconResId, databaseId);
    this.foreignKeyEventId = foreignKeyEventId;
    this.datatype = datatype;
  }

  public long getForeignKeyEventId() {
    return foreignKeyEventId;
  }

  public long getDatatype() {
    return datatype;
  }
}