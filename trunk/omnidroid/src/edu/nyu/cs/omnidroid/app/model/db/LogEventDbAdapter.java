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
package edu.nyu.cs.omnidroid.app.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Database Adapter class for the LogEvent table. Defines basic CRUD methods.
 * <p>
 * This table records all events that Omnidroid handles. LogEventID is the primary key. TimeStamp is
 * when it occurred. FK_AppName is the application that caused the event. FK_EventName is the name
 * of the event. EventParameters are the parameters associated with the event that occurred.
 * </p>
 * TODO: (acase) Make a LogActionDbAdapter as well.
 */
public class LogEventDbAdapter extends LogDbAdapter {

  /* Column names */
  public static final String KEY_LOGEVENTID = "LogEventID";
  public static final String KEY_TIMESTAMP = "TimeStamp";
  public static final String KEY_APPNAME = "FK_AppName";
  public static final String KEY_EVENTNAME = "FK_EventName";
  public static final String KEY_EVENTPARAMETERS = "EventParameters";

  /* An array of all column names */
  public static final String[] KEYS = { KEY_LOGEVENTID, KEY_TIMESTAMP, KEY_APPNAME, KEY_EVENTNAME,
      KEY_EVENTPARAMETERS };

  /* Table name */
  private static final String DATABASE_TABLE = "LogAction";

  /* Create and drop statement. */
  protected static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("
      + KEY_LOGEVENTID + " integer primary key autoincrement, " + KEY_TIMESTAMP + " integer, "
      + KEY_APPNAME + " text not null, " + KEY_EVENTNAME + " text not null, " + KEY_EVENTPARAMETERS
      + " text not null);";
  protected static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

  public LogEventDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  /**
   * Insert a new Event Log record.
   * 
   * @param timeStamp
   *          the time stamp of the action taken.
   * @param appName
   *          the name of the application that originated the event
   * @param eventName
   *          the name of the event that occurred
   * @param eventParameters
   *          the parameters for the event
   * @return the row ID of the newly inserted row, or -1 if an error occurred
   */
  public long insert(Long timeStamp, String appName, String eventName, String eventParameters) {
    if (timeStamp == null || appName == null || eventName == null || eventParameters == null) {
      throw new IllegalArgumentException("insert parameter null.");
    }
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_TIMESTAMP, timeStamp);
    initialValues.put(KEY_APPNAME, appName);
    initialValues.put(KEY_EVENTNAME, eventName);
    initialValues.put(KEY_EVENTPARAMETERS, eventParameters);
    return database.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * deletes the specified LogEvent from the database
   * 
   * @param logEventID
   *          - ID of the LogEvent to delete
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if the primary id is null.
   */
  public boolean delete(Long logEventID) throws IllegalArgumentException {
    if (logEventID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set the whereArgs to null here.
    return database.delete(DATABASE_TABLE, KEY_LOGEVENTID + "=" + logEventID, null) > 0;
  }

  /**
   * Delete all LogEvent records.
   * 
   * @return true if success, or false if failed or nothing to be deleted.
   */
  public boolean deleteAll() {
    // Set where and whereArgs to null here.
    return database.delete(DATABASE_TABLE, null, null) > 0;
  }

  /**
   * Return a Cursor pointing to the record matches the logEventID.
   * 
   * @param logEventID
   *          - ID of the LogEvent to delete
   * @return the matching cursor.
   */
  public Cursor fetch(Long logEventID) {
    if (logEventID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set selectionArgs, groupBy, having, orderBy and limit to be null.
    Cursor mCursor = database.query(true, DATABASE_TABLE, KEYS, KEY_LOGEVENTID + "=" + logEventID,
        null, null, null, null, null);
    if (mCursor != null) {
      mCursor.moveToFirst();
    }
    return mCursor;
  }

  /**
   * @return a Cursor that contains all LogEvent records.
   */
  public Cursor fetchAll() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, null);
  }

  /**
   * @return the sql command to create this LogEvent table
   */
  public String getSqliteCreateStatement() {
    return DATABASE_CREATE;
  }
}
