/*  
 * Copyright (c) 2016  LibreTasks - https://github.com/biotinker/LibreTasks  
 *  
 *  This file is free software: you may copy, redistribute and/or modify it  
 *  under the terms of the GNU General Public License as published by the  
 *  Free Software Foundation, either version 3 of the License, or (at your  
 *  option) any later version.  
 *  
 *  This file is distributed in the hope that it will be useful, but  
 *  WITHOUT ANY WARRANTY; without even the implied warranty of  
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  
 *  General Public License for more details.  
 *  
 *  You should have received a copy of the GNU General Public License  
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 *  
 * This file incorporates work covered by the following copyright and  
 * permission notice:  
 /*******************************************************************************
 * Copyright 2009, 2010 Omnidroid - http://code.google.com/p/omnidroid 
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
package libretasks.app.view.simple.model;

/**
 * UI representation of an application.
 */
public class ModelApplication extends ModelItem {

  private final boolean loginEnabled;
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
}
