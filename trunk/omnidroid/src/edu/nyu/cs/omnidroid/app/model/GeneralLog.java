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
import edu.nyu.cs.omnidroid.app.model.db.LogDbAdapter;

/**
 * This class represents an General{@code Log}. Logs are displayed on the ActivityLogs for
 * users to see what is going on.
 */
public class GeneralLog extends Log {
  public static final String TAG = GeneralLog.class.getSimpleName();

  /**
   * @param context
   *          application context for the db connection
   * @param text
   *          to create an OmniLogEvent out of
   * 
   */
  public GeneralLog(Context context, String text) {
    super();
    this.context = context;
    this.dbHelper = new CoreGeneralLogsDbHelper(context);
    this.text = text;
  }

  /**
   * Copy constructor
   * 
   * @param log
   *          GeneralLog to duplicate
   * 
   */
  public GeneralLog(GeneralLog log) {
    super(log);
    this.context = log.context;
    this.dbHelper = new CoreGeneralLogsDbHelper(context);
  }

  /**
   * Create a new log based on an a new Log
   * 
   * @param context
   *          application context for the db connection
   * @param id
   *          the ID for the log in the database
   */
  public GeneralLog(Context context, long id) {
    super();
    this.context = context;
    this.dbHelper = new CoreGeneralLogsDbHelper(context);
    Cursor cursor = dbHelper.getLogMatchingID(id);
    this.id = CursorHelper.getLongFromCursor(cursor, LogDbAdapter.KEY_ID);
    this.timestamp = CursorHelper.getLongFromCursor(cursor, LogDbAdapter.KEY_TIMESTAMP);
    this.text = CursorHelper.getStringFromCursor(cursor, LogDbAdapter.KEY_DESCRIPTION);
    cursor.close();
  }

  public String toString() {
    return "ID: " + id + "\n" + "Timestamp: " + timestamp + "\n" + "\nText: " + text;
  }
}
