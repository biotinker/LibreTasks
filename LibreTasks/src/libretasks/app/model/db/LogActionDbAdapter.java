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
package libretasks.app.model.db;

import libretasks.app.model.Log;
import libretasks.app.model.ActionLog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Database Adapter class for the LogAction table. Defines basic CRUD methods.
 * <p>
 * This table records all Actions that Omnidroid handles.
 * </p>
 */
public class LogActionDbAdapter extends LogDbAdapter {

  /* Column names */
  public static final String KEY_LOGEVENTID = "FK_LogEventID";
  public static final String KEY_RULENAME = "ruleName";
  public static final String KEY_ACTIONAPPNAME = "FK_ActionAppName";
  public static final String KEY_ACTIONEVENTNAME = "FK_ActionEventName";
  public static final String KEY_ACTIONPARAMETERS = "ActionParameters";

  /* An array of all column names */
  public static final String[] KEYS = { KEY_ID, KEY_TIMESTAMP, KEY_LOGEVENTID, KEY_RULENAME,
      KEY_ACTIONAPPNAME, KEY_ACTIONEVENTNAME, KEY_ACTIONPARAMETERS, KEY_DESCRIPTION };

  /* Table name */
  private static final String DATABASE_TABLE = "LogAction";

  /* Create and drop statement. */
  protected static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" + KEY_ID
      + " integer primary key autoincrement, " + KEY_TIMESTAMP + " integer, " + KEY_LOGEVENTID
      + " integer, " + KEY_RULENAME + " text not null, " + KEY_ACTIONAPPNAME + " text not null, "
      + KEY_ACTIONEVENTNAME + " text not null, " + KEY_ACTIONPARAMETERS + " text not null, "
      + KEY_DESCRIPTION + " text not null);";
  protected static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

  public LogActionDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  /**
   * Insert a new Action Log record.
   * 
   * @param timeStamp
   *          the time stamp of the action taken.
   * @param logEventID
   *          the FK for the LogEvent record that caused this action
   * @param ruleName
   *          the name of the Rule that caused this action
   * @param actionAppName
   *          the name of the application for the action
   * @param actionEventName
   *          the name of the event for the action
   * @param actionParameters
   *          the parameters for the action
   * @param description
   *          a description of the Log
   * @return the row ID of the newly inserted row, or -1 if an error occurred
   */
  public long insert(long timeStamp, long logEventID, String ruleName, String actionAppName,
      String actionEventName, String actionParameters, String description) {
    if (ruleName == null || actionAppName == null || actionEventName == null
        || actionParameters == null || description == null) {
      throw new IllegalArgumentException("insert parameter null.");
    }
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_TIMESTAMP, timeStamp);
    initialValues.put(KEY_LOGEVENTID, logEventID);
    initialValues.put(KEY_RULENAME, ruleName);
    initialValues.put(KEY_ACTIONAPPNAME, actionAppName);
    initialValues.put(KEY_ACTIONEVENTNAME, actionEventName);
    initialValues.put(KEY_ACTIONPARAMETERS, actionParameters);
    initialValues.put(KEY_DESCRIPTION, description);
    return database.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * deletes the specified LogAction from the database
   * 
   * @param id
   *          - ID of the database entry to delete
   * @return true if success, or false otherwise.
   */
  public boolean delete(long id) {
    // Set the whereArgs to null here.
    return database.delete(DATABASE_TABLE, KEY_ID + "=" + id, null) > 0;
  }

  /**
   * Delete all LogAction records.
   * 
   * @return true if success, or false if failed or nothing to be deleted.
   */
  public boolean deleteAll() {
    // Set where and whereArgs to null here.
    return database.delete(DATABASE_TABLE, null, null) > 0;
  }

  /**
   * Return a Cursor pointing to the record matches the logActionID.
   * 
   * @param id
   *          - ID of the database entry to retrieve
   * @return the matching cursor.
   */
  public Cursor fetch(long id) {
    // Set selectionArgs, groupBy, having, orderBy and limit to be null.
    Cursor mCursor = database.query(true, DATABASE_TABLE, KEYS, KEY_ID + "=" + id, null, null,
        null, null, null);
    if (mCursor != null) {
      mCursor.moveToFirst();
    }
    return mCursor;
  }

  /**
   * @return a Cursor that contains all LogAction records sorted by timestamp in descending order.
   */
  public Cursor fetchAll() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, KEY_TIMESTAMP + " desc");
  }

  /**
   * @return the sql command to create this LogAction table
   */
  public String getSqliteCreateStatement() {
    return DATABASE_CREATE;
  }

  @Override
  public long insert(Log log) {
    if (log == null) {
      throw new IllegalArgumentException("no log specified.");
    }

    ActionLog myLog = (ActionLog) log;
    return insert(myLog.getTimestamp(), myLog.getLogEventID(), myLog.getRuleName(), myLog
        .getAppName(), myLog.getActionName(), myLog.getParameters(), myLog.getText());
  }

  @Override
  public Cursor fetchAllBefore(long timestamp) {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    String selection = KEY_TIMESTAMP + " < " + timestamp;
    return database.query(DATABASE_TABLE, KEYS, selection, null, null, null, null);
  }

  @Override
  public int deleteAllBefore(long timestamp) {
    // Set where to before timestamp and whereArgs to null here.
    String where = KEY_TIMESTAMP + " < " + timestamp;
    return database.delete(DATABASE_TABLE, where, null);
  }

}
