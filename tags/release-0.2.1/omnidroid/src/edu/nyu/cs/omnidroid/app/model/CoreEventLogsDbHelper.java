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

import static edu.nyu.cs.omnidroid.app.model.CursorHelper.getLongFromCursor;
import static edu.nyu.cs.omnidroid.app.model.CursorHelper.getStringFromCursor;

import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import edu.nyu.cs.omnidroid.app.model.db.LogDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.LogEventDbAdapter;

/**
 * This class serves as a access layer to the database for Omnidroid's {@code LogEvent} storage.
 * 
 */
public class CoreEventLogsDbHelper extends CoreLogsDbHelper {

  /**
   * Creates a new CoreEventDbHelper within the current context and initializes all necessary
   * database adapters.
   * 
   * @param context
   *          context for the application database resource
   */
  public CoreEventLogsDbHelper(Context context) {
    super(context);
    logDbAdapter = new LogEventDbAdapter(database);
  }

  @Override
  public Log getLog(Cursor cursor) {
    // Get Log data from the DB
    long id = getLongFromCursor(cursor, LogDbAdapter.KEY_ID);
    long timestamp = getLongFromCursor(cursor, LogDbAdapter.KEY_TIMESTAMP);
    String appName = getStringFromCursor(cursor, LogEventDbAdapter.KEY_APPNAME);
    String eventName = getStringFromCursor(cursor, LogEventDbAdapter.KEY_EVENTNAME);
    String parameters = getStringFromCursor(cursor, LogEventDbAdapter.KEY_EVENTPARAMETERS);
    String text = getStringFromCursor(cursor, LogDbAdapter.KEY_DESCRIPTION);

    // Create a new Log object from the data
    EventLog log = new EventLog(id, timestamp, appName, eventName, parameters, text);
    return log;
  }

  /**
   * 
   * @return number of events that were performed in the last minute
   */
  public int getLogCountDuringLastMinute() {
    // Fetch all actions recorded in the last minute
    long logsSinceTimestamp = (new Date()).getTime() - LogDbAdapter.TIME_IN_MINUTE;
    Cursor logTable = ((LogEventDbAdapter)logDbAdapter).fetchAllSince(logsSinceTimestamp);
    int count = logTable.getCount();
    logTable.close();
    return count;
  }
}
