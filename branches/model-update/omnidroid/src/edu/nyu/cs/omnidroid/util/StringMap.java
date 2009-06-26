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

package edu.nyu.cs.omnidroid.util;

/**
 * Class that holds a key and a value, both of String type.
 */
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
   * @param s1 the key for the StringMap
   * @param s2 the value for the StringMap
   */
  public void set(String s1, String s2) {
    strings[KEY] = s1;
    strings[VALUE] = s2;
  }

  @Override
  public String toString() {
    return strings[1];
  }

  public String getKey() {
    return strings[KEY];
  }

  public String getValue() {
    return strings[VALUE];
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof StringMap)) {
      return false;
    }

    StringMap o = (StringMap) obj;
    return strings[KEY].equals(o.strings[KEY])
        && strings[VALUE].equals(o.strings[VALUE]);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + strings[KEY].hashCode();
    result = 31 * result + strings[VALUE].hashCode();
    return result;
  }
}
