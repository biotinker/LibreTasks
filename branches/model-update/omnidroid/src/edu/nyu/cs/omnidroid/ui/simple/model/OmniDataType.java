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
 * Dummy base class for DataType. Inheriting classes would be OmniText, OmniPhonenumber, OmniDate,
 * etc. This is to be removed once the real implementations are added to the core package.
 * 
 * We follow the general idea that a data type constructor throws an exception if its input is
 * invalid, and that a meaningful error will be in the thrown exception for display to the user.
 */
public abstract class OmniDataType {

  public OmniDataType() throws Exception {
  }

  public abstract String getDescription();
}