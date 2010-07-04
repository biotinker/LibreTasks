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
package edu.nyu.cs.omnidroid.app.controller.datatypes;

/**
 * Provides data type that can be used as password in actions
 * TODO(ehotou): Consider use some encrypt/decrypt methods so that it won't be stored as plain 
 * text in the database. 
 */
public class OmniPasswordInput extends DataType {
  private String value;

  /* data type name to be stored in db */
  public static final String DB_NAME = "PasswordInput";

  public OmniPasswordInput(String str) {
    this.value = str;
  }

  public OmniPasswordInput(Object obj) {
    this.value = obj.toString();
  }

  /**
   * No filter for this data type
   * @see edu.nyu.cs.omnidroid.app.controller.datatypes.DataType#matchFilter(DataType.Filter, DataType)
   */
  @Override
  public boolean matchFilter(Filter filter, DataType userDefinedValue)
      throws IllegalArgumentException {
    throw new IllegalArgumentException("Matching filter not found for the datatype "
        + userDefinedValue.getClass().toString() + ". ");
  }
  
  @Override
  public String getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
