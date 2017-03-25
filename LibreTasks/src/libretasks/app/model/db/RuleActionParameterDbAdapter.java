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
package libretasks.app.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * Database helper class for the RuleActionParameters table. Defines basic CRUD methods.
 * 
 * <p>
 * This table contains parameter association with each ruleAction as well as data that required to
 * feed in to these parameters.
 * 
 * FK_RuleActionID points to the ruleAction it belongs to. FK_ActionParameterID points to the
 * actionParameter record associated. FK_RuleActionParameterData is the user defined data for this
 * parameter
 * </p>
 */
public class RuleActionParameterDbAdapter extends DbAdapter {

  /* Column names */
  public static final String KEY_RULEACTIONPARAMETERID = "RuleActionParameterID";
  public static final String KEY_RULEACTIONID = "FK_RuleActionID";
  public static final String KEY_ACTIONPARAMETERID = "FK_ActionParameterID";
  public static final String KEY_RULEACTIONPARAMETERDATA = "FK_RuleActionParameterData";

  /* An array of all column names */
  public static final String[] KEYS = { KEY_RULEACTIONPARAMETERID, KEY_RULEACTIONID,
      KEY_ACTIONPARAMETERID, KEY_RULEACTIONPARAMETERDATA };

  /* Table name */
  private static final String DATABASE_TABLE = "RuleActionParameters";

  /* Create and drop statement. */
  protected static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("
      + KEY_RULEACTIONPARAMETERID + " integer primary key autoincrement, " 
      + KEY_RULEACTIONID + " integer not null, " 
      + KEY_ACTIONPARAMETERID + " integer not null, "
      + KEY_RULEACTIONPARAMETERDATA + " text not null);";
  protected static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

  /**
   * Constructor.
   * 
   * @param database
   *          is the database object to work within.
   */
  public RuleActionParameterDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  /**
   * Insert a new RuleActionParameter record.
   * 
   * @param ruleActionID
   *          is id of rule action it belongs to
   * @param actionParameterID
   *          is id of its action parameter type
   * @param ruleActionParameterData
   *          is the data associated with this parameter
   * @return RuleActionParameterID or -1 if creation failed.
   * @throws IllegalArgumentException
   *           if there is null within parameters
   */
  public long insert(Long ruleActionID, Long actionParameterID, String ruleActionParameterData) {
    if (ruleActionID == null || actionParameterID == null || ruleActionParameterData == null) {
      throw new IllegalArgumentException("insert parameter null.");
    }
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_RULEACTIONID, ruleActionID);
    initialValues.put(KEY_ACTIONPARAMETERID, actionParameterID);
    initialValues.put(KEY_RULEACTIONPARAMETERDATA, ruleActionParameterData);
    // Set null because don't use 'null column hack'.
    return database.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * Delete a RuleActionParameter record.
   * 
   * @param ruleActionParameterID
   *          is the id of the record.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if ruleActionParameterID is null
   */
  public boolean delete(Long ruleActionParameterID) {
    if (ruleActionParameterID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set the whereArgs to null here.
    return database.delete(DATABASE_TABLE, KEY_RULEACTIONPARAMETERID + "=" + ruleActionParameterID,
        null) > 0;
  }

  /**
   * Delete all RuleActionParameter records.
   * 
   * @return true if success, or false if failed or nothing to be deleted.
   */
  public boolean deleteAll() {
    // Set where and whereArgs to null here.
    return database.delete(DATABASE_TABLE, null, null) > 0;
  }

  /**
   * Return a Cursor pointing to the record matches the ruleActionParameterID.
   * 
   * @param ruleActionParameterID
   *          is the id of the record to be fetched.
   * @return a Cursor pointing to the found record.
   * 
   * @throws IllegalArgumentException
   *           if ruleActionParameterID is null
   */
  public Cursor fetch(Long ruleActionParameterID) {
    if (ruleActionParameterID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set selectionArgs, groupBy, having, orderBy and limit to be null.
    Cursor mCursor = database.query(true, DATABASE_TABLE, KEYS, KEY_RULEACTIONPARAMETERID + "="
        + ruleActionParameterID, null, null, null, null, null);
    if (mCursor != null) {
      mCursor.moveToFirst();
    }
    return mCursor;
  }

  /**
   * @return a Cursor that contains all RuleActionParameter records.
   */
  public Cursor fetchAll() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, null);
  }

  /**
   * Return a Cursor that contains all RuleActionParameter records which matches the parameters.
   * 
   * @param ruleActionID
   *          is id of rule action it belongs to, or null to fetch any
   * @param actionParameterID
   *          is id of its action parameter type, or null to fetch any
   * @param ruleActionParameterData
   *          is the data associated with this parameter, or null to fetch any
   * @return a Cursor that contains all RuleActionParameter records which matches the parameters.
   */
  public Cursor fetchAll(Long ruleActionID, Long actionParameterID, String ruleActionParameterData) {

    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(DATABASE_TABLE);
    qb.appendWhere("1=1");
    if (ruleActionID != null) {
      qb.appendWhere(" AND " + KEY_RULEACTIONID + " = " + ruleActionID);
    }
    if (actionParameterID != null) {
      qb.appendWhere(" AND " + KEY_ACTIONPARAMETERID + " = " + actionParameterID);
    }
    if (ruleActionParameterData != null) {
      qb.appendWhere(" AND " + KEY_RULEACTIONPARAMETERDATA + " = ");
      qb.appendWhereEscapeString(ruleActionParameterData);
    }
    // Not using additional selections, selectionArgs, groupBy, having, orderBy, set them to null.
    return qb.query(database, KEYS, null, null, null, null, null);
  }

  /**
   * Package protected method for performing a simple SQLite select query.
   * 
   * @param queryBuilder
   *          {@link SQLiteQueryBuilder} instance with the necessary query parameters
   */
  Cursor sqlQuery(SQLiteQueryBuilder queryBuilder) {
    queryBuilder.setTables(DATABASE_TABLE);
    return queryBuilder.query(database, null, null, null, null, null, null);
  }

  /**
   * Update a RuleActionParameter record with specific parameters.
   * 
   * @param ruleActionParameterID
   *          is the id of the record to be updated.
   * @param ruleActionID
   *          is id of rule action it belongs to, or null if not updating it.
   * @param actionParameterID
   *          is id of its action parameter type, or null if not updating it.
   * @param ruleActionParameterData
   *          is the data associated with this parameter, or null if not updating it.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if ruleActionParameterID is null
   */
  public boolean update(Long ruleActionParameterID, Long ruleActionID, Long actionParameterID,
      String ruleActionParameterData) {

    if (ruleActionParameterID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    ContentValues args = new ContentValues();
    if (ruleActionID != null) {
      args.put(KEY_RULEACTIONID, ruleActionID);
    }
    if (actionParameterID != null) {
      args.put(KEY_ACTIONPARAMETERID, actionParameterID);
    }
    if (ruleActionParameterData != null) {
      args.put(KEY_RULEACTIONPARAMETERDATA, ruleActionParameterData);
    }

    if (args.size() > 0) {
      // Set whereArg to null here
      return database.update(DATABASE_TABLE, args, KEY_RULEACTIONPARAMETERID + "="
          + ruleActionParameterID, null) > 0;
    }
    return false;
  }

  public static String getSqliteCreateStatement() {
    return DATABASE_CREATE;
  }
}
