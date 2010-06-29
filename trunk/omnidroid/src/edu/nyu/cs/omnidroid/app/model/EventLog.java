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
import edu.nyu.cs.omnidroid.app.controller.Event;
import edu.nyu.cs.omnidroid.app.model.db.LogDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.LogEventDbAdapter;

/**
 * This class represents an Event{@code Log}. Logs are displayed on the {@code ActivityLogs} for
 * users to see what is going on.
 */
public class EventLog extends Log {
  public static final String TAG = EventLog.class.getSimpleName();

  // Extended Log Constructs
  String appName;
  String eventName;
  String parameters;

  /**
   * @param context
   *          application context for the db connection
   * @param event
   *          to create an {@code Event} out of
   * 
   */
  public EventLog(Context context, Event event) {
    super();
    this.context = context;
    this.dbHelper = new CoreEventLogsDbHelper(context);
    this.appName = event.getAppName();
    this.eventName = event.getEventName();
    this.parameters = event.getParameters();
    this.text = event.getEventName() + event.getParameters();
  }

  /**
   * Copy constructor
   * 
   * @param log
   *          EventLog to duplicate
   * 
   */
  public EventLog(EventLog log) {
    super(log);
    this.context = log.context;
    this.dbHelper = new CoreEventLogsDbHelper(context);
    this.id = log.id;
    this.timestamp = log.timestamp;
    this.appName = log.appName;
    this.eventName = log.eventName;
    this.parameters = log.parameters;
  }

  /**
   * Create a new log based on an a new Event
   * 
   * @param context
   *          application context for the db connection
   * @param id
   *          the ID for the log in the database
   */
  public EventLog(Context context, long id) {
    super();
    this.context = context;
    this.dbHelper = new CoreEventLogsDbHelper(context);
    Cursor cursor = dbHelper.getLogMatchingID(id);
    this.id = CursorHelper.getLongFromCursor(cursor, LogDbAdapter.KEY_ID);
    this.timestamp = CursorHelper.getLongFromCursor(cursor, LogDbAdapter.KEY_TIMESTAMP);
    this.appName = CursorHelper.getStringFromCursor(cursor, LogEventDbAdapter.KEY_APPNAME);
    this.eventName = CursorHelper.getStringFromCursor(cursor, LogEventDbAdapter.KEY_EVENTNAME);
    this.parameters = CursorHelper.getStringFromCursor(cursor,
        LogEventDbAdapter.KEY_EVENTPARAMETERS);
    this.text = CursorHelper.getStringFromCursor(cursor, LogDbAdapter.KEY_DESCRIPTION);
    cursor.close();
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getAppName() {
    return appName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getEventName() {
    return eventName;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  public String getParameters() {
    return parameters;
  }

  public String toString() {
    return "ID: " + id + "\n" + "Timestamp: " + timestamp + "\n" + "Application Name: " + appName
        + "\n" + "Event Name: " + eventName + "\nParameters: " + parameters + "\nText: " + text;
  }
}
