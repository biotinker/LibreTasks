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

import static libretasks.app.model.CursorHelper.getLongFromCursor;
import static libretasks.app.model.CursorHelper.getStringFromCursor;

import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import libretasks.app.model.db.LogDbAdapter;
import libretasks.app.model.db.LogEventDbAdapter;

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
