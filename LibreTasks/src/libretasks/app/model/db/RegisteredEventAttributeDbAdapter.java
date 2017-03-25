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
 * Database helper class for the RegisteredEventAttributes table. Defines basic CRUD methods.
 * 
 * <p>
 * This table contains all event attributes registered in Omnidroid. EventAttributeName is the name
 * of that attribute; FK_EventID points to the event it belongs to; FK_DataTypeID points the
 * dataType of this attribute.
 * </p>
 * <p>
 * Note: Attributes belong to the same event should each has a unique name.
 * <p>
 */
public class RegisteredEventAttributeDbAdapter extends DbAdapter {

  /* Column names */
  public static final String KEY_EVENTATTRIBUTEID = "EventAttributeID";
  public static final String KEY_EVENTATTRIBUTENAME = "EventAttributeName";
  public static final String KEY_EVENTID = "FK_EventID";
  public static final String KEY_DATATYPEID = "FK_DataTypeID";

  /* An array of all column names */
  public static final String[] KEYS = { KEY_EVENTATTRIBUTEID, KEY_EVENTATTRIBUTENAME, KEY_EVENTID,
      KEY_DATATYPEID };

  /* Table name */
  private static final String DATABASE_TABLE = "RegisteredEventAttributes";
  /*
   * FK_EventID used for global attributes. -1 is used since it is the number that will not appear
   * as the eventID of the RegisteredEvents table.
   */
  private static final long GLOBAL_ATTRIBUTE_DB_ID = -1L;

  /* Create and drop statement. */
  protected static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("
      + KEY_EVENTATTRIBUTEID + " integer primary key autoincrement, " + KEY_EVENTATTRIBUTENAME
      + " text not null, " + KEY_EVENTID + " integer, " + KEY_DATATYPEID + " integer);";
  protected static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

  /**
   * Constructor.
   * 
   * @param database
   *          is the database object to work within.
   */
  public RegisteredEventAttributeDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  /**
   * Insert a new RegisteredEventAttribute record.
   * 
   * @param attributeName
   *          is the name of the event attribute.
   * @param eventID
   *          is the id of the event it belongs to.
   * @param dataTypeID
   *          is the id of data type it has.
   * @return attribute id or -1 if creation failed.
   * @throws IllegalArgumentException
   *           if there is null within parameters
   */
  public long insert(String attributeName, Long eventID, Long dataTypeID) {
    if (attributeName == null || eventID == null || dataTypeID == null) {
      throw new IllegalArgumentException("insert parameter null.");
    }
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_EVENTATTRIBUTENAME, attributeName);
    initialValues.put(KEY_EVENTID, eventID);
    initialValues.put(KEY_DATATYPEID, dataTypeID);
    // Set null because don't use 'null column hack'.
    return database.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * Insert a new RegisteredEventAttribute record.
   * 
   * @param attributeName
   *          is the name of the event attribute.
   * @param dataTypeID
   *          is the id of data type it has.
   * @return attribute id or -1 if creation failed.
   * @throws IllegalArgumentException
   *           if there is null within parameters
   */
  public long insertGeneralAttribute(String attributeName, Long dataTypeID) {
    return insert(attributeName, GLOBAL_ATTRIBUTE_DB_ID, dataTypeID);
  }

  /**
   * Delete a RegisteredEventAttribute record
   * 
   * @param attributeID
   *          is the id of the record.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if attributeID is null
   */
  public boolean delete(Long attributeID) {
    if (attributeID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set the whereArgs to null here.
    return database.delete(DATABASE_TABLE, KEY_EVENTATTRIBUTEID + "=" + attributeID, null) > 0;
  }

  /**
   * Delete all RegisteredEventAttribute records.
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
    Cursor mCursor = database.query(true, DATABASE_TABLE, KEYS, KEY_EVENTATTRIBUTEID + "="
        + attributeID, null, null, null, null, null);
    if (mCursor != null) {
      mCursor.moveToFirst();
    }
    return mCursor;
  }

  /**
   * @return a Cursor that contains all RegisteredEventAttribute records.
   */
  public Cursor fetchAll() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, null);
  }

  /**
   * @return a Cursor that contains all RegisteredEventAttribute records that applies to all events.
   */
  public Cursor fetchAllGlobalAttributes() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, KEY_EVENTID + " = " + GLOBAL_ATTRIBUTE_DB_ID, null,
        null, null, null);
  }

  /**
   * @return a Cursor that contains all RegisteredEventAttribute records that are specific for
   *         certain events.
   */
  public Cursor fetchAllSpecificAttibutes() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, KEY_EVENTID + " != " + GLOBAL_ATTRIBUTE_DB_ID,
        null, null, null, null);
  }

  /**
   * Return a Cursor contains all RegisteredEventAttribute records which matches the parameters.
   * 
   * @param attributeName
   *          is the attribute name, or null to fetch any attributeName
   * @param eventID
   *          is the event id, or null to fetch any eventID
   * @param dataTypeID
   *          is the dataType id, or null to fetch any dataTypeID
   * @return a Cursor contains all RegisteredEventAttribute records which matches the parameters.
   */
  public Cursor fetchAll(String attributeName, Long eventID, Long dataTypeID) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(DATABASE_TABLE);
    qb.appendWhere("1=1");
    if (attributeName != null) {
      qb.appendWhere(" AND " + KEY_EVENTATTRIBUTENAME + " = ");
      qb.appendWhereEscapeString(attributeName);
    }
    if (eventID != null) {
      qb.appendWhere(" AND " + KEY_EVENTID + " = " + eventID);
    }
    if (dataTypeID != null) {
      qb.appendWhere(" AND " + KEY_DATATYPEID + " = " + dataTypeID);
    }
    // Not using additional selections, selectionArgs, groupBy, having, orderBy, set them to null.
    return qb.query(database, KEYS, null, null, null, null, null);
  }

  /**
   * Update a RegisteredEventAttribute record with specific parameters.
   * 
   * @param attributeID
   *          is the id of the record to be updated.
   * @param attributeName
   *          is the attribute name, or null if not updating it.
   * @param eventID
   *          is the event id, or null if not updating it.
   * @param dataTypeID
   *          is the dataType id, or null if not updating it.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if attributeID is null
   */
  public boolean update(Long attributeID, String attributeName, Long eventID, Long dataTypeID) {
    if (attributeID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    ContentValues args = new ContentValues();
    if (attributeName != null) {
      args.put(KEY_EVENTATTRIBUTENAME, attributeName);
    }
    if (eventID != null) {
      args.put(KEY_EVENTID, eventID);
    }
    if (dataTypeID != null) {
      args.put(KEY_DATATYPEID, dataTypeID);
    }

    if (args.size() > 0) {
      // Set whereArg to null here
      return database.update(DATABASE_TABLE, args, KEY_EVENTATTRIBUTEID + "=" + attributeID, null) > 0;
    }
    return false;
  }

  public static String getSqliteCreateStatement() {
    return DATABASE_CREATE;
  }
}
