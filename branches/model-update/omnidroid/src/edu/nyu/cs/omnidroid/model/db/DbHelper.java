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
import edu.nyu.cs.omnidroid.util.IOUtil;

/**
 * This class extends SQLiteOpenHelper to handle creating/open/close database, creating/deleting
 * tables and migrations.
 */
public class DbHelper extends SQLiteOpenHelper {

  private static final String TAG = DbHelper.class.getName();
  private static final int DATABASE_VERSION = 2;
  private static final String DATABASE_NAME = "omnidroid";
  private static final String DATABASE_NAME_BACKUP = "omnidroid_backup";
  private static final String DATABASE_FOLDER = "/databases/";
  private static final String PKG_ROOT = "/data/data/";

  private Context context;

  DbHelper(Context context) {
    // Set the CursorFactory to null since we don't use it.
    super(context, DATABASE_NAME, null, DATABASE_VERSION);

    this.context = context;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {

    // Create all tables when new database instance is created.
    createTables(db);
    
    // Pre-populate data after installation.
    populateDefaultData(db);
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
   *          SQLiteDatabase object to work with
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
   * Pre-populate data into the database
   * 
   * @param db
   *          SQLiteDatabase object to work with
   */
  private void populateDefaultData(SQLiteDatabase db) {
    
    // TODO(ehotou) The exact data to be populated is under discussion.
    
    // Populate data types
    DataTypeDbAdapter dataTypeDbAdapter = new DataTypeDbAdapter(db);
    long dataType_id_text = dataTypeDbAdapter.insert("Text", "OmniText");
    long dataType_id_date = dataTypeDbAdapter.insert("Date", "OmniDate");
    long dataType_id_phone = dataTypeDbAdapter.insert("PhoneNumber", "OmniPhoneNumber");
    
    // Populate data filters
    DataFilterDbAdapter dataFilterDbAdapter = new DataFilterDbAdapter(db);
    dataFilterDbAdapter.insert("equals", dataType_id_text);
    dataFilterDbAdapter.insert("contains", dataType_id_text);
    dataFilterDbAdapter.insert("equals", dataType_id_phone);
    dataFilterDbAdapter.insert("before", dataType_id_date);
    dataFilterDbAdapter.insert("after", dataType_id_date);
    
    // Populate registered apps
    RegisteredAppDbAdapter registeredAppDbAdapter = new RegisteredAppDbAdapter(db);
    long app_id_sms = registeredAppDbAdapter.insert("SMS", "", true);
    long app_id_dial = registeredAppDbAdapter.insert("DIAL", "", true);
    
    // Populate registered events
    RegisteredEventDbAdapter registeredEventDbAdapter = new RegisteredEventDbAdapter(db);
    long event_id_sms_rec = registeredEventDbAdapter.insert("SMS Received", app_id_sms);
    long event_id_phone_rec = registeredEventDbAdapter.insert("Phone Call Received", app_id_dial);
    
    // Populate registered actions
    RegisteredActionDbAdapter registeredActionDbAdapter = new RegisteredActionDbAdapter(db);
    registeredActionDbAdapter.insert("SMS Send", app_id_sms);
    
    // Populate registered event attributes
    RegisteredEventAttributeDbAdapter registeredEventAttributeDbAdapter = 
        new RegisteredEventAttributeDbAdapter(db);
    registeredEventAttributeDbAdapter.insert(
        "SMS Phonenumber", event_id_sms_rec, dataType_id_phone);
    registeredEventAttributeDbAdapter.insert(
        "SMS Text", event_id_sms_rec, dataType_id_phone);
    registeredEventAttributeDbAdapter.insert(
        "Phonenumber", event_id_phone_rec, dataType_id_phone);
  }

  /**
   * Back up the database by backing up the sqlite file.
   */
  public void backup() {
    Log.w(TAG, "Backing up" + DATABASE_NAME);
    IOUtil.copy(databaseDir() + DATABASE_NAME, databaseDir() + DATABASE_NAME_BACKUP);
  }

  /**
   * Restore the database by using the sqlite file backed up.
   */
  public void restore() {
    Log.w(TAG, "Restoring " + DATABASE_NAME);
    IOUtil.remove(databaseDir() + DATABASE_NAME);
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

}
