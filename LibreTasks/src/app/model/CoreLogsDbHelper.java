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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import libretasks.app.R;
import libretasks.app.controller.util.Logger;
import libretasks.app.model.db.DbHelper;
import libretasks.app.model.db.LogDbAdapter;

/**
 * Abstract base class that provides general access to the Log DB layer.
 * 
 */
abstract public class CoreLogsDbHelper {
  // Class identifier
  private static final String TAG = CoreLogsDbHelper.class.getSimpleName();

  /**
   * Default limit of time to keep logs around, specified in hours
   * 
   * Unfortunately Android doesn't support integer based arrays with the ListPreference interface,
   * so we have to convert an integer back from a string. See:
   * http://code.google.com/p/android/issues/detail?id=2096
   */
  protected static final String LOG_LIMIT_DEFAULT = "24";

  // DB Management
  protected Context context;
  protected DbHelper dbHelper;
  protected SQLiteDatabase database;
  protected LogDbAdapter logDbAdapter;

  /**
   * Creates a new CoreLogsDbHelper within the current context and initializes all necessary
   * database adapters.
   * 
   * @param context
   *          context for the application database resource
   */
  public CoreLogsDbHelper(Context context) {
    this.context = context;
    dbHelper = new DbHelper(context);
    database = dbHelper.getWritableDatabase();
  }

  /**
   * Close this database helper object. Attempting to use this object after this call will cause an
   * {@link IllegalStateException} being raised.
   */
  public void close() {
    Logger.i(TAG, "closing database.");
    database.close();

    // Not necessary, but also close all dbHelper databases just in case.
    dbHelper.close();
  }

  /**
   * 
   * @param log
   *          is a cursor to the current log to return
   * @return the {@code Log} at the cursor position
   */
  abstract public Log getLog(Cursor log);

  /*
   * Retrieves a cursor to the log that matched the ID passed in.
   * 
   * @param id - the ID for the Log that is desired.
   * 
   * @return a cursor to the event log requested.
   */
  public Log getLogMatchingID(long id) {
    return getLog(logDbAdapter.fetch(id));
  }

  /**
   * Insert a new Log record into DB
   * 
   * @param log
   *          log to store in the DB
   * @return id of the record inserted, -1 if unsuccessful
   */
  public long insert(Log log) {
    deleteOldLogs();
    log.setTimestamp((new Date()).getTime());
    return logDbAdapter.insert(log);
  }

  /**
   * @return a List of {@code nLog}s that are stored in the DB.
   */
  public List<Log> getLogs() {
    ArrayList<Log> logs = new ArrayList<Log>();

    // Fetch all rules that match this event and are enabled
    Cursor logTable = logDbAdapter.fetchAll();

    // Build a rule for each row in the database and add it to the rule list
    while (logTable.moveToNext()) {
      logs.add(getLog(logTable));
    }
    logTable.close();
    return logs;
  }

  public void deleteOldLogs() {
    if (!database.isOpen()) {
      throw new IllegalStateException(TAG + " is already closed.");
    }

    /*
     * Get the Log limit stored in preferences (in hours)
     * 
     * Unfortunately Android doesn't support integer based arrays with the ListPreference interface,
     * so we have to convert an integer back from a string. See:
     * http://code.google.com/p/android/issues/detail?id=2096
     */
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    String sLogLimitHours = prefs.getString(context.getString(R.string.pref_key_log_limit),
        LOG_LIMIT_DEFAULT);
    int logLimitHours = Integer.parseInt(sLogLimitHours);

    // Convert hours to a limit based on timestamp
    long logsBeforeTimestamp = (new Date()).getTime() - (logLimitHours * LogDbAdapter.TIME_IN_HOUR);

    // Delete the old logs
    logDbAdapter.deleteAllBefore(logsBeforeTimestamp);
  }

}
