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
package edu.nyu.cs.omnidroid.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.nyu.cs.omnidroid.app.controller.Log;
import edu.nyu.cs.omnidroid.app.controller.util.Logger;
import edu.nyu.cs.omnidroid.app.model.db.DbHelper;
import edu.nyu.cs.omnidroid.app.model.db.LogEventDbAdapter;

/**
 * This class serves as a access layer to the database for Omnidroid's {@code LogEvent} storage.
 * 
 * TODO: (acase) Create a CoreLogActionDbHelper class
 */
public class CoreLogEventDbHelper {
  private static final String TAG = CoreLogEventDbHelper.class.getSimpleName();

  private DbHelper omnidroidDbHelper;
  private LogEventDbAdapter logEventDbAdapter;
  private SQLiteDatabase database;

  /**
   * Creates a new CoreEventDbHelper within the current context and initializes all necessary
   * database adapters.
   * 
   * @param context
   *          context for the application database resource
   */
  public CoreLogEventDbHelper(Context context) {
    omnidroidDbHelper = new DbHelper(context);
    database = omnidroidDbHelper.getWritableDatabase();
    logEventDbAdapter = new LogEventDbAdapter(database);
  }

  /**
   * Close this database helper object. Attempting to use this object after this call will cause an
   * {@link IllegalStateException} being raised.
   */
  public void close() {
    Logger.i(TAG, "closing database.");
    database.close();
    
    // Not necessary, but also close all omnidroidDbHelper databases just in case.
    omnidroidDbHelper.close();
  }

  /**
   * Builds an ArrayList of {@link Logger} objects from the data stored in the Omnidroid database.
   * 
   * @param appName
   *          the name of the application the event belongs to in the database
   * @param eventName
   *          the name of the event to be matched in the database
   * @return a list of logs matches the provided parameters
   */
  public List<Log> getLogsMatchingEvent(String appName, String eventName) {
    // TODO: (acase) write this.
    return null;
  }

  /**
   * @return a List of {@code EventLog}s that are stored in the DB.
   */
  public List<Log> getEventLogs() {
    ArrayList<Log> logs = new ArrayList<Log>();

    // Fetch all rules that match this event and are enabled
    Cursor logTable = logEventDbAdapter.fetchAll();

    // Build a rule for each row in the database and add it to the rule list
    while (logTable.moveToNext()) {
      logs.add(getLog(logTable));
    }
    logTable.close();
    return logs;
  }

  /**
   * Retrieves a {@link Log} and associated data from the database and returns it in an OmniLog data
   * structure.
   * 
   * @param logRecord
   *          a {@link Cursor} that points to the log record to retrieve from the database.
   * @return a Log object built from the database record.
   */
  private Log getLog(Cursor logRecord) {
    // TODO(acase): Write this
    return null;
  }

  /*
   * Retrieves a cursor to the event log that matched the ID passed in.
   * 
   * @param logEventID
   *            - the ID for the EventLog that is desired.
   * @return a cursor to the event log requested.
   * 
   */
  public Cursor getLogMatchingEvent(long logEventID) {
    Cursor cursor = logEventDbAdapter.fetch(logEventID);
    return cursor;
  }

  /**
   * Insert a new Action Log record.
   * 
   * @param appName
   *          event's application name for the log
   * @param eventName
   *          event's name for for the log
   * @param eventParameters
   *          event's paramaters for the log
   * @return
   */
  public long insert(String appName, String eventName, String eventParameters) {
    return logEventDbAdapter.insert((new Date()).getTime(), appName, eventName, eventParameters);
  }

  /**
   * Insert a new Action Log record.
   * 
   * @param appName
   *          event's application name for the log
   * @param eventName
   *          event's name for for the log
   * @return
   */
  public long insert(String appName, String eventName) {
    return logEventDbAdapter.insert((new Date()).getTime(), appName, eventName, "");
  }
}
