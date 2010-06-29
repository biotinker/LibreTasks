/*******************************************************************************
 * Copyright 2009, 2010 OmniDroid - http://code.google.com/p/omnidroid 
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
 * Base class for a representation of core model items in the UI.
 */
public class ModelItem implements Comparable<ModelItem> {

  /** Database Row ID. */
  protected final long databaseId;

  /** Simple name of class for presentation to the user in the UI. */
  protected final String typeName;

  /** Description of the instance for use with a help button in the UI. */
  protected final String description;

  /** Icon resource ID to use in the UI for this instance (may switch to path). */
  protected final int iconResId;

  public ModelItem(String typeName, String description, int iconResId, long databaseId) {
    this.typeName = typeName;
    this.description = description;
    this.iconResId = iconResId;
    this.databaseId = databaseId;
  }

  public String getTypeName() {
    return typeName;
  }

  public String getDescription() {
    return description;
  }

  public int getIconResId() {
    return iconResId;
  }

  public long getDatabaseId() {
    return databaseId;
  }

  /**
   * Each derived class should build a short description of themselves here using their type name
   * and any other relevant class data. This string will be used to describe the item in the tree
   * listview which has very limited screen space.
   * 
   * @return Short description of this instance for use in the UI.
   */
  public String getDescriptionShort() {
    return typeName;
  }

  public String toString() {
    String str = typeName + ", " + description + ", " + iconResId;
    return str;
  }

  /**
   * Compare toString() methods.
   */
  public int compareTo(ModelItem anotherItem) {
    return this.toString().compareTo((anotherItem).toString()); 
  }
}