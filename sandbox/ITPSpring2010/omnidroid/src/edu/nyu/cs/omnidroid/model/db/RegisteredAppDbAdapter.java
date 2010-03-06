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
package edu.nyu.cs.omnidroid.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * Database helper class for the RegisteredApps table. Defines basic CRUD methods.  
 * <p>
 * This table contains all applications registered in Omnidroid. For each application record: 
 * AppName is name of the application; PkgName is the package name of the application; 
 * Enabled is whether this application is enabled in Omnidroid
 * </p>
 * <p>
 * Note: Every app should have a unique name
 * </p>
 */
public class RegisteredAppDbAdapter extends DbAdapter {

  /* Column names */
  public static final String KEY_APPID = "AppID";
  public static final String KEY_APPNAME = "AppName";
  public static final String KEY_PKGNAME = "PkgName";
  public static final String KEY_ENABLED = "Enabled";

  /* An array of all column names */
  public static final String[] KEYS = { KEY_APPID, KEY_APPNAME, KEY_PKGNAME, KEY_ENABLED };

  /* Table name */
  private static final String DATABASE_TABLE = "RegisteredApps";

  /* Create and drop statement. */
  protected static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("
      + KEY_APPID + " integer primary key autoincrement, " 
      + KEY_APPNAME + " text not null, "
      + KEY_PKGNAME + " text not null, " 
      + KEY_ENABLED + " integer);";
  protected static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

  /**
   * Constructor.
   * 
   * @param database
   *          is the database object to work within.
   */
  public RegisteredAppDbAdapter(SQLiteDatabase database) {
    super(database);
  }

  /**
   * Insert a new RegisteredApp record.
   * 
   * @param appName
   *          is the name of the application.
   * @param pkgName
   *          is the package name of the application.
   * @param enabled
   *          is whether the application is activated.
   * @return appID or -1 if creation failed.
   * @throws IllegalArgumentException
   *           if there is null within parameters
   */
  public long insert(String appName, String pkgName, Boolean enabled) {
    if (appName == null || pkgName == null || enabled == null) {
      throw new IllegalArgumentException("insert parameter null.");
    }
    ContentValues initialValues = new ContentValues();
    initialValues.put(KEY_APPNAME, appName);
    initialValues.put(KEY_PKGNAME, pkgName);
    initialValues.put(KEY_ENABLED, enabled);
    // Set null because don't use 'null column hack'.
    return database.insert(DATABASE_TABLE, null, initialValues);
  }

  /**
   * Delete a RegisteredApp record.
   * 
   * @param appID
   *          is the id of the record.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if appID is null
   */
  public boolean delete(Long appID) {
    if (appID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set the whereArgs to null here.
    return database.delete(DATABASE_TABLE, KEY_APPID + "=" + appID, null) > 0;
  }

  /**
   * Delete all RegisteredApp records.
   * 
   * @return true if success, or false if failed or nothing to be deleted.
   */
  public boolean deleteAll() {
    // Set where and whereArgs to null here.
    return database.delete(DATABASE_TABLE, null, null) > 0;
  }

  /**
   * Return a Cursor pointing to the record matches the appID.
   * 
   * @param appID
   *          is the id of the record to be fetched.
   * @return a Cursor pointing to the found record.
   * @throws IllegalArgumentException
   *           if appID is null
   */
  public Cursor fetch(Long appID) {
    if (appID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    // Set selectionArgs, groupBy, having, orderBy and limit to be null.
    Cursor mCursor = database.query(true, DATABASE_TABLE, KEYS, KEY_APPID + "=" + appID, null,
        null, null, null, null);
    if (mCursor != null) {
      mCursor.moveToFirst();
    }
    return mCursor;
  }

  /**
   * @return a Cursor that contains all RegisteredApp records.
   */
  public Cursor fetchAll() {
    // Set selections, selectionArgs, groupBy, having, orderBy to null to fetch all rows.
    return database.query(DATABASE_TABLE, KEYS, null, null, null, null, null);
  }

  /**
   * Return a Cursor that contains all RegisteredApp records which matches the parameters.
   * 
   * @param appName
   *          is the application name or null to fetch any appName.
   * @param pkgName
   *          is the package name or null to fetch any pkgName.
   * @param enabled
   *          is whether the application is activated or null to fetch any enabled status.
   * @return a Cursor that contains all RegisteredApp records which matches the parameters.
   */
  public Cursor fetchAll(String appName, String pkgName, Boolean enabled) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(DATABASE_TABLE);
    qb.appendWhere("1=1");
    if (appName != null) {
      qb.appendWhere(" AND " + KEY_APPNAME + " = ");
      qb.appendWhereEscapeString(appName);
    }
    if (pkgName != null) {
      qb.appendWhere(" AND " + KEY_PKGNAME + " = ");
      qb.appendWhereEscapeString(pkgName);
    }
    if (enabled != null) {
      qb.appendWhere(" AND " + KEY_ENABLED + " = " + (enabled ? 1 : 0));
    }
    // Not using additional selections, selectionArgs, groupBy, having, orderBy, set them to null.
    return qb.query(database, KEYS, null, null, null, null, null);
  }

  /**
   * Update a RegisteredApp record with specific parameters.
   * 
   * @param appID
   *          is the id of the record to be updated.
   * @param appName
   *          is the application name or null if not updating it.
   * @param pkgName
   *          is the package name or null if not updating it.
   * @param enabled
   *          is whether the application is activated or null if not updating it.
   * @return true if success, or false otherwise.
   * @throws IllegalArgumentException
   *           if appID is null
   */
  public boolean update(Long appID, String appName, String pkgName, Boolean enabled) {
    if (appID == null) {
      throw new IllegalArgumentException("primary key null.");
    }
    ContentValues args = new ContentValues();
    if (appName != null) {
      args.put(KEY_APPNAME, appName);
    }
    if (pkgName != null) {
      args.put(KEY_PKGNAME, pkgName);
    }
    if (enabled != null) {
      args.put(KEY_ENABLED, enabled);
    }

    if (args.size() > 0) {
      // Set whereArg to null here
      return database.update(DATABASE_TABLE, args, KEY_APPID + "=" + appID, null) > 0;
    }
    return false;
  }

}
