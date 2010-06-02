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

import java.util.ArrayList;

/**
 * UI representation of an action. Actions always have an associated parent application.
 */
public class ModelAction extends ModelItem {

  /** An action always has a parent application. */
  private final ModelApplication application;
  
  /** A list of parameter names for this action. */
  private ArrayList<ModelParameter> parameters;

  public ModelAction(String typeName, String description, int iconResId, long databaseId,
      ModelApplication application, ArrayList<ModelParameter> parameters) {
    super(typeName, description, iconResId, databaseId);
    this.application = application;
    this.parameters = parameters;
  }

  public ModelApplication getApplication() {
    return application;
  }
  
  public ArrayList<ModelParameter> getParameters() {
    return parameters;
  }
}