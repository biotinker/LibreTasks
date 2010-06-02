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
package edu.nyu.cs.omnidroid.app.view;

/**
 * Stores codes for use between different UI elements.
 */
public interface Constants {

  /** Return code for filter building activities. */
  public static final int ACTIVITY_RESULT_ADD_FILTER = 0;

  /** Return code for action building activities. */
  public static final int ACTIVITY_RESULT_ADD_ACTION = 1;

  /** Return code for filter editing activities. */
  public static final int ACTIVITY_RESULT_EDIT_FILTER = 2;

  /** Return code for action editing activities. */
  public static final int ACTIVITY_RESULT_EDIT_ACTION = 3;
  
  /** Return code for rule name editing activity. */
  public static final int ACTIVITY_RESULT_RULE_NAME = 4;
  
  /** Return code for rule building activity. */
  public static final int ACTIVITY_RESULT_ADD_FILTERS_AND_ACTIONS = 5;
}