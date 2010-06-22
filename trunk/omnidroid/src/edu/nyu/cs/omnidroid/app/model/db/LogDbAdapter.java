/*******************************************************************************
 * Copyright 2009 OmniDroid - http://code.google.com/p/omnidroid
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

import android.database.sqlite.SQLiteDatabase;

/**
 * This class represents a Omnidroid log. Logs are displayed on the ActivityEventLog for users to
 * see what is going on.
 */
public abstract class LogDbAdapter extends DbAdapter {
  public static final String LOG_ID = "Log ID";
  public static final String LOG_INFO = "Log information";

  public LogDbAdapter(SQLiteDatabase database) {
    super(database);
  }
}
