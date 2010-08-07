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

import android.content.Context;
import android.database.Cursor;
import edu.nyu.cs.omnidroid.app.model.db.LogDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.LogGeneralDbAdapter;

/**
 * This class serves as a access layer to the database for Omnidroid's {@code LogGeneral} storage.
 * 
 */
public class CoreGeneralLogsDbHelper extends CoreLogsDbHelper {

  /**
   * Creates a new CoreGeneralDbHelper within the current context and initializes all necessary
   * database adapters.
   * 
   * @param context
   *          context for the application database resource
   */
  public CoreGeneralLogsDbHelper(Context context) {
    super(context);
    logDbAdapter = new LogGeneralDbAdapter(database);
  }

  @Override
  public Log getLog(Cursor cursor) {
    // Get the log from the db
    long id = CursorHelper.getLongFromCursor(cursor, LogDbAdapter.KEY_ID);
    long timestamp = CursorHelper.getLongFromCursor(cursor, LogDbAdapter.KEY_TIMESTAMP);
    String text = CursorHelper.getStringFromCursor(cursor, LogDbAdapter.KEY_DESCRIPTION);
    int level = CursorHelper.getIntFromCursor(cursor, LogGeneralDbAdapter.KEY_LEVEL);
    // Make an object out of it
    GeneralLog log = new GeneralLog(id, timestamp, text, level);
    return log;
  }

}
