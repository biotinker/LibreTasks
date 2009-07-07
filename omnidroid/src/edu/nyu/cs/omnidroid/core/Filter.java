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
package edu.nyu.cs.omnidroid.core;

/**
 * This class contains a user defined filter, which will be checked against an event's attribute
 * data to see if the event matches the user defined rule.
 */
public class Filter {
  /**
   * The type and data to filter.
   */
  public final String type;
  public final String data;

  /**
   * Create a new filter for a specific data field
   * 
   * @param type
   *          The type of the filter, which should match either an event attribute or a system or
   *          global attribute
   * @param data
   *          The value of the filter data type, defined during rule creation
   */
  public Filter(String type, String data) {
    this.type = type;
    this.data = data;
  }
}
