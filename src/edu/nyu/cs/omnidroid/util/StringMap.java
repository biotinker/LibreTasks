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
/**
 * 
 */
package edu.nyu.cs.omnidroid.util;

public class StringMap {
  public static final int KEY = 0;
  public static final int VALUE = 1;

  private String[] strings = { "", "" };

  public StringMap() {
  }

  public StringMap(String s1, String s2) {
    strings[KEY] = s1;
    strings[VALUE] = s2;
  }

  public String get(int i) {
    return strings[i];
  }

  /**
   * @param actualEvent
   * @param displayEvent
   */
  public void set(String s1, String s2) {
    strings[KEY] = s1;
    strings[VALUE] = s2;
  }

  public String toString() {
    return strings[1];
  }

  public String getKey() {
    return strings[KEY];
  }

  public String getValue() {
    return strings[VALUE];
  }
}
