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
package edu.nyu.cs.omnidroid.app.core.datatypes;

/**
 * Provides data type for storing a check box input.
 */
public class OmniCheckBoxInput extends DataType {
  private Boolean value;

  /* data type name to be stored in db */
  public static final String DB_NAME = "CheckBoxInput";

  public OmniCheckBoxInput(Boolean value) {
    this.value = value;
  
  }

  @Override
  public String getValue() {
    return value.toString();
  }

  @Override
  public boolean matchFilter(Filter filter, DataType userDefinedValue)
          throws IllegalArgumentException {
    return false;
  }

  @Override
  public String toString() {
    return value.toString();
  }

  /**
   * @return the value
   */
  public boolean isChecked() {
    return value;
  }

}