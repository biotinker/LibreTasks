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
 * Copyright 2009, 2010 Omnidroid - http://code.google.com/p/omnidroid 
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
package libretasks.app.view.simple.model;

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
