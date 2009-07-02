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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class extends SQLiteOpenHelper to handle creating/open/close database, creating/deleting
 * tables and migrations.
 */
public class DbHelper extends SQLiteOpenHelper {

  private static final String TAG = DbHelper.class.getName();
  private static final int DATABASE_VERSION = 2;
  private static final String DATABASE_NAME = "omnidroid";

  DbHelper(Context context) {
    // Set the CursorFactory to null since we don't use it.
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {

    // Create all tables when new database instance is created.
    createTables(db);

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
        + ", which will destroy all old data");

    // TODO(ehotou) Future migration codes here.

    // Default migration operations, drop all tables and recreate databases
    dropTables(db);
    onCreate(db);
  }

  /**
   * Create all necessary tables in the database
   * 
   * @param db
   *          SQLiteDatabase object to deal with
   */
  private void createTables(SQLiteDatabase db) {
    db.execSQL(RegisteredAppDbAdapter.DATABASE_CREATE);

    db.execSQL(RegisteredEventDbAdapter.DATABASE_CREATE);
    db.execSQL(RegisteredEventAttributeDbAdapter.DATABASE_CREATE);

    db.execSQL(RegisteredActionDbAdapter.DATABASE_CREATE);
    db.execSQL(RegisteredActionParameterDbAdapter.DATABASE_CREATE);

    db.execSQL(DataFilterDbAdapter.DATABASE_CREATE);
    db.execSQL(DataTypeDbAdapter.DATABASE_CREATE);

    db.execSQL(ExternalAttributeDbAdapter.DATABASE_CREATE);

    db.execSQL(RuleDbAdapter.DATABASE_CREATE);
    db.execSQL(RuleFilterDbAdapter.DATABASE_CREATE);
    db.execSQL(RuleActionDbAdapter.DATABASE_CREATE);
    db.execSQL(RuleActionParameterDbAdapter.DATABASE_CREATE);
  }

  /**
   * Drop all table in the database
   * 
   * @param db
   *          SQLiteDatabase object to deal with
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

  // TODO(ehotou) Database backup and restore methods here.

}
