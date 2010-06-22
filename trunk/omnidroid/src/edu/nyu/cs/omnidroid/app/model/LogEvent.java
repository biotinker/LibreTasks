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

import java.sql.Timestamp;

import android.content.Context;
import android.database.Cursor;
import edu.nyu.cs.omnidroid.app.controller.Event;
import edu.nyu.cs.omnidroid.app.controller.Log;
import edu.nyu.cs.omnidroid.app.controller.util.Logger;
import edu.nyu.cs.omnidroid.app.model.db.LogEventDbAdapter;

/**
 * This class represents an Event {@code OmniLog}. Logs are displayed on the ActivityEventLog for
 * users to see what is going on.
 */
public class LogEvent extends Log {
  public static final String TAG = LogEvent.class.getSimpleName();

  // Database storage accessor variables
  Context context;
  CoreLogEventDbHelper dbHelper;

  // Log Construct
  long logEventID = -1;
  long timestamp = -1;
  String appName;
  String eventName;

  // TODO(acase): Support eventParameters

  /**
   * @param context
   *          application context for the db connection
   * @param event
   *          to create an OmniLogEvent out of
   * 
   */
  public LogEvent(Context context, Event event) {
    super();
    this.context = context;
    this.dbHelper = new CoreLogEventDbHelper(context);
    this.appName = event.getAppName();
    this.eventName = event.getEventName();
  }

  /**
   * Copy constructor
   * 
   * @param log
   *          OmniLogEvent to duplicate
   * 
   */
  public LogEvent(LogEvent log) {
    super();
    this.context = log.context;
    this.dbHelper = new CoreLogEventDbHelper(context);
    this.logEventID = log.logEventID;
    this.timestamp = log.timestamp;
    this.appName = log.appName;
    this.eventName = log.eventName;
  }

  /**
   * Create a new log based on an a new Event
   * 
   * @param context
   *          application context for the db connection
   * @param eventID
   *          the ID for the log in the database
   */
  public LogEvent(Context context, long logEventID) {
    super();
    this.context = context;
    this.dbHelper = new CoreLogEventDbHelper(context);
    Cursor cursor = dbHelper.getLogMatchingEvent(logEventID);
    this.logEventID = CursorHelper.getLongFromCursor(cursor, LogEventDbAdapter.KEY_LOGEVENTID);
    this.timestamp = CursorHelper.getLongFromCursor(cursor, LogEventDbAdapter.KEY_TIMESTAMP);
    this.appName = CursorHelper.getStringFromCursor(cursor, LogEventDbAdapter.KEY_APPNAME);
    this.eventName = CursorHelper.getStringFromCursor(cursor, LogEventDbAdapter.KEY_EVENTNAME);
    cursor.close();
  }

  public void setEventID(long logEventID) {
    this.logEventID = logEventID;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  /**
   * Store this LogEvent in our database.
   */
  public void Log() {
    if ((timestamp != -1) || (logEventID != -1)) {
      Logger.e(TAG, "Trying to insert duplicate entry into DB");
    } else if (appName == null) {
      Logger.e(TAG, "Trying to insert duplicate entry into DB");
      throw new IllegalArgumentException("an event needs an application to have triggered it.");
    } else if (eventName == null) {
      Logger.e(TAG, "Trying to insert duplicate entry into DB");
      throw new IllegalArgumentException("an event needs an event name.");
    } else {
      dbHelper.insert(appName, eventName);
    }
  }

  public String toString() {
    Timestamp ts = new Timestamp(timestamp);
    return ts.toString();
  }

  public String toStringLong() {
    return "ID: " + logEventID + "\n" + "Timestamp: " + timestamp + "\n" + "Application Name: "
        + appName + "\n" + "Event Name: " + eventName + "\n";
  }

}
