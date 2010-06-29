/*******************************************************************************
 * Copyright 2010 OmniDroid - http://code.google.com/p/omnidroid
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
package edu.nyu.cs.omnidroid.app.model;

import android.content.Context;
import android.database.Cursor;
import edu.nyu.cs.omnidroid.app.controller.Action;
import edu.nyu.cs.omnidroid.app.model.db.LogActionDbAdapter;

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
  public ActionLog(Context context, Action action, Long logEventID) {
    this.dbHelper = new CoreActionLogsDbHelper(context);
    this.logEventID = logEventID;
    this.ruleName = action.getRuleName();
    this.appName = action.getAppName();
    this.actionName = action.getActionName();
    this.parameters = action.getParameters();
    this.text = action.getDescription();
  }

  public ActionLog(ActionLog log) {
    super(log);
    this.context = log.context;
    this.dbHelper = new CoreActionLogsDbHelper(context);
    this.ruleName = log.ruleName;
    this.logEventID = log.logEventID;
    this.appName = log.appName;
    this.actionName = log.actionName;
    this.parameters = log.parameters;
  }

  /**
   * Create a new log based on a LogAction entry already in the database 
   * 
   * @param context
   *          application context for the db connection
   * @param id
   *          the ID for the record in the database
   */
  public ActionLog(Context context, long id) {
    super();
    this.context = context;
    this.dbHelper = new CoreActionLogsDbHelper(context);
    Cursor cursor = dbHelper.getLogMatchingID(id);
    this.id = CursorHelper.getLongFromCursor(cursor, LogActionDbAdapter.KEY_ID);
    this.timestamp = CursorHelper.getLongFromCursor(cursor, LogActionDbAdapter.KEY_TIMESTAMP);
    this.logEventID = CursorHelper.getLongFromCursor(cursor, LogActionDbAdapter.KEY_LOGEVENTID);
    this.ruleName = CursorHelper.getStringFromCursor(cursor, LogActionDbAdapter.KEY_RULENAME);
    this.appName = CursorHelper.getStringFromCursor(cursor, LogActionDbAdapter.KEY_ACTIONAPPNAME);
    this.text = CursorHelper.getStringFromCursor(cursor, LogActionDbAdapter.KEY_DESCRIPTION);
    this.actionName = CursorHelper.getStringFromCursor(cursor,
        LogActionDbAdapter.KEY_ACTIONEVENTNAME);
    this.parameters = CursorHelper.getStringFromCursor(cursor,
        LogActionDbAdapter.KEY_ACTIONPARAMETERS);
    cursor.close();
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
