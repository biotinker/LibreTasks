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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * Database helper class for the RegisteredActions table. Defines basic CRUD methods. 
 * <p>
 * This table records all actions registered in Omnidroid. ActionName is the name of the action, 
 * FK_AppID points to the application it belongs to. Every application could have multiple actions.
 * </p>
 * <p>Note: Actions belong to the same app should each has a unique name.<p>
 */
public class RegisteredActionDbAdapter extends DbAdapter {

  /* Column names */
  public static final String KEY_ACTIONID = "ActionID";
  public static final String KEY_ACTIONNAME = "ActionName";
  public static final String KEY_APPID = "FK_AppID";

  /* An array of all column names */
  public static final String[] KEYS = { KEY_ACTIONID, KEY_ACTIONNAME, KEY_APPID };

  /* Table name */
  private static final String DATABASE_TABLE = "RegisteredActions";

  /* Create and drop statement. */
  protected static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("
      + KEY_ACTIONID + " integer primary key autoincrement, " 
      + KEY_ACTIONNAME + " text not null, "
      + KEY_APPID + " integer);";
  protected static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

  /**
   * Constructor.
   * 
   * @param database
   *          is the context to work within.
   */
  public RegisteredActionDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  /**
   * Insert a new RegisteredAction record
   * 
   * @param actionName
   *          is the actionName
   * @param appID
   *          is the application id
   * @return actionID or -1 if creation failed.
   * @throws IllegalArgumentException
   *           if there is null within parameters
   */
  public long insert(String actionName, Long appID) {
    if (actionName == null || appID == null) {
      throw new IllegalArgumentException("insert parameter null.");
    }
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_ACTIONNAME, actionName);
    initialValues.put(KEY_APPID, appID);
    // Set null because don't use 'null column hack'.
    return database.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * Delete a RegisteredAction record
   * 
   * @param actionID
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if actionID is null
   */
  public boolean delete(Long actionID) {
    if (actionID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set the whereArgs to null here.
    return database.delete(DATABASE_TABLE, KEY_ACTIONID + "=" + actionID, null) > 0;
  }

  /**
   * Delete all RegisteredAction records.
   * 
   * @return true if success, or false if failed or nothing to be deleted.
   */
  public boolean deleteAll() {
    // Set where and whereArgs to null here.
    return database.delete(DATABASE_TABLE, null, null) > 0;
  }

  /**
   * Return a Cursor pointing to the record matches the actionID.
   * 
   * @param actionID
   *          is the action id
   * @return a Cursor pointing to the found record.
   * @throws IllegalArgumentException
   *           if actionID is null
   */
  public Cursor fetch(Long actionID) {
    if (actionID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set selectionArgs, groupBy, having, orderBy and limit to be null.
    Cursor mCursor = database.query(true, DATABASE_TABLE, KEYS, KEY_ACTIONID + "=" + actionID,
        null, null, null, null, null);
    if (mCursor != null) {
      mCursor.moveToFirst();
    }
    return mCursor;
  }

  /**
   * @return a Cursor that contains all RegisteredAction records.
   */
  public Cursor fetchAll() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, null);
  }

  /**
   * Return a Cursor that contains all RegisteredAction records which matches the parameters
   * 
   * @param actionName
   *          is the actionName, or null to fetch any actionName
   * @param appID
   *          is the application id, or null to fetch any appID
   * @return a Cursor that contains all RegisteredAction records which matches the parameters.
   */
  public Cursor fetchAll(String actionName, Long appID) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(DATABASE_TABLE);
    qb.appendWhere("1=1");
    if (actionName != null) {
      qb.appendWhere(" AND " + KEY_ACTIONNAME + " = ");
      qb.appendWhereEscapeString(actionName);
    }
    if (appID != null) {
      qb.appendWhere(" AND " + KEY_APPID + " = " + appID);
    }
    // Not using additional selections, selectionArgs, groupBy, having, orderBy, set them to null.
    return qb.query(database, KEYS, null, null, null, null, null);
  }

  /**
   * Update a RegisteredAction record with specific parameters.
   * 
   * @param actionID
   *          is the id of the record to be updated.
   * @param actionName
   *          is the action name, or null if not updating it.
   * @param appID
   *          is the application id, or null if not updating it.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if actionID is null
   */
  public boolean update(Long actionID, String actionName, Long appID) {
    if (actionID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    ContentValues args = new ContentValues();
    if (actionName != null) {
      args.put(KEY_ACTIONNAME, actionName);
    }
    if (appID != null) {
      args.put(KEY_APPID, appID);
    }

    if (args.size() > 0) {
      // Set whereArg to null here
      return database.update(DATABASE_TABLE, args, KEY_ACTIONID + "=" + actionID, null) > 0;
    }
    return false;
  }


  public static String getSqliteCreateStatement() {
    return DATABASE_CREATE;
  }
}
