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
 * Database adapter class for the ExternalAttributes table. Defines basic CRUD methods. 
 * 
 * <p>
 * This table contains all external attributes that Omnidroid support. ExternalAttributeName is 
 * the name of the attribute; FK_AppID points to the app it belongs to; FK_DataTypeID points to
 * its data type. 
 * </p>
 * 
 * <p>Note: Every External attributes should have a unique name.<p>
 */
public class ExternalAttributeDbAdapter extends DbAdapter {

  /* Column names */
  public static final String KEY_EXTERNALATTRIBUTEID = "ExternalAttributeID";
  public static final String KEY_EXTERNALATTRIBUTENAME = "ExternalAttributeName";
  public static final String KEY_APPID = "FK_AppID";
  public static final String KEY_DATATYPEID = "FK_DataTypeID";

  /* An array of all column names */
  public static final String[] KEYS = { KEY_EXTERNALATTRIBUTEID, KEY_EXTERNALATTRIBUTENAME,
      KEY_APPID, KEY_DATATYPEID };

  /* Table name */
  private static final String DATABASE_TABLE = "ExternalAttributes";

  /* Create and drop statement. */
  protected static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("
      + KEY_EXTERNALATTRIBUTEID + " integer primary key autoincrement, "
      + KEY_EXTERNALATTRIBUTENAME + " text not null, " 
      + KEY_APPID + " integer, " 
      + KEY_DATATYPEID + " integer);";
  protected static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

  /**
   * Constructor.
   * 
   * @param database
   *          is the context to work within.
   */
  public ExternalAttributeDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  /**
   * Insert a new ExternalAttribute record
   * 
   * @param attributeName
   *          is the attribute name
   * @param appID
   *          is the application id that it belongs to
   * @param dataTypeID
   *          is the dataType id that it belongs to
   * @return attributeID or -1 if creation failed.
   * @throws IllegalArgumentException
   *           if there is null within parameters
   */
  public long insert(String attributeName, Long appID, Long dataTypeID) {
    if (attributeName == null || appID == null || dataTypeID == null) {
      throw new IllegalArgumentException("insert parameter null.");
    }
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_EXTERNALATTRIBUTENAME, attributeName);
    initialValues.put(KEY_APPID, appID);
    initialValues.put(KEY_DATATYPEID, dataTypeID);
    // Set null because don't use 'null column hack'.
    return database.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * Delete a ExternalAttribute record
   * 
   * @param attributeID
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if attributeID is null
   */
  public boolean delete(Long attributeID) {
    if (attributeID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set the whereArgs to null here.
    return database.delete(DATABASE_TABLE, KEY_EXTERNALATTRIBUTEID + "=" + attributeID, null) > 0;
  }

  /**
   * Delete all ExternalAttribute records.
   * 
   * @return true if success, or false if failed or nothing to be deleted.
   */
  public boolean deleteAll() {
    // Set where and whereArgs to null here.
    return database.delete(DATABASE_TABLE, null, null) > 0;
  }

  /**
   * Return a Cursor pointing to the record matches the attributeID.
   * 
   * @param attributeID
   *          is the attribute id
   * @return a Cursor pointing to the found record.
   * @throws IllegalArgumentException
   *           if attributeID is null
   */
  public Cursor fetch(Long attributeID) {
    if (attributeID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set selectionArgs, groupBy, having, orderBy and limit to be null.
    Cursor mCursor = database.query(true, DATABASE_TABLE, KEYS, KEY_EXTERNALATTRIBUTEID + "="
        + attributeID, null, null, null, null, null);
    if (mCursor != null) {
      mCursor.moveToFirst();
    }
    return mCursor;
  }

  /**
   * @return a Cursor that contains all ExternalAttribute records.
   */
  public Cursor fetchAll() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, null);
  }

  /**
   * Return a Cursor that contains all ExternalAttribute records which matches the parameters
   * 
   * @param attributeName
   *          is the attribute name, or null to fetch any actionName
   * @param appID
   *          is the application id, or null to fetch any appID
   * @param dataTypeID
   *          is the dataType id, or null to fetch any dataTypeID
   * @return a Cursor that contains all RegisteredAction records which matches the parameters.
   */
  public Cursor fetchAll(String attributeName, Long appID, Long dataTypeID) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(DATABASE_TABLE);
    qb.appendWhere("1=1");
    if (attributeName != null) {
      qb.appendWhere(" AND " + KEY_EXTERNALATTRIBUTENAME + " = ");
      qb.appendWhereEscapeString(attributeName);
    }
    if (appID != null) {
      qb.appendWhere(" AND " + KEY_APPID + " = " + appID);
    }
    if (dataTypeID != null) {
      qb.appendWhere(" AND " + KEY_DATATYPEID + " = " + dataTypeID);
    }
    // Not using additional selections, selectionArgs, groupBy, having, orderBy, set them to null.
    return qb.query(database, KEYS, null, null, null, null, null);
  }

  /**
   * Update a ExternalAttribute record with specific parameters.
   * 
   * @param attributeID
   *          is the id of the record to be updated.
   * @param attributeName
   *          is the attribute name, or null if not updating it.
   * @param appID
   *          is the application id, or null if not updating it.
   * @param dataTypeID
   *          is the dataType id, or null if not updating it.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if attributeID is null
   */
  public boolean update(Long attributeID, String attributeName, Long appID, Long dataTypeID) {
    if (attributeID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    ContentValues args = new ContentValues();
    if (attributeName != null) {
      args.put(KEY_EXTERNALATTRIBUTENAME, attributeName);
    }
    if (appID != null) {
      args.put(KEY_APPID, appID);
    }
    if (dataTypeID != null) {
      args.put(KEY_DATATYPEID, dataTypeID);
    }

    if (args.size() > 0) {
      // Set whereArg to null here
      return database.update(DATABASE_TABLE, args, KEY_EXTERNALATTRIBUTEID + "=" + attributeID,
          null) > 0;
    }
    return false;
  }
  
  public static String getSqliteCreateStatement() {
    return DATABASE_CREATE;
  }
}
