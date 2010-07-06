/*******************************************************************************
 * Copyright 2010 Omnidroid - http://code.google.com/p/omnidroid 
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
package edu.nyu.cs.omnidroid.app.view.simple.viewitem;

import edu.nyu.cs.omnidroid.app.view.simple.model.ModelAttribute;

/**
 * Abstract class for {@link ViewItem} containing convenience methods for subclasses.
 */
public abstract class AbstractViewItem implements ViewItem {
  protected final int ID;
  protected final long dataTypeDbID;
  
  public AbstractViewItem(int id, long dataTypeDbID) {
    ID = id;
    this.dataTypeDbID = dataTypeDbID;
  }
  
  /**
   * {@inheritDoc}
   */
  public final int getID() {
    return ID;
  }

  /**
   * {@inheritDoc}
   */
  public final long getDataTypeDbID() {
    return dataTypeDbID;
  }

  protected final static String getAttributeInsertName(ModelAttribute modelAttribute) {
    return "<" + modelAttribute.getTypeName() + ">";
  }
}
