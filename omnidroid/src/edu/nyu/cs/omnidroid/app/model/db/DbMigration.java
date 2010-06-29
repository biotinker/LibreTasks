/*******************************************************************************
 * Copyright 2009, 2010 OmniDroid - http://code.google.com/p/omnidroid
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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import edu.nyu.cs.omnidroid.app.controller.actions.CallPhoneAction;
import edu.nyu.cs.omnidroid.app.controller.actions.OmniAction;
import edu.nyu.cs.omnidroid.app.controller.actions.SendGmailAction;
import edu.nyu.cs.omnidroid.app.controller.actions.SendSmsAction;
import edu.nyu.cs.omnidroid.app.controller.actions.SetPhoneLoudAction;
import edu.nyu.cs.omnidroid.app.controller.actions.SetPhoneSilentAction;
import edu.nyu.cs.omnidroid.app.controller.actions.SetPhoneVibrateAction;
import edu.nyu.cs.omnidroid.app.controller.actions.SetScreenBrightnessAction;
import edu.nyu.cs.omnidroid.app.controller.actions.ShowAlertAction;
import edu.nyu.cs.omnidroid.app.controller.actions.ShowNotificationAction;
import edu.nyu.cs.omnidroid.app.controller.actions.ShowWebsiteAction;
import edu.nyu.cs.omnidroid.app.controller.actions.UpdateTwitterStatusAction;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniArea;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDayOfWeek;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniPasswordInput;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniPhoneNumber;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniText;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniTimePeriod;
import edu.nyu.cs.omnidroid.app.controller.events.LocationChangedEvent;
import edu.nyu.cs.omnidroid.app.controller.events.PhoneRingingEvent;
import edu.nyu.cs.omnidroid.app.controller.events.CallEndedEvent;
import edu.nyu.cs.omnidroid.app.controller.events.SMSReceivedEvent;
import edu.nyu.cs.omnidroid.app.controller.events.SystemEvent;
import edu.nyu.cs.omnidroid.app.controller.events.TimeTickEvent;
import edu.nyu.cs.omnidroid.app.model.CursorHelper;

/**
 * Class used for migrating a database for Omnidroid from one version to another.
 */
public class DbMigration {
  private static final String TAG = DbMigration.class.getSimpleName();

  /**
   * This class does not need to be instantiated.
   */
  private DbMigration() {
  }

  /**
   * Migrate the database to its latest version
   * 
   * @param db
   *          the database to migrate
   * @param currentDbVersionNumber
   *          the version number of the current database before migrating
   */
  public static void migrateToLatest(SQLiteDatabase db, int currentDbVersionNumber) {
    Log.i(TAG, "Migrating database from version " + currentDbVersionNumber);
    switch (currentDbVersionNumber) {
    case 1:
    case 2:
    case 3:
    case 4:
      initialVersion(db);
    case 5:
      addCallEndEvent(db);
    case 6:
      addLogEvent(db);
    case 7:
      dropLogEvent(db);
      addLogEvent(db);
      dropLogAction(db);
      addLogAction(db);
      addLogGeneral(db);

      /*
       * Insert new versions before this line and do not forget to update {@code
       * DbHelper.DATABASE_VERSION}. Otherwise, the constructor call on SQLiteOpenHelper will not
       * trigger the {@code onUpgrade} callback method.
       */
      break;
    default:
      Log.w(TAG, "Attempting to migrate from an unknown version!");
      break;
    }
  }

  /**
   * Create the initial version of the Omnidroid database along with prepopulated data.
   * 
   * @param db
   *          SQLiteDatabase object to work with
   */
  private static void initialVersion(SQLiteDatabase db) {
    /**
     * Create tables
     */
    db.execSQL(RegisteredAppDbAdapter.getSqliteCreateStatement());
    db.execSQL(RegisteredEventDbAdapter.getSqliteCreateStatement());
    db.execSQL(RegisteredEventAttributeDbAdapter.getSqliteCreateStatement());
    db.execSQL(RegisteredActionDbAdapter.getSqliteCreateStatement());
    db.execSQL(RegisteredActionParameterDbAdapter.getSqliteCreateStatement());
    db.execSQL(DataFilterDbAdapter.getSqliteCreateStatement());
    db.execSQL(DataTypeDbAdapter.getSqliteCreateStatement());
    db.execSQL(ExternalAttributeDbAdapter.getSqliteCreateStatement());
    db.execSQL(RuleDbAdapter.getSqliteCreateStatement());
    db.execSQL(RuleFilterDbAdapter.getSqliteCreateStatement());
    db.execSQL(RuleActionDbAdapter.getSqliteCreateStatement());
    db.execSQL(RuleActionParameterDbAdapter.getSqliteCreateStatement());

    /*
     * Populate data types and their data filters
     */
    DataTypeDbAdapter dataTypeDbAdapter = new DataTypeDbAdapter(db);
    DataFilterDbAdapter dataFilterDbAdapter = new DataFilterDbAdapter(db);

    long dataTypeIdText = dataTypeDbAdapter.insert(OmniText.DB_NAME, OmniText.class.getName());
    dataFilterDbAdapter.insert(OmniText.Filter.EQUALS.toString(),
        OmniText.Filter.EQUALS.displayName, dataTypeIdText, dataTypeIdText);
    dataFilterDbAdapter.insert(OmniText.Filter.CONTAINS.toString(),
        OmniText.Filter.CONTAINS.displayName, dataTypeIdText, dataTypeIdText);

    long dataTypeIdPhoneNumber = dataTypeDbAdapter.insert(OmniPhoneNumber.DB_NAME,
        OmniPhoneNumber.class.getName());
    dataFilterDbAdapter.insert(OmniPhoneNumber.Filter.EQUALS.toString(),
        OmniPhoneNumber.Filter.EQUALS.displayName, dataTypeIdPhoneNumber, dataTypeIdPhoneNumber);

    long dataTypeIdDayOfWeek = dataTypeDbAdapter.insert(OmniDayOfWeek.DB_NAME, OmniDayOfWeek.class
        .getName());

    long dataTypeIdTimePeriod = dataTypeDbAdapter.insert(OmniTimePeriod.DB_NAME,
        OmniTimePeriod.class.getName());
    long dataTypeIdDate = dataTypeDbAdapter.insert(OmniDate.DB_NAME, OmniDate.class.getName());

    dataFilterDbAdapter.insert(OmniTimePeriod.Filter.DURING_EVERYDAY.toString(),
        OmniTimePeriod.Filter.DURING_EVERYDAY.displayName, dataTypeIdTimePeriod, dataTypeIdDate);
    dataFilterDbAdapter.insert(OmniTimePeriod.Filter.EXCEPT_EVERYDAY.toString(),
        OmniTimePeriod.Filter.EXCEPT_EVERYDAY.displayName, dataTypeIdTimePeriod, dataTypeIdDate);

    dataFilterDbAdapter.insert(OmniDate.Filter.IS_EVERYDAY.toString(),
        OmniDate.Filter.IS_EVERYDAY.displayName, dataTypeIdDate, dataTypeIdDate);
    dataFilterDbAdapter.insert(OmniDate.Filter.IS_NOT_EVERYDAY.toString(),
        OmniDate.Filter.IS_NOT_EVERYDAY.displayName, dataTypeIdDate, dataTypeIdDate);
    dataFilterDbAdapter.insert(OmniDate.Filter.BEFORE_EVERYDAY.toString(),
        OmniDate.Filter.BEFORE_EVERYDAY.displayName, dataTypeIdDate, dataTypeIdDate);
    dataFilterDbAdapter.insert(OmniDate.Filter.AFTER_EVERYDAY.toString(),
        OmniDate.Filter.AFTER_EVERYDAY.displayName, dataTypeIdDate, dataTypeIdDate);
    dataFilterDbAdapter.insert(OmniDate.Filter.DURING_EVERYDAY.toString(),
        OmniDate.Filter.DURING_EVERYDAY.displayName, dataTypeIdDate, dataTypeIdTimePeriod);
    dataFilterDbAdapter.insert(OmniDate.Filter.EXCEPT_EVERYDAY.toString(),
        OmniDate.Filter.EXCEPT_EVERYDAY.displayName, dataTypeIdDate, dataTypeIdTimePeriod);
    dataFilterDbAdapter.insert(OmniDate.Filter.ISDAYOFWEEK.toString(),
        OmniDate.Filter.ISDAYOFWEEK.displayName, dataTypeIdDate, dataTypeIdDayOfWeek);

    long dataTypeIdArea = dataTypeDbAdapter.insert(OmniArea.DB_NAME, OmniArea.class.getName());
    dataFilterDbAdapter.insert(OmniArea.Filter.NEAR.toString(), OmniArea.Filter.NEAR.displayName,
        dataTypeIdArea, dataTypeIdArea);
    dataFilterDbAdapter.insert(OmniArea.Filter.AWAY.toString(), OmniArea.Filter.AWAY.displayName,
        dataTypeIdArea, dataTypeIdArea);

    long dataTypeIdPasswordInput = dataTypeDbAdapter.insert(OmniPasswordInput.DB_NAME,
        OmniPasswordInput.class.getName());

    /*
     * Populate registered applications
     */
    RegisteredAppDbAdapter appDbAdapter = new RegisteredAppDbAdapter(db);
    long appIdSms = appDbAdapter.insert(DbHelper.AppName.SMS, "", true);
    long appIdPhone = appDbAdapter.insert(DbHelper.AppName.PHONE, "", true);
    long appIdGPS = appDbAdapter.insert(DbHelper.AppName.GPS, "", true);
    long appIdGmail = appDbAdapter.insert(DbHelper.AppName.GMAIL, "", true, true);
    long appIdTwitter = appDbAdapter.insert(DbHelper.AppName.TWITTER, "", true, true);
    long appIdOmnidroid = appDbAdapter.insert(OmniAction.APP_NAME, "", true);
    long appIdAndroid = appDbAdapter.insert(SystemEvent.PowerConnectedEvent.APPLICATION_NAME, "",
        true);

    /*
     * Populate registered events and event attributes
     */
    RegisteredEventDbAdapter eventDbAdapter = new RegisteredEventDbAdapter(db);
    RegisteredEventAttributeDbAdapter eventAttributeDbAdapter = new RegisteredEventAttributeDbAdapter(
        db);

    for (SystemEvent e : SystemEvent.values()) {
      eventDbAdapter.insert(e.EVENT_NAME, appIdAndroid);
    }

    long eventIdSmsRec = eventDbAdapter.insert(SMSReceivedEvent.EVENT_NAME, appIdSms);
    eventAttributeDbAdapter.insert(SMSReceivedEvent.ATTRIB_PHONE_NO, eventIdSmsRec,
        dataTypeIdPhoneNumber);
    eventAttributeDbAdapter.insert(SMSReceivedEvent.ATTRIB_MESSAGE_TEXT, eventIdSmsRec,
        dataTypeIdText);
    eventAttributeDbAdapter.insert(SMSReceivedEvent.ATTRIB_MESSAGE_TIME, eventIdSmsRec,
        dataTypeIdDate);

    long eventIdPhoneRings = eventDbAdapter.insert(PhoneRingingEvent.EVENT_NAME, appIdPhone);
    eventAttributeDbAdapter.insert(PhoneRingingEvent.ATTRIBUTE_PHONE_NUMBER, eventIdPhoneRings,
        dataTypeIdPhoneNumber);
    eventAttributeDbAdapter.insert(PhoneRingingEvent.ATTRIBUTE_TIMESTAMP, eventIdPhoneRings,
        dataTypeIdDate);

    long eventIdGPSLocationChanged = eventDbAdapter.insert(LocationChangedEvent.EVENT_NAME,
        appIdGPS);
    eventAttributeDbAdapter.insert(LocationChangedEvent.ATTRIBUTE_CURRENT_LOCATION,
        eventIdGPSLocationChanged, dataTypeIdArea);

    long eventIdTimeTick = eventDbAdapter.insert(TimeTickEvent.EVENT_NAME, appIdAndroid);
    eventAttributeDbAdapter.insert(TimeTickEvent.ATTRIBUTE_CURRENT_TIME, eventIdTimeTick,
        dataTypeIdDate);

    /*
     * Populate registered actions and action parameters
     */
    RegisteredActionDbAdapter actionDbAdapter = new RegisteredActionDbAdapter(db);
    RegisteredActionParameterDbAdapter actionParameterDbAdapter = new 
        RegisteredActionParameterDbAdapter(db);

    long actionIdDisplayMessage = actionDbAdapter.insert(ShowAlertAction.ACTION_NAME,
        appIdOmnidroid);
    actionParameterDbAdapter.insert(ShowAlertAction.PARAM_ALERT_MESSAGE, actionIdDisplayMessage,
        dataTypeIdText);
    long actionIdNotifyMessage = actionDbAdapter.insert(ShowNotificationAction.ACTION_NAME,
        appIdOmnidroid);
    actionParameterDbAdapter.insert(ShowNotificationAction.PARAM_ALERT_MESSAGE,
        actionIdNotifyMessage, dataTypeIdText);
    long actionIdShowWebsite = actionDbAdapter
        .insert(ShowWebsiteAction.ACTION_NAME, appIdOmnidroid);
    actionParameterDbAdapter.insert(ShowWebsiteAction.PARAM_WEB_URL, actionIdShowWebsite,
        dataTypeIdText);
    long actionIdSetBrightness = actionDbAdapter.insert(SetScreenBrightnessAction.ACTION_NAME,
        appIdOmnidroid);
    actionParameterDbAdapter.insert(SetScreenBrightnessAction.PARAM_BRIGHTNESS,
        actionIdSetBrightness, dataTypeIdText);
    actionDbAdapter.insert(SetPhoneLoudAction.ACTION_NAME, appIdOmnidroid);
    actionDbAdapter.insert(SetPhoneSilentAction.ACTION_NAME, appIdOmnidroid);
    actionDbAdapter.insert(SetPhoneVibrateAction.ACTION_NAME, appIdOmnidroid);

    long actionIdSmsSend = actionDbAdapter.insert(SendSmsAction.ACTION_NAME, appIdSms);
    actionParameterDbAdapter.insert(SendSmsAction.PARAM_PHONE_NO, actionIdSmsSend,
        dataTypeIdPhoneNumber);
    actionParameterDbAdapter.insert(SendSmsAction.PARAM_SMS, actionIdSmsSend, dataTypeIdText);

    long actionIdPhoneCall = actionDbAdapter.insert(CallPhoneAction.ACTION_NAME, appIdPhone);
    actionParameterDbAdapter.insert(CallPhoneAction.PARAM_PHONE_NO, actionIdPhoneCall,
        dataTypeIdPhoneNumber);

    long actionIdGmailSend = actionDbAdapter.insert(SendGmailAction.ACTION_NAME, appIdGmail);
    actionParameterDbAdapter.insert(SendGmailAction.PARAM_USERNAME, actionIdGmailSend,
        dataTypeIdText);
    actionParameterDbAdapter.insert(SendGmailAction.PARAM_PASSWORD, actionIdGmailSend,
        dataTypeIdPasswordInput);
    actionParameterDbAdapter.insert(SendGmailAction.PARAM_TO, actionIdGmailSend, dataTypeIdText);
    actionParameterDbAdapter.insert(SendGmailAction.PARAM_SUBJECT, actionIdGmailSend,
        dataTypeIdText);
    actionParameterDbAdapter.insert(SendGmailAction.PARAM_BODY, actionIdGmailSend, dataTypeIdText);

    long actionIdTwitterUpdate = actionDbAdapter.insert(UpdateTwitterStatusAction.ACTION_NAME,
        appIdTwitter);
    actionParameterDbAdapter.insert(UpdateTwitterStatusAction.PARAM_USERNAME,
        actionIdTwitterUpdate, dataTypeIdText);
    actionParameterDbAdapter.insert(UpdateTwitterStatusAction.PARAM_PASSWORD,
        actionIdTwitterUpdate, dataTypeIdPasswordInput);
    actionParameterDbAdapter.insert(UpdateTwitterStatusAction.PARAM_MESSAGE, actionIdTwitterUpdate,
        dataTypeIdText);
  }

  /**
   * Update the database to support the call ended event.
   * 
   * @param db
   *          the database instance to work with
   */
  private static void addCallEndEvent(SQLiteDatabase db) {
    RegisteredEventDbAdapter eventDbAdapter = new RegisteredEventDbAdapter(db);
    RegisteredEventAttributeDbAdapter eventAttributeDbAdapter = new 
        RegisteredEventAttributeDbAdapter(db);

    DataTypeDbAdapter dataTypeDbAdapter = new DataTypeDbAdapter(db);
    Cursor dataTypeDbCursor = dataTypeDbAdapter
        .fetchAll(OmniDate.DB_NAME, OmniDate.class.getName());

    /**
     * Just get the first result. The identifiers used in the query should already be unique enough
     * that it should just return one record if it existed.
     */
    dataTypeDbCursor.moveToFirst();
    long dataTypeIdDate = CursorHelper.getLongFromCursor(dataTypeDbCursor,
        DataTypeDbAdapter.KEY_DATATYPEID);

    long eventIdPhoneCallEnded = eventDbAdapter.insert(CallEndedEvent.EVENT_NAME,
        DbHelper.AppName.PHONE, "");
    eventAttributeDbAdapter.insert(CallEndedEvent.ATTRIBUTE_TIMESTAMP, eventIdPhoneCallEnded,
        dataTypeIdDate);

    dataTypeDbCursor.close();
  }

  /**
   * Add table to provide {@code LogEvent} support.
   * 
   * @param db
   *          the database instance to work with
   */
  private static void addLogEvent(SQLiteDatabase db) {
    // Create table
    db.execSQL(LogEventDbAdapter.DATABASE_CREATE);
  }

  /**
   * Drop table that provides {@code LogAction} support.
   * 
   * @param db
   *          the database instance to work with
   */
  private static void dropLogEvent(SQLiteDatabase db) {
    // Drop table
    db.execSQL(LogEventDbAdapter.DATABASE_DROP);
  }

  /**
   * Add table to provide {@code LogAction} support.
   * 
   * @param db
   *          the database instance to work with
   */
  private static void addLogAction(SQLiteDatabase db) {
    // Create table
    db.execSQL(LogActionDbAdapter.DATABASE_CREATE);
  }

  /**
   * Drop table that provides {@code LogAction} support.
   * 
   * @param db
   *          the database instance to work with
   */
  private static void dropLogAction(SQLiteDatabase db) {
    // Drop table
    db.execSQL(LogActionDbAdapter.DATABASE_DROP);
  }

  /**
   * Add table to provide {@code LogGeneral} support.
   * 
   * @param db
   *          the database instance to work with
   */
  private static void addLogGeneral(SQLiteDatabase db) {
    // Create table
    db.execSQL(LogGeneralDbAdapter.DATABASE_CREATE);
  }
}