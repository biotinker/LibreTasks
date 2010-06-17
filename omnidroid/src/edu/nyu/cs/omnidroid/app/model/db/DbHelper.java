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
package edu.nyu.cs.omnidroid.app.model.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import edu.nyu.cs.omnidroid.app.controller.util.IOUtil;
import edu.nyu.cs.omnidroid.app.model.DbMigration;

/**
 * This class extends SQLiteOpenHelper to handle creating/open/close database, creating/deleting
 * tables and migrations.
 */
public class DbHelper extends SQLiteOpenHelper {
  public static class AppName {
    public static final String SMS = "SMS";
    public static final String PHONE = "Phone";
    public static final String GPS = "GPS";
    public static final String GMAIL = "GMAIL";
    public static final String TWITTER = "Twitter";
  }

  private static final String TAG = DbHelper.class.getName();
  
  // This version number needs to increase whenever a data schema change is made
  private static final int DATABASE_VERSION = 6;
  
  private static final String DATABASE_NAME = "omnidroid";
  private static final String DATABASE_NAME_BACKUP = "omnidroid_backup";
  private static final String DATABASE_FOLDER = "/databases/";
  private static final String PKG_ROOT = "/data/data/";

  private Context context;

  // Store simple user preferences in a SharedPreferences file and their associated keys.
  private static final String SHARED_PREFS = "OmnidroidSharedPrefs";
  public static final String SETTING_ACCEPTED_DISCLAIMER = "SettingDisclaimerAccepted";

  public DbHelper(Context context) {
    // Set the CursorFactory to null since we don't use it.
    super(context, DATABASE_NAME, null, DATABASE_VERSION);

    this.context = context;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    DbMigration.migrateToLatest(db, 1);
  }

  // TODO(Fang Huang) Consider the Log Level, Warn, in this class. Maybe need to change to Info,
  // Verbose or others?
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    DbMigration.migrateToLatest(db, oldVersion);
  }
  
  /**
   * Cleanup the DB, including user defined rules
   * 
   * @param db
   *          SQLiteDatabase object to work with
   */
  public void cleanup(SQLiteDatabase db) {
    Log.w(TAG, "Resetting database");
    dropTables(db);
    onCreate(db);
  }

  /**
   * Drop all table in the database
   * 
   * @param db
   *          SQLiteDatabase object to work with
   */
  private void dropTables(SQLiteDatabase db) {
    db.execSQL(RegisteredAppDbAdapter.DATABASE_DROP);
    db.execSQL(RegisteredEventDbAdapter.DATABASE_DROP);
    db.execSQL(RegisteredEventAttributeDbAdapter.DATABASE_DROP);
    db.execSQL(RegisteredActionDbAdapter.DATABASE_DROP);
    db.execSQL(RegisteredActionParameterDbAdapter.DATABASE_DROP);
    db.execSQL(DataFilterDbAdapter.DATABASE_DROP);
    db.execSQL(DataTypeDbAdapter.DATABASE_DROP);
    db.execSQL(ExternalAttributeDbAdapter.DATABASE_DROP);
    db.execSQL(RuleDbAdapter.DATABASE_DROP);
    db.execSQL(RuleFilterDbAdapter.DATABASE_DROP);
    db.execSQL(RuleActionDbAdapter.DATABASE_DROP);
    db.execSQL(RuleActionParameterDbAdapter.DATABASE_DROP);
  }

  /**
   * Back up the database by backing up the sqlite file.
   */
  public void backup() {
    Log.w(TAG, "Backing up" + DATABASE_NAME);
    IOUtil.copy(databaseDir() + DATABASE_NAME, databaseDir() + DATABASE_NAME_BACKUP);
  }
  
  /**
   * Removing the current database file
   */
  public void remove() {
    Log.w(TAG, "Removing" + DATABASE_NAME);
    IOUtil.remove(databaseDir() + DATABASE_NAME);
  }

  /**
   * Restore the database by using the sqlite file backed up.
   */
  public void restore() {
    Log.w(TAG, "Restoring " + DATABASE_NAME);
    remove();
    IOUtil.move(databaseDir() + DATABASE_NAME_BACKUP, databaseDir() + DATABASE_NAME);
  }

  /**
   * @return whether the database backup file exists.
   */
  public boolean isBackedUp() {
    return IOUtil.exist(databaseDir() + DATABASE_NAME_BACKUP);
  }

  /**
   * @return the directory of the database file.
   */
  private String databaseDir() {
    return PKG_ROOT + context.getPackageName() + DATABASE_FOLDER;
  }

  /**
   * @return the sharedPreferences to allow for get/setting of user preferences. 
   */
  public SharedPreferences getSharedPreferences() {
    return context.getSharedPreferences(SHARED_PREFS, 0);
  }
}
