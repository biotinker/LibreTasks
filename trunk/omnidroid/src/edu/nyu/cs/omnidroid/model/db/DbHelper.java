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
  
  // This version number needs to increase whenever a data schema change is made
  private static final int DATABASE_VERSION = 3;
  
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
    
    // Populate data types
    DataTypeDbAdapter dataTypeDbAdapter = new DataTypeDbAdapter(db);
    long dataTypeIdText = dataTypeDbAdapter.insert("Text", "OmniText");
    long dataTypeIdDate = dataTypeDbAdapter.insert("Date", "OmniDate");
    long dataTypeIdPhone = dataTypeDbAdapter.insert("PhoneNumber", "OmniPhoneNumber");
    long dataTypeIdArea = dataTypeDbAdapter.insert("Area", "OmniArea");
    long dataTypeIdDayOfWeek = dataTypeDbAdapter.insert("DayOfWeek", "OmniDayOfWeek");
    
    // Populate data filters
    DataFilterDbAdapter dataFilterDbAdapter = new DataFilterDbAdapter(db);
    dataFilterDbAdapter.insert("equals", dataTypeIdText, dataTypeIdText);
    dataFilterDbAdapter.insert("contains", dataTypeIdText, dataTypeIdText);
    
    dataFilterDbAdapter.insert("equals", dataTypeIdPhone, dataTypeIdPhone);
    
    dataFilterDbAdapter.insert("before", dataTypeIdDate, dataTypeIdDate);
    dataFilterDbAdapter.insert("after", dataTypeIdDate, dataTypeIdDate);
    
    dataFilterDbAdapter.insert("near", dataTypeIdArea, dataTypeIdArea);
    dataFilterDbAdapter.insert("away", dataTypeIdArea, dataTypeIdArea);
    
    dataFilterDbAdapter.insert("isDayOfWeek", dataTypeIdDate, dataTypeIdDayOfWeek);
    
    // Populate registered apps
    RegisteredAppDbAdapter registeredAppDbAdapter = new RegisteredAppDbAdapter(db);
    long appIdSms = registeredAppDbAdapter.insert("SMS", "", true);
    long appIdDial = registeredAppDbAdapter.insert("DIAL", "", true);
    
    // Populate registered events
    RegisteredEventDbAdapter registeredEventDbAdapter = new RegisteredEventDbAdapter(db);
    long eventIdSmsRec = registeredEventDbAdapter.insert("SMS Received", appIdSms);
    
    // Populate registered actions
    RegisteredActionDbAdapter registeredActionDbAdapter = new RegisteredActionDbAdapter(db);
    long actionIdSmsSend = registeredActionDbAdapter.insert("SMS Send", appIdSms);
    long actionIdPhoneCall = registeredActionDbAdapter.insert("Phone Call", appIdDial);
    
    // Populate registered event attributes
    RegisteredEventAttributeDbAdapter registeredEventAttributeDbAdapter = 
        new RegisteredEventAttributeDbAdapter(db);
    registeredEventAttributeDbAdapter.insert("SMS Phonenumber", eventIdSmsRec, dataTypeIdPhone);
    registeredEventAttributeDbAdapter.insert("SMS Text", eventIdSmsRec, dataTypeIdText);
    
    // Populate registered action parameters
    RegisteredActionParameterDbAdapter registeredActionParameterDbAdapter = 
        new RegisteredActionParameterDbAdapter(db);
    registeredActionParameterDbAdapter.insert("Phone Number", actionIdSmsSend, dataTypeIdPhone);
    registeredActionParameterDbAdapter.insert("Text Message", actionIdSmsSend, dataTypeIdText);
    registeredActionParameterDbAdapter.insert("Phone Number", actionIdPhoneCall, dataTypeIdPhone);
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
