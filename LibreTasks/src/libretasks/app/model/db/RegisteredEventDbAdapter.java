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
 * Copyright 2009 Omnidroid - http://code.google.com/p/omnidroid
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
package libretasks.app.model.db;

import libretasks.app.model.CursorHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * Database helper class for the RegisteredEvents table. Defines basic CRUD methods.
 * 
 * <p>
 * This table contains all events registered in Omnidroid. EventName is the name of that event;
 * FK_AppID points to the application it belongs to.
 * </p>
 * <p>
 * Note: Events belong to the same app should each has a unique name.
 * <p>
 */
public class RegisteredEventDbAdapter extends DbAdapter {

  /* Column names */
  public static final String KEY_EVENTID = "EventID";
  public static final String KEY_EVENTNAME = "EventName";
  public static final String KEY_APPID = "FK_AppID";

  /* An array of all column names */
  public static final String[] KEYS = { KEY_EVENTID, KEY_EVENTNAME, KEY_APPID };

  /* Table name */
  private static final String DATABASE_TABLE = "RegisteredEvents";

  /* Create and drop statement. */
  protected static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("
      + KEY_EVENTID + " integer primary key autoincrement, " + KEY_EVENTNAME + " text not null, "
      + KEY_APPID + " integer);";
  protected static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

  /**
   * Constructor.
   * 
   * @param database
   *          is the database object to work within.
   */
  public RegisteredEventDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  /**
   * Insert a new RegisteredEvent record given the name of the event and details about the
   * application. Note that a record with {@code appName} with package name {@code appPackageName}
   * should exist on the registered application table.
   * 
   * @param eventName
   *          the name of the event
   * @param appName
   *          the name of the application associated with the event
   * @param appPackageName
   *          the package name of the application with the event
   * @return the primary key id of the new record if successful, -1 otherwise
   * @throws IllegalArgumentException
   *           if eventName is null or if no application named {@code appName} with package name
   *           {@code appPackageName} does not exist
   */
  public long insert(String eventName, String appName, String appPackageName) {
    RegisteredAppDbAdapter appDbAdapter = new RegisteredAppDbAdapter(database);
    Cursor appDbCursor = appDbAdapter.fetchAll(appName, appPackageName, null);

    /**
     * Just get the first result. The identifiers used in the query should already be unique enough
     * that it should just return one record if it existed.
     */
    appDbCursor.moveToFirst();

    long appIdPhone;
    try {
      appIdPhone = CursorHelper.getLongFromCursor(appDbCursor, RegisteredAppDbAdapter.KEY_APPID);
    } catch (CursorIndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Application named" + appName + " with package name "
          + appPackageName + " does not exist.");
    } finally {
      appDbCursor.close();
    }

    return insert(eventName, appIdPhone);
  }

  /**
   * Insert a new RegisteredEvent record
   * 
   * @param eventName
   *          is the event name
   * @param appID
   *          is the application id
   * @return eventID or -1 if creation failed.
   * @throws IllegalArgumentException
   *           if there is null within parameters
   */
  public long insert(String eventName, Long appID) {
    if (eventName == null || appID == null) {
      throw new IllegalArgumentException("insert parameter null.");
    }
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_EVENTNAME, eventName);
    initialValues.put(KEY_APPID, appID);
    // Set null because don't use 'null column hack'.
    return database.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * Delete a RegisteredEvent record
   * 
   * @param eventID
   *          is the id of the record.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if eventID is null
   */
  public boolean delete(Long eventID) {
    if (eventID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set the whereArgs to null here.
    return database.delete(DATABASE_TABLE, KEY_EVENTID + "=" + eventID, null) > 0;
  }

  /**
   * Delete all RegisteredEvent records.
   * 
   * @return true if success, or false if failed or nothing to be deleted.
   */
  public boolean deleteAll() {
    // Set where and whereArgs to null here.
    return database.delete(DATABASE_TABLE, null, null) > 0;
  }

  /**
   * Return a Cursor pointing to the record matches the eventID.
   * 
   * @param eventID
   *          is the event id
   * @return a Cursor pointing to the found record.
   * @throws IllegalArgumentException
   *           if eventID is null
   */
  public Cursor fetch(Long eventID) {
    if (eventID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set selectionArgs, groupBy, having, orderBy and limit to be null.
    Cursor mCursor = database.query(true, DATABASE_TABLE, KEYS, KEY_EVENTID + "=" + eventID, null,
        null, null, null, null);
    if (mCursor != null) {
      mCursor.moveToFirst();
    }
    return mCursor;
  }

  /**
   * @return a Cursor that contains all RegisteredEvent records.
   */
  public Cursor fetchAll() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, null);
  }

  /**
   * Get all registered events in the database with the event names alphabetically ordered.
   * 
   * @return a Cursor that contains all RegisteredEvent records
   */
  public Cursor fetchAllOrdered() {
    // Set selections, selectionArgs, groupBy, having, to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, KEY_EVENTNAME + " ASC");
  }

  /**
   * Return a Cursor that contains all RegisteredEvent records which matches the parameters.
   * 
   * @param eventName
   *          is the event name, or null to fetch any eventName
   * @param appID
   *          is the application id, or null to fetch any appID
   * @return a Cursor that contains all RegisteredEvent records which matches the parameters.
   */
  public Cursor fetchAll(String eventName, Long appID) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(DATABASE_TABLE);
    qb.appendWhere("1=1");
    if (eventName != null) {
      qb.appendWhere(" AND " + KEY_EVENTNAME + " = ");
      qb.appendWhereEscapeString(eventName);
    }
    if (appID != null) {
      qb.appendWhere(" AND " + KEY_APPID + " = " + appID);
    }
    // Not using additional selections, selectionArgs, groupBy, having, orderBy, set them to null.
    return qb.query(database, KEYS, null, null, null, null, null);
  }

  /**
   * Update a RegisteredEvent record with specific parameters.
   * 
   * @param eventID
   *          is the id of the record to be updated.
   * @param eventName
   *          is the event name, or null if not updating it.
   * @param appID
   *          is the application id, or null if not updating it.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if eventID is null
   */
  public boolean update(Long eventID, String eventName, Long appID) {
    if (eventID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    ContentValues args = new ContentValues();
    if (eventName != null) {
      args.put(KEY_EVENTNAME, eventName);
    }
    if (appID != null) {
      args.put(KEY_APPID, appID);
    }

    if (args.size() > 0) {
      // Set whereArg to null here
      return database.update(DATABASE_TABLE, args, KEY_EVENTID + "=" + eventID, null) > 0;
    }
    return false;
  }

  public static String getSqliteCreateStatement() {
    return DATABASE_CREATE;
  }
}
