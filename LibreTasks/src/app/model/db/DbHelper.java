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

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import libretasks.app.controller.util.IOUtil;

/**
 * This class extends SQLiteOpenHelper to handle creating/open/close database, creating/deleting
 * tables and migrations.
 */
public class DbHelper extends SQLiteOpenHelper  {
  public static class AppName {
    public static final String SMS = "SMS";
    public static final String PHONE = "Phone";
    public static final String GPS = "GPS";
    public static final String MEDIA = "Media";
    public static final String GMAIL = "GMAIL";
    public static final String TWITTER = "Twitter";
  }

  private static final String TAG = DbHelper.class.getName();

  // This version number needs to increase whenever a data schema change is made
  private static final int DATABASE_VERSION = 19;


  private static final String DATABASE_NAME = "omnidroid";
  private static final String DATABASE_NAME_BACKUP = "omnidroid_backup";
  private static final String DATABASE_FOLDER = "/databases/";
  private static final String PKG_ROOT = "/data/data/";

  private Context context;

  public DbHelper(Context context) {
    // Set the CursorFactory to null since we don't use it.
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.context = context;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // If the first install, upgrade starting from DB version 1
    DbMigration.migrateToLatest(context, db, 1);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // If upgrading, upgrade starting from the last version of the DB
    DbMigration.migrateToLatest(context, db, oldVersion);
  }

  /**
   * Cleanup the DB, including user defined rules
   * 
   * @param db
   *          SQLiteDatabase object to work with
   */
  public void cleanup(SQLiteDatabase db) {
    // Log using standard log since the DB may not be setup yet
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
    db.execSQL(LogEventDbAdapter.DATABASE_DROP);
    db.execSQL(LogActionDbAdapter.DATABASE_DROP);
    db.execSQL(LogGeneralDbAdapter.DATABASE_DROP);
    db.execSQL(FailedActionsDbAdapter.DATABASE_DROP);
    db.execSQL(FailedActionParameterDbAdapter.DATABASE_DROP);
  }

  /**
   * Back up the database by backing up the sqlite file.
   */
  public void backup() {
    // Log using standard log since the DB may not be setup yet
    Log.w(TAG, "Backing up" + DATABASE_NAME);
    IOUtil.copy(databaseDir() + DATABASE_NAME, databaseDir() + DATABASE_NAME_BACKUP);
  }

  /**
   * Removing the current database file
   */
  public void remove() {
    // Log using standard log since the DB may not be setup yet
    Log.w(TAG, "Removing" + DATABASE_NAME);
    IOUtil.remove(databaseDir() + DATABASE_NAME);
  }

  /**
   * Restore the database by using the sqlite file backed up.
   */
  public void restore() {
    // Log using standard log since the DB may not be setup yet
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
    return PreferenceManager.getDefaultSharedPreferences(context);
  }
}
