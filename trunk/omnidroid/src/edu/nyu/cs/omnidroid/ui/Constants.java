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
package edu.nyu.cs.omnidroid.ui;

/**
 * This class simply stores some constants that are used between all the UI classes
 * TODO: (markww) Remove unused constants after done switching to new UI.
 */
public interface Constants {
  // Activity Request Codes
  final int RESULT_ADD_OMNIHANDLER = 1;
  final int RESULT_DEL_OMNIHANDLER = 2;
  final int RESULT_EDIT_OMNIHANDLER = 3;
  final int RESULT_ADD_FILTER = 4;
  final int RESULT_DEL_FILTER = 5;
  final int RESULT_EDIT_FILTER = 6;
  final int RESULT_ADD_THROWER = 7;
  final int RESULT_DEL_THROWER = 8;
  final int RESULT_EDIT_THROWER = 9;
  final int RESULT_ADD_DATUM = 10;
  final int RESULT_DEL_DATUM = 11;
  final int RESULT_EDIT_DATUM = 12;
  final int RESULT_EDIT_SETTINGS = 13;

  // Request Result Values
  final int RESULT_SUCCESS = 1;
  final int RESULT_FAILURE = 2;
  

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