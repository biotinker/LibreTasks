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
    EventLog log = new EventLog(context, getLongFromCursor(cursor,
        LogEventDbAdapter.KEY_ID));
    log.setTimestamp(getLongFromCursor(cursor, LogDbAdapter.KEY_TIMESTAMP));
    log.setAppName(getStringFromCursor(cursor, LogEventDbAdapter.KEY_APPNAME));
    log.setEventName(getStringFromCursor(cursor, LogEventDbAdapter.KEY_EVENTNAME));
    log.setParameters(getStringFromCursor(cursor, LogEventDbAdapter.KEY_EVENTPARAMETERS));
    log.setText(getStringFromCursor(cursor, LogDbAdapter.KEY_DESCRIPTION));
    return log;
  }
}
