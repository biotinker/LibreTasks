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
package edu.nyu.cs.omnidroid.app.model.db;

import edu.nyu.cs.omnidroid.app.model.GeneralLog;
import edu.nyu.cs.omnidroid.app.model.Log;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Database Adapter class for the {@code LogGeneral} table. Defines basic CRUD methods.
 * <p>
 * This table records General Logs that Omnidroid handles.
 * </p>
 */
public class LogGeneralDbAdapter extends LogDbAdapter {

  /* Column names */
  // No specialty information for General logs.

  /* An array of all column names */
  public static final String[] KEYS = { KEY_ID, KEY_TIMESTAMP, KEY_DESCRIPTION };

  /* Table name */
  private static final String DATABASE_TABLE = "LogGeneral";

  /* Create and drop statement. */
  protected static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" + KEY_ID
      + " integer primary key autoincrement, " + KEY_TIMESTAMP + " integer, " + KEY_DESCRIPTION
      + " text not null);";
  protected static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

  public LogGeneralDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  /**
   * Insert a new General Log record.
   * 
   * @param timeStamp
   *          the time stamp of the action taken.
   * @return the row ID of the newly inserted row, or -1 if an error occurred
   */
  public long insert(Long timeStamp, String description) {
    if (timeStamp == null || description == null) {
      throw new IllegalArgumentException("insert parameter null.");
    }
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_TIMESTAMP, timeStamp);
    initialValues.put(KEY_DESCRIPTION, description);
    return database.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * deletes the specified LogAction from the database
   * 
   * @param id
   *          - ID of the database entry to delete
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if the primary id is null.
   */
  public boolean delete(Long id) throws IllegalArgumentException {
    if (id == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set the whereArgs to null here.
    return database.delete(DATABASE_TABLE, KEY_ID + "=" + id, null) > 0;
  }

  /**
   * Delete all LogAction records.
   * 
   * @return true if success, or false if failed or nothing to be deleted.
   */
  public boolean deleteAll() {
    // Set where and whereArgs to null here.
    return database.delete(DATABASE_TABLE, null, null) > 0;
  }

  /**
   * Return a Cursor pointing to the record matches the databse row id.
   * 
   * @param id
   *          - ID of the database entry to retrieve
   * @return the matching cursor.
   */
  public Cursor fetch(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("primary key null.");
    }

    // Set selectionArgs, groupBy, having, orderBy and limit to be null.
    Cursor mCursor = database.query(true, DATABASE_TABLE, KEYS, KEY_ID + "=" + id, null, null,
        null, null, null);
    if (mCursor != null) {
      mCursor.moveToFirst();
    }
    return mCursor;
  }

  /**
   * @return a Cursor that contains all {@code LogGeneral} records.
   */
  public Cursor fetchAll() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, null);
  }

  /**
   * @return the sql command to create this LogAction table
   */
  public String getSqliteCreateStatement() {
    return DATABASE_CREATE;
  }

  @Override
  public long insert(Log log) {
    GeneralLog myLog = (GeneralLog) log;
    return insert(myLog.getTimestamp(), myLog.getText());
  }
}
