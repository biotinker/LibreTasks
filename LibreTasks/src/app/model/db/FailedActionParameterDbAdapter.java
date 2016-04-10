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
package libretasks.app.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * Database helper class for the FailedActionParameters table. Defines basic CRUD methods.
 * 
 * <p>
 * This table contains parameter association with each failedAction
 *  
 * FK_FailedActionID points to the failedAction it belongs to.
 * ActionParameterName is name of the parameter;
 * FK_FailedActionParameterData is the user defined data for this parameter
 * </p>
 */
public class FailedActionParameterDbAdapter extends DbAdapter {

  /* Column names */
  public static final String KEY_FAILEDACTIONPARAMETERID = "FailedActionParameterID";
  public static final String KEY_FAILEDACTIONID = "FK_FailedActionID";
  public static final String KEY_ACTIONPARAMETERNAME = "ActionParameterName";
  public static final String KEY_FAILEDACTIONPARAMETERDATA = "FailedActionParameterData";

  /* An array of all column names */
  public static final String[] KEYS = { KEY_FAILEDACTIONPARAMETERID, KEY_FAILEDACTIONID,
      KEY_ACTIONPARAMETERNAME, KEY_FAILEDACTIONPARAMETERDATA };

  /* Table name */
  private static final String DATABASE_TABLE = "FailedActionParameters";

  /* Create and drop statement. */
  protected static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("
      + KEY_FAILEDACTIONPARAMETERID + " integer primary key autoincrement, " 
      + KEY_FAILEDACTIONID + " integer not null, " 
      + KEY_ACTIONPARAMETERNAME + " text not null, "
      + KEY_FAILEDACTIONPARAMETERDATA + " text not null);";
  protected static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

  /**
   * Constructor.
   * 
   * @param database
   *          is the database object to work within.
   */
  public FailedActionParameterDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  /**
   * Insert a new FailedActionParameter record.
   * 
   * @param failedActionID
   *          is id of failed action it belongs to
   * @param key
   *          is id of its action parameter type
   * @param failedActionParameterData
   *          is the data associated with this parameter
   * @return FailedActionParameterID or -1 if creation failed.
   * @throws IllegalArgumentException
   *           if there is null within parameters
   */
  public long insert(Long failedActionID, String actionParameterName, 
      String failedActionParameterData){
    if (failedActionID == null || actionParameterName == null || failedActionParameterData == null){
      throw new IllegalArgumentException("insert parameter null.");
    }
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_FAILEDACTIONID, failedActionID);
    initialValues.put(KEY_ACTIONPARAMETERNAME, actionParameterName);
    initialValues.put(KEY_FAILEDACTIONPARAMETERDATA, failedActionParameterData);
    return database.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * Delete a FailedActionParameter record for failed action
   * 
   * @param failedActionID
   *          is the id of the record.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if failedActionParameterID is null
   */
  public boolean delete(Long failedActionID) {
    if (failedActionID == null) {
      throw new IllegalArgumentException("null");
    }
    // Set the whereArgs to null here.
    return database.delete(DATABASE_TABLE, KEY_FAILEDACTIONID + "=" + failedActionID, null) > 0;
  }

  /**
   * Delete all FailedActionParameter records.
   * 
   * @return true if success, or false if failed or nothing to be deleted.
   */
  public boolean deleteAll() {
    // Set where and whereArgs to null here.
    return database.delete(DATABASE_TABLE, null, null) > 0;
  }

  /**
   * Return a Cursor pointing to the record matches the FailedActionParameterID.
   * 
   * @param failedActionParameterID
   *          is the id of the record to be fetched.
   * @return a Cursor pointing to the found record.
   * 
   * @throws IllegalArgumentException
   *           if failedActionParameterID is null
   */
  public Cursor fetch(Long failedActionParameterID) {
    if (failedActionParameterID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set selectionArgs, groupBy, having, orderBy and limit to be null.
    Cursor mCursor = database.query(true, DATABASE_TABLE, KEYS, KEY_FAILEDACTIONPARAMETERID + "="
        + failedActionParameterID, null, null, null, null, null);
    if (mCursor != null) {
      mCursor.moveToFirst();
    }
    return mCursor;
  }

  /**
   * @return a Cursor that contains all FailedActionParameter records.
   */
  public Cursor fetchAll() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, null);
  }

  /**
   * Return a Cursor that contains all FailedActionParameter records which matches the parameters.
   * 
   * @param failedActionID
   *          is id of failed action it belongs to, or null to fetch any
   * @param actionParameterName
   *          name of action parameter, or null to fetch any
   * @param failedActionParameterData
   *          is the data associated with this parameter, or null to fetch any
   * @return a Cursor that contains all FailedActionParameter records which matches the parameters.
   */
  public Cursor fetchAll(Long failedActionID, String actionParameterName, 
      String failedActionParameterData) {

    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(DATABASE_TABLE);
    qb.appendWhere("1=1");
    if (failedActionID != null) {
      qb.appendWhere(" AND " + KEY_FAILEDACTIONID + " = " + failedActionID);
    }
    if (actionParameterName != null) {
      qb.appendWhere(" AND " + KEY_ACTIONPARAMETERNAME + " = " + actionParameterName);
    }
    if (failedActionParameterData != null) {
      qb.appendWhere(" AND " + KEY_FAILEDACTIONPARAMETERDATA + " = ");
      qb.appendWhereEscapeString(failedActionParameterData);
    }
    // Not using additional selections, selectionArgs, groupBy, having, orderBy, set them to null.
    return qb.query(database, KEYS, null, null, null, null, null);
  }

  /**
   * Update a FailedActionParameter record with specific parameters.
   * 
   * @param failedActionParameterID
   *          is the id of the record to be updated.
   * @param failedActionID
   *          is id of failed action it belongs to, or null if not updating it.
   * @param actionParameterName
   *          name of action parameter, or null if not updating it.
   * @param failedActionParameterData
   *          is the data associated with this parameter, or null if not updating it.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if failedActionParameterID is null
   */
  public boolean update(Long failedActionParameterID, Long failedActionID, 
      String actionParameterName, String failedActionParameterData) {

    if (failedActionParameterID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    ContentValues args = new ContentValues();
    if (failedActionID != null) {
      args.put(KEY_FAILEDACTIONID, failedActionID);
    }
    if (actionParameterName != null) {
      args.put(KEY_ACTIONPARAMETERNAME, actionParameterName);
    }
    if (failedActionParameterData != null) {
      args.put(KEY_FAILEDACTIONPARAMETERDATA, failedActionParameterData);
    }

    if (args.size() > 0) {
      // Set whereArg to null here
      return database.update(DATABASE_TABLE, args, KEY_FAILEDACTIONPARAMETERID + "="
          + failedActionParameterID, null) > 0;
    }
    return false;
  }
  
  public static String getSqliteCreateStatement() {
    return DATABASE_CREATE;
  }
}
