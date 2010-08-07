/*******************************************************************************
 * Copyright 2009, 2010 Omnidroid - http://code.google.com/p/omnidroid
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

import edu.nyu.cs.omnidroid.app.model.Log;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class represents a Omnidroid log. Logs are displayed on the ActivityEventLog for users to
 * see what is going on.
 */
public abstract class LogDbAdapter extends DbAdapter {
  /* Timestamp conversion constants */
  public static final int MILLISECONDS_TO_SECONDS = 1000;
  public static final int TIME_IN_MINUTE = 60 * MILLISECONDS_TO_SECONDS;
  public static final int TIME_IN_HOUR = 60 * TIME_IN_MINUTE;
  public static final int TIME_IN_DAY = 24 * TIME_IN_HOUR;

  /* Database Column Names */
  public static final String KEY_ID = "_ID";
  public static final String KEY_TIMESTAMP = "TimeStamp";
  public static final String KEY_DESCRIPTION = "Description";

  public LogDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  abstract public Cursor fetchAll();

  abstract public Cursor fetch(long id);

  abstract public long insert(Log log);

  /**
   * @return a Cursor that contains all Log records before timestamp
   */
  abstract public Cursor fetchAllBefore(long timestamp);

  /**
   * @return number of logs deleted that were before timestamp
   */
  abstract public int deleteAllBefore(long timestamp);

  abstract public boolean delete(long id);
}
