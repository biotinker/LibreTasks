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
 * Copyright 2010 Omnidroid - http://code.google.com/p/omnidroid
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
package libretasks.app.model;

import libretasks.app.controller.Action;

/**
 * This class represents an Action {@code Log}. Logs are displayed on the ActivityLogs for
 * users to see what is going on.
 */
public class ActionLog extends Log {
  public static final String TAG = ActionLog.class.getSimpleName();

  // Extended Log Constructs
  private Long logEventID;
  private String ruleName;
  private String appName;
  private String actionName;
  private String parameters;

  /**
   * @param context
   *          application context for the db connection
   * @param action
   *          the Action to create a {@code Log} out of
   * @param logEventID
   *          the {@code Event} that caused this action.
   * 
   */
  public ActionLog(Action action, Long logEventID) {
    this.ruleName = action.getRuleName();
    this.logEventID = logEventID;
    this.appName = action.getAppName();
    this.actionName = action.getActionName();
    this.parameters = action.getParameters();
    this.text = action.getDescription();
  }

  public ActionLog(ActionLog log) {
    super(log);
    this.ruleName = log.ruleName;
    this.logEventID = log.logEventID;
    this.appName = log.appName;
    this.actionName = log.actionName;
    this.parameters = log.parameters;
  }

  /**
   * Create a Log item that stores relevant action data.
   * 
   * @param id
   *          the database id for this log entry
   * @param timeStamp
   *          the time stamp of the action taken.
   * @param logEventID
   *          the FK for the LogEvent record that caused this action
   * @param ruleName
   *          the name of the Rule that caused this action
   * @param appName
   *          the name of the application for the action
   * @param actionName
   *          the name of the event for the action
   * @param parameters
   *          the parameters for the action
   * @param text
   *          a textual description of the Log
   */
  public ActionLog(long id, long timestamp, long logEventID, String ruleName,
      String appName, String actionName, String parameters, String text) {
    super(id, timestamp, text);
    this.ruleName = ruleName;
    this.logEventID = logEventID;
    this.appName = appName;
    this.actionName = actionName;
    this.parameters = parameters;
  }

  public void setLogEventID(Long logEventID) {
    this.logEventID = logEventID;
  }

  public Long getLogEventID() {
    return logEventID;
  }

  public void setRuleName(String ruleName) {
    this.ruleName = ruleName;
  }

  public String getRuleName() {
    return ruleName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getAppName() {
    return appName;
  }

  public void setActionName(String actionName) {
    this.actionName = actionName;
  }

  public String getActionName() {
    return actionName;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  public String getParameters() {
    return parameters;
  }

  public String toString() {
    return "ID: " + id + "\nTimestamp: " + timestamp + "\nLogEventID: " + logEventID
        + "\nRuleName: " + ruleName + "\nApplication Name: " + appName + "\nAction Name: "
        + actionName + "\nParameters: " + parameters;
  }
}
