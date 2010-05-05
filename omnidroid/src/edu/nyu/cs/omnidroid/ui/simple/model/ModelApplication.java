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
 * UI representation of an application.
 */
public class ModelApplication extends ModelItem {

  private final boolean loginEnabled;
  private boolean staySignedIn;
  private String username;
  private String password;

  public ModelApplication(String typeName, String description, int iconResId, long databaseId) {
    super(typeName, description, iconResId, databaseId);
    this.loginEnabled = false;
    this.username = "";
    this.password = "";
  }

  public ModelApplication(String typeName, String description, int iconResId, long databaseId,
          boolean loginEnabled, String username, String password) {
    super(typeName, description, iconResId, databaseId);
    this.loginEnabled = loginEnabled;
    this.username = username;
    this.password = password;
  }

  /**
   * @return the loginEnabled
   */
  public boolean getLoginEnabled() {
    return loginEnabled;
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }
  
  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }
  
  /**
   * @return the staySignedIn
   */
  public boolean isStaySignedIn() {
    return staySignedIn;
  }

  /**
   * @param staySignedIn the staySignedIn to set
   */
  public void setStaySignedIn(boolean staySignedIn) {
    this.staySignedIn = staySignedIn;
  }

}