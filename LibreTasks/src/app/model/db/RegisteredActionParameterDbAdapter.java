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
 * Database helper class for the RegisteredActionParameters table. Defines basic CRUD methods.
 * <p>
 * This table contains all action parameter registered in Omnidroid. ActionParameterName is the
 * name of this parameter; FK_ActionID points to the action it belongs to; FK_DataTypeID points
 * to the dataType of this parameter.
 * </p>
 * <p>Note: Parameters belong to the same action should each has a unique name.<p>
 */
public class RegisteredActionParameterDbAdapter extends DbAdapter {

  /* Column names */
  public static final String KEY_ACTIONPARAMETERID = "ActionParameterID";
  public static final String KEY_ACTIONPARAMETERNAME = "ActionParameterName";
  public static final String KEY_ACTIONID = "FK_ActionID";
  public static final String KEY_DATATYPEID = "FK_DataTypeID";

  /* An array of all column names */
  public static final String[] KEYS = { KEY_ACTIONPARAMETERID, KEY_ACTIONPARAMETERNAME,
      KEY_ACTIONID, KEY_DATATYPEID };

  /* Table name */
  private static final String DATABASE_TABLE = "RegisteredActionParameters";

  /* Create and drop statement. */
  protected static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("
      + KEY_ACTIONPARAMETERID + " integer primary key autoincrement, " 
      + KEY_ACTIONPARAMETERNAME + " text not null, " 
      + KEY_ACTIONID + " integer, " 
      + KEY_DATATYPEID + " integer);";
  protected static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

  /**
   * Constructor.
   * 
   * @param database
   *          is the database object to work within.
   */
  public RegisteredActionParameterDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  /**
   * Insert a new RegisteredActionParameter record.
   * 
   * @param parameterName
   *          is the name of the parameter.
   * @param actionID
   *          is the id of the action it belongs to.
   * @param dataTypeID
   *          is the id of data type it has.
   * @return attribute id or -1 if creation failed.
   * @throws IllegalArgumentException
   *           if there is null within parameters
   */
  public long insert(String parameterName, Long actionID, Long dataTypeID) {
    if (parameterName == null || actionID == null || dataTypeID == null) {
      throw new IllegalArgumentException("insert parameter null.");
    }
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_ACTIONPARAMETERNAME, parameterName);
    initialValues.put(KEY_ACTIONID, actionID);
    initialValues.put(KEY_DATATYPEID, dataTypeID);
    // Set null because don't use 'null column hack'.
    return database.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * Delete a RegisteredActionParameter record
   * 
   * @param parameterID
   *          is the id of the record.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if parameterID is null
   */
  public boolean delete(Long parameterID) {
    if (parameterID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set the whereArgs to null here.
    return database.delete(DATABASE_TABLE, KEY_ACTIONPARAMETERID + "=" + parameterID, null) > 0;
  }

  /**
   * Delete all RegisteredActionParameter records.
   * 
   * @return true if success, or false if failed or nothing to be deleted.
   */
  public boolean deleteAll() {
    // Set where and whereArgs to null here.
    return database.delete(DATABASE_TABLE, null, null) > 0;
  }

  /**
   * Return a Cursor pointing to the record matches the parameterID.
   * 
   * @param parameterID
   *          is the parameter id
   * @return a Cursor pointing to the found record.
   * @throws IllegalArgumentException
   *           if parameterID is null
   */
  public Cursor fetch(Long parameterID) {
    if (parameterID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set selectionArgs, groupBy, having, orderBy and limit to be null.
    Cursor mCursor = database.query(true, DATABASE_TABLE, KEYS, KEY_ACTIONPARAMETERID + "="
        + parameterID, null, null, null, null, null);
    if (mCursor != null) {
      mCursor.moveToFirst();
    }
    return mCursor;
  }

  /**
   * @return a Cursor that contains all RegisteredActionParameter records.
   */
  public Cursor fetchAll() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, null);
  }

  /**
   * Return a Cursor contains all RegisteredActionParameter records which matches the parameters.
   * 
   * @param parameterName
   *          is the parameter name, or null to fetch any parameterName.
   * @param actionID
   *          is the action id, or null to fetch any actionID.
   * @param dataTypeID
   *          is the dataType id, or null to fetch any dataTypeID.
   * @return a Cursor contains all RegisteredActionParameter records which matches the parameters.
   */
  public Cursor fetchAll(String parameterName, Long actionID, Long dataTypeID) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(DATABASE_TABLE);
    qb.appendWhere("1=1");
    if (parameterName != null) {
      qb.appendWhere(" AND " + KEY_ACTIONPARAMETERNAME + " = ");
      qb.appendWhereEscapeString(parameterName);
    }
    if (actionID != null) {
      qb.appendWhere(" AND " + KEY_ACTIONID + " = " + actionID);
    }
    if (dataTypeID != null) {
      qb.appendWhere(" AND " + KEY_DATATYPEID + " = " + dataTypeID);
    }
    // Not using additional selections, selectionArgs, groupBy, having, orderBy, set them to null.
    return qb.query(database, KEYS, null, null, null, null, null);
  }

  /**
   * Update a RegisteredActionParameter record with specific parameters.
   * 
   * @param parameterID
   *          is the id of the record to be updated.
   * @param parameterName
   *          is the parameter name, or null if not updating it.
   * @param actionID
   *          is the action id, or null if not updating it.
   * @param dataTypeID
   *          is the dataType id, or null if not updating it.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if parameterID is null
   */
  public boolean update(Long parameterID, String parameterName, Long actionID, Long dataTypeID) {
    if (parameterID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    ContentValues args = new ContentValues();
    if (parameterName != null) {
      args.put(KEY_ACTIONPARAMETERNAME, parameterName);
    }
    if (actionID != null) {
      args.put(KEY_ACTIONID, actionID);
    }
    if (dataTypeID != null) {
      args.put(KEY_DATATYPEID, dataTypeID);
    }

    if (args.size() > 0) {
      // Set whereArg to null here
      return database.update(DATABASE_TABLE, args, KEY_ACTIONPARAMETERID + "=" + 
          parameterID, null) > 0;
    }
    return false;
  }


  public static String getSqliteCreateStatement() {
    return DATABASE_CREATE;
  }
}
