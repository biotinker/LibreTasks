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
package edu.nyu.cs.omnidroid.app.view.simple.model;


/**
 * UI representation of an action parameter.
 */
public class ModelParameter extends ModelItem {

  private final long foreignKeyActionId;
  private final long datatype;

  public ModelParameter(long databaseId, long foreignKeyActionId, long datatype, String typeName,
      String description) {
    super(typeName, description, -1, databaseId);
    this.foreignKeyActionId = foreignKeyActionId;
    this.datatype = datatype;
  }

  public long getForeignKeyActionId() {
    return foreignKeyActionId;
  }
  
  public long getDatatype() {
    return datatype;
  }
}