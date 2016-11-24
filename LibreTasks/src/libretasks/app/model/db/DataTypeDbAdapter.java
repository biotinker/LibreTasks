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
 * Database helper class for the DataTypes table. Defines basic CRUD methods. 
 * <p>
 * This table contains all data types that registered in Omnidroid. Every datatype have a name 
 * and class name. The class name is the class that actually implement this datatype.
 * </p>
 * <p>
 * Every data type should have a unique name
 * </p>
 */
public class DataTypeDbAdapter extends DbAdapter {

  /* Column names */
  public static final String KEY_DATATYPEID = "DataTypeID";
  public static final String KEY_DATATYPENAME = "DataTypeName";
  public static final String KEY_DATATYPECLASSNAME = "DataTypeClassName";

  /* An array of all column names */
  public static final String[] KEYS = { KEY_DATATYPEID, KEY_DATATYPENAME, KEY_DATATYPECLASSNAME };

  /* Table name */
  private static final String DATABASE_TABLE = "DataTypes";

  /* Create and drop statement. */
  protected static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("
      + KEY_DATATYPEID + " integer primary key autoincrement, " 
      + KEY_DATATYPENAME + " text not null, " 
      + KEY_DATATYPECLASSNAME + " text not null);";
  protected static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

  /**
   * Constructor.
   * 
   * @param database
   *          is the database object to work within.
   */
  public DataTypeDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  /**
   * Insert a new DataType record.
   * 
   * @param dataTypeName
   *          is the name of the data type.
   * @param dataTypeClassName
   *          is the name of the data type class.
   * @return appID or -1 if creation failed.
   * @throws IllegalArgumentException
   *           if there is null within parameters
   */
  public long insert(String dataTypeName, String dataTypeClassName) {
    if (dataTypeName == null || dataTypeClassName == null) {
      throw new IllegalArgumentException("insert parameter null.");
    }
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_DATATYPENAME, dataTypeName);
    initialValues.put(KEY_DATATYPECLASSNAME, dataTypeClassName);
    // Set null because don't use 'null column hack'.
    return database.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * Delete a DataType record.
   * 
   * @param dataTypeID
   *          is the id of the record.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if dataTypeID is null
   */
  public boolean delete(Long dataTypeID) {
    if (dataTypeID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set the whereArgs to null here.
    return database.delete(DATABASE_TABLE, KEY_DATATYPEID + "=" + dataTypeID, null) > 0;
  }

  /**
   * Delete all DataType records.
   * 
   * @return true if success, or false if failed or nothing to be deleted.
   */
  public boolean deleteAll() {
    // Set where and whereArgs to null here.
    return database.delete(DATABASE_TABLE, null, null) > 0;
  }

  /**
   * Return a Cursor pointing to the record matches the dataTypeID.
   * 
   * @param dataTypeID
   *          is the id of the record to be fetched.
   * @return a Cursor pointing to the found record.
   * @throws IllegalArgumentException
   *           if dataTypeID is null
   */
  public Cursor fetch(Long dataTypeID) {
    if (dataTypeID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set selectionArgs, groupBy, having, orderBy and limit to be null.
    Cursor mCursor = database.query(true, DATABASE_TABLE, KEYS, KEY_DATATYPEID + "=" + dataTypeID,
        null, null, null, null, null);
    if (mCursor != null) {
      mCursor.moveToFirst();
    }
    return mCursor;
  }

  /**
   * @return a Cursor that contains all DataType records.
   */
  public Cursor fetchAll() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, null);
  }

  /**
   * Return a Cursor that contains all DataType records which matches the parameters.
   * 
   * @param dataTypeName
   *          is the data type name or null to fetch any dataTypeName.
   * @param dataTypeClassName
   *          is the name of the data type class.
   * @return a Cursor that contains all DataType records which matches the parameters.
   */
  public Cursor fetchAll(String dataTypeName, String dataTypeClassName) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(DATABASE_TABLE);
    qb.appendWhere("1=1");
    if (dataTypeName != null) {
      qb.appendWhere(" AND " + KEY_DATATYPENAME + " = ");
      qb.appendWhereEscapeString(dataTypeName);
    }
    if (dataTypeClassName != null) {
      qb.appendWhere(" AND " + KEY_DATATYPECLASSNAME + " = ");
      qb.appendWhereEscapeString(dataTypeClassName);
    }
    // Not using additional selections, selectionArgs, groupBy, having, orderBy, set them to null.
    return qb.query(database, KEYS, null, null, null, null, null);
  }

  /**
   * Update a DataType record with specific parameters.
   * 
   * @param dataTypeID
   *          is the id of the record to be updated.
   * @param dataTypeName
   *          is the data type name or null if not updating it.
   * @param dataTypeClassName
   *          is the name of the data type class.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if dataTypeID is null
   */
  public boolean update(Long dataTypeID, String dataTypeName, String dataTypeClassName) {
    if (dataTypeID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    ContentValues args = new ContentValues();
    if (dataTypeName != null) {
      args.put(KEY_DATATYPENAME, dataTypeName);
    }
    if (dataTypeClassName != null) {
      args.put(KEY_DATATYPECLASSNAME, dataTypeClassName);
    }

    if (args.size() > 0) {
      // Set whereArg to null here
      return database.update(DATABASE_TABLE, args, KEY_DATATYPEID + "=" + dataTypeID, null) > 0;
    }
    return false;
  }


  public static String getSqliteCreateStatement() {
    return DATABASE_CREATE;
  }
}
