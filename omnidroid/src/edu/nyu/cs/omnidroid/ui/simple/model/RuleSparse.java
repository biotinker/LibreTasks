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
 * This is a simple representation of a Rule for when we need to display 
 * a master list of all Rules from the database. When a user wants more
 * info about a Rule, we load the full Rule instance from the database.
 */
public class RuleSparse {

  /** Database ID for the rule. */
  private int mDatabaseId;
  
  /** Name the user gave to the rule. */
  private String mName;
  
  /** Is the rule on or off. */
  private boolean mIsActive;
  
  public RuleSparse(int databaseId, String name, boolean isActive) {
	  mDatabaseId = databaseId;
	  mName = name;
	  mIsActive = isActive;
  }
  
  public int getDatabaseId() {
	  return mDatabaseId;
  }
  
  public String getName() {
	  return mName;
  }
  
  public boolean getIsActive() {
	  return mIsActive;
  }
}