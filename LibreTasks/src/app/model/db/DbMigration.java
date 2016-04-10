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

import static libretasks.app.model.CursorHelper.getLongFromCursor;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import libretasks.app.R;
import libretasks.app.controller.Event;
import libretasks.app.controller.actions.CallPhoneAction;
import libretasks.app.controller.actions.OmniAction;
import libretasks.app.controller.actions.SendGmailAction;
import libretasks.app.controller.actions.SendSmsAction;
import libretasks.app.controller.actions.PauseMediaAction;
import libretasks.app.controller.actions.PlayMediaAction;
import libretasks.app.controller.actions.SetPhoneLoudAction;
import libretasks.app.controller.actions.SetPhoneSilentAction;
import libretasks.app.controller.actions.SetPhoneVibrateAction;
import libretasks.app.controller.actions.SetScreenBrightnessAction;
import libretasks.app.controller.actions.ShowAlertAction;
import libretasks.app.controller.actions.ShowNotificationAction;
import libretasks.app.controller.actions.ShowWebsiteAction;
import libretasks.app.controller.actions.TurnOffWifiAction;
import libretasks.app.controller.actions.TurnOnWifiAction;
import libretasks.app.controller.actions.UpdateTwitterStatusAction;
import libretasks.app.controller.datatypes.OmniArea;
import libretasks.app.controller.datatypes.OmniDate;
import libretasks.app.controller.datatypes.OmniDayOfWeek;
import libretasks.app.controller.datatypes.OmniPasswordInput;
import libretasks.app.controller.datatypes.OmniPhoneNumber;
import libretasks.app.controller.datatypes.OmniText;
import libretasks.app.controller.datatypes.OmniTimePeriod;
import libretasks.app.controller.datatypes.OmniUserAccount;
import libretasks.app.controller.events.LocationChangedEvent;
import libretasks.app.controller.events.InternetAvailableEvent;
import libretasks.app.controller.events.MissedCallEvent;
import libretasks.app.controller.events.PhoneCallEvent;
import libretasks.app.controller.events.PhoneRingingEvent;
import libretasks.app.controller.events.CallEndedEvent;
import libretasks.app.controller.events.SMSReceivedEvent;
import libretasks.app.controller.events.ServiceAvailableEvent;
import libretasks.app.controller.events.SystemEvent;
import libretasks.app.controller.events.TimeTickEvent;
import libretasks.app.model.CursorHelper;

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
   * @param context
   *          the context from DbHelper
   * 
   * @param db
   *          the database to migrate
   * @param currentDbVersionNumber
   *          the version number of the current database before migrating
   */
  public static void migrateToLatest(Context context, SQLiteDatabase db, int currentDbVersionNumber) {
    // Use standard Logger since DB may not be setup yet
    Log.i(TAG, "Migrating database from version " + currentDbVersionNumber);
    switch (currentDbVersionNumber) {
    case 1:
    case 2:
    case 3:
    case 4:
      initialVersion(db);
    case 5:
      updateDataTypeClassNameChanges(db);
      addCallEndEvent(db);
    case 6:
      addLogEvent(db);
    case 7:
      dropLogEvent(db);
      addLogEvent(db);
      dropLogAction(db);
      addLogAction(db);
      addLogGeneral(db);
    case 8:
      modifyGmailAndTwitterParam(db);
    case 9:
      addWifiActions(db);
    case 10:
      addNotification(db);
    case 11:
      addPhoneNumberNotEqualsFilter(db);
    case 12:
      addGeneralLogLevels(db);
    case 13:
      addFailedActions(db);
      addInternetAndServiceAvailableEvents(db);
    case 14:
      setDefaultRules(context, db);
    case 15:
      addSupportForGlobalEventAttributes(db);
    case 16:
      alterFailedActionsTable(db);
    case 17:
      addMissedCallEvent(db);


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
   * When name restructuring was done in post r725 (Version 3 (0.1.2)) code, we reorganized the
   * source tree. DataTypes were moved to a new hierarchy resulting in their class names changing.
   * This code will update the class names stored in the database to reflect the new class names.
   * 
   * @param db
   *          the database
   */
  private static void updateDataTypeClassNameChanges(SQLiteDatabase db) {
    /**
     * If upgrading from a r725 or earlier release, these are stored in "DataTypes" table:
     * 
     * VALUES(1,'Text','edu.nyu.cs.omnidroid.app.core.datatypes.OmniText')
     * VALUES(2,'PhoneNumber','edu.nyu.cs.omnidroid.app.core.datatypes.OmniPhoneNumber')
     * VALUES(3,'DayOfWeek','edu.nyu.cs.omnidroid.app.core.datatypes.OmniDayOfWeek')
     * VALUES(4,'TimePeriod','edu.nyu.cs.omnidroid.app.core.datatypes.OmniTimePeriod')
     * VALUES(5,'Date','edu.nyu.cs.omnidroid.app.core.datatypes.OmniDate')
     * VALUES(6,'Area','edu.nyu.cs.omnidroid.app.core.datatypes.OmniArea')
     * VALUES(7,'PasswordInput','edu.nyu.cs.omnidroid.app.core.datatypes.OmniPasswordInput')
     * 
     * And need to be updated to the new class names.
     */
    DataTypeDbAdapter dataTypeDbAdapter = new DataTypeDbAdapter(db);
    Cursor cursor;
    long id;

    // Update OmniText
    cursor = dataTypeDbAdapter.fetchAll(OmniText.DB_NAME, null);
    cursor.moveToFirst();
    id = getLongFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPEID);
    dataTypeDbAdapter.update(id, null, OmniText.class.getName());
    cursor.close();
    // Update OmniPhoneNumber
    cursor = dataTypeDbAdapter.fetchAll(OmniPhoneNumber.DB_NAME, null);
    cursor.moveToFirst();
    id = getLongFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPEID);
    dataTypeDbAdapter.update(id, null, OmniPhoneNumber.class.getName());
    cursor.close();
    // Update OmniDayOfWeek
    cursor = dataTypeDbAdapter.fetchAll(OmniDayOfWeek.DB_NAME, null);
    cursor.moveToFirst();
    id = getLongFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPEID);
    dataTypeDbAdapter.update(id, null, OmniDayOfWeek.class.getName());
    cursor.close();
    // Update OmniTimePeriod
    cursor = dataTypeDbAdapter.fetchAll(OmniTimePeriod.DB_NAME, null);
    cursor.moveToFirst();
    id = getLongFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPEID);
    dataTypeDbAdapter.update(id, null, OmniTimePeriod.class.getName());
    cursor.close();
    // Update OmniDate
    cursor = dataTypeDbAdapter.fetchAll(OmniDate.DB_NAME, null);
    cursor.moveToFirst();
    id = getLongFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPEID);
    dataTypeDbAdapter.update(id, null, OmniDate.class.getName());
    cursor.close();
    // Update OmniArea
    cursor = dataTypeDbAdapter.fetchAll(OmniArea.DB_NAME, null);
    cursor.moveToFirst();
    id = getLongFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPEID);
    dataTypeDbAdapter.update(id, null, OmniArea.class.getName());
    cursor.close();
    // Update OmniPasswordInput
    cursor = dataTypeDbAdapter.fetchAll(OmniPasswordInput.DB_NAME, null);
    cursor.moveToFirst();
    id = getLongFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPEID);
    dataTypeDbAdapter.update(id, null, OmniPasswordInput.class.getName());
    cursor.close();
  }
 
  /**
   * Set the default rules
   * 
   * @param context
   *          the context
   * 
   * @param db
   *          database
   * 
   */
  private static void setDefaultRules(Context context, SQLiteDatabase db) {
    final String TIME_NIGHT = "0001-01-01 00:00:00";
    final String TIME_MORNING = "0001-01-01 09:00:00";
    RuleDbAdapter ruleAdapter = new RuleDbAdapter(db);
    RegisteredEventDbAdapter eventsAdapter = new RegisteredEventDbAdapter(db);
    RegisteredActionDbAdapter actionAdapter = new RegisteredActionDbAdapter(db);
    RuleActionDbAdapter ruleActionAdapter = new RuleActionDbAdapter(db);
    RegisteredActionParameterDbAdapter actionParametersAdapter = new RegisteredActionParameterDbAdapter(
        db);
    RuleActionParameterDbAdapter ruleActionParametersAdapter = new RuleActionParameterDbAdapter(db);
    RegisteredEventAttributeDbAdapter eventAttributesAdapter = new RegisteredEventAttributeDbAdapter(
        db);
    DataFilterDbAdapter dataFilterAdapter = new DataFilterDbAdapter(db);
    RuleFilterDbAdapter ruleFilterAdapter = new RuleFilterDbAdapter(db);

    // Set default rule: sent back sms "Busy, in meeting. Will get back to you later" when phone
    // is ringing
    Cursor phoneRingEventCursor = eventsAdapter.fetchAll(PhoneRingingEvent.EVENT_NAME, null);
    if (phoneRingEventCursor.moveToNext()) {
      long phoneRingEventID = getLongFromCursor(phoneRingEventCursor,
          RegisteredEventDbAdapter.KEY_EVENTID);
      String phoneRingToSMSRuleName = context.getString(R.string.phoneRingToSMSRuleName);
      String phoneRingToSMSRuleDesc = context.getString(R.string.phoneRingToSMSRuleDesc);
      long phoneToSMSRuleID = ruleAdapter.insert(phoneRingEventID, phoneRingToSMSRuleName,
          phoneRingToSMSRuleDesc, false);
      Cursor smsActionCursor = actionAdapter.fetchAll(SendSmsAction.ACTION_NAME, null);
      if (smsActionCursor.moveToNext()) {
        long smsActionID = getLongFromCursor(smsActionCursor,
            RegisteredActionDbAdapter.KEY_ACTIONID);
        long phoneToSMSRuleActionID = ruleActionAdapter.insert(phoneToSMSRuleID, smsActionID);
        Cursor smsPhoneNOParameterCursor = actionParametersAdapter.fetchAll(
            SendSmsAction.PARAM_PHONE_NO, smsActionID, null);
        if (smsPhoneNOParameterCursor.moveToNext()) {
          long smsPhoneNOParameterID = getLongFromCursor(smsPhoneNOParameterCursor,
              RegisteredActionParameterDbAdapter.KEY_ACTIONPARAMETERID);
          Cursor smsMessageParameterCursor = actionParametersAdapter.fetchAll(
              SendSmsAction.PARAM_SMS, smsActionID, null);
          if (smsMessageParameterCursor.moveToNext()) {
            long smsMessageParameterID = getLongFromCursor(smsMessageParameterCursor,
                RegisteredActionParameterDbAdapter.KEY_ACTIONPARAMETERID);
            String phoneParameter = "<" + PhoneRingingEvent.ATTRIBUTE_PHONE_NUMBER + ">";
            ruleActionParametersAdapter.insert(phoneToSMSRuleActionID, smsPhoneNOParameterID,
                phoneParameter);
            String PhoneRingToSMSRuleMessage = context
                .getString(R.string.phoneRingToSMSRuleMessage);
            ruleActionParametersAdapter.insert(phoneToSMSRuleActionID, smsMessageParameterID,
                PhoneRingToSMSRuleMessage);
          }
          smsMessageParameterCursor.close();
        }
        smsPhoneNOParameterCursor.close();
      }
      smsActionCursor.close();
    }
    phoneRingEventCursor.close();

    // Set default rule: Sleep during night, when time is after midnight set phone silent
    // Set default rule: Wake up during daytime, when time is after 9am set phone loud
    Cursor timeEventCursor = eventsAdapter.fetchAll(TimeTickEvent.EVENT_NAME, null);
    if (timeEventCursor.moveToNext()) {
      long timeEventID = getLongFromCursor(timeEventCursor, RegisteredEventDbAdapter.KEY_EVENTID);
      /*
       * Use the "deprecated" constant since addSupportForGlobalEventAttributes was accidentally
       * deleted causing the Event.ATTRIBUTE_TIME not to be inserted to the table.
       */
      @SuppressWarnings("deprecation")
      Cursor timeAttributesCursor = eventAttributesAdapter.fetchAll(
          TimeTickEvent.ATTRIBUTE_CURRENT_TIME, timeEventID, null);
      if (timeAttributesCursor.moveToNext()) {
        long timeAttrbutesID = getLongFromCursor(timeAttributesCursor,
            RegisteredEventAttributeDbAdapter.KEY_EVENTATTRIBUTEID);
        String timeToSilentRuleTime = TIME_NIGHT;
        String timeToLoudRuleTime = TIME_MORNING;

        // Set rule: Sleep during night
        Cursor timeToSilentFilterCursor = dataFilterAdapter.fetchAll(null,
            OmniDate.Filter.IS_EVERYDAY.displayName, null, null);
        if (timeToSilentFilterCursor.moveToNext()) {
          long timeToSilentFilterID = getLongFromCursor(timeToSilentFilterCursor,
              DataFilterDbAdapter.KEY_DATAFILTERID);
          String timeToSilentRuleName = context.getString(R.string.timeToSilentRuleName);
          String timeToSilentRuleDesc = context.getString(R.string.timeToSilentRuleDesc);
          long timeToSilentRuleID = ruleAdapter.insert(timeEventID, timeToSilentRuleName,
              timeToSilentRuleDesc, false);
          Cursor silentActionCursor = actionAdapter
              .fetchAll(SetPhoneSilentAction.ACTION_NAME, null);
          if (silentActionCursor.moveToNext()) {
            long silentActionID = getLongFromCursor(silentActionCursor,
                RegisteredActionDbAdapter.KEY_ACTIONID);
            ruleActionAdapter.insert(timeToSilentRuleID, silentActionID);
            ruleFilterAdapter.insert(timeToSilentRuleID, timeAttrbutesID, -1l,
                timeToSilentFilterID, -1l, timeToSilentRuleTime);
          }
          silentActionCursor.close();
        }
        timeToSilentFilterCursor.close();

        // Set rule: Wake up during daytime
        Cursor timeToLoudFilterCursor = dataFilterAdapter.fetchAll(null,
            OmniDate.Filter.IS_EVERYDAY.displayName, null, null);
        if (timeToLoudFilterCursor.moveToNext()) {
          long timeToLoudFilterID = getLongFromCursor(timeToLoudFilterCursor,
              DataFilterDbAdapter.KEY_DATAFILTERID);
          String timeToLoudRuleName = context.getString(R.string.timeToLoudRuleName);
          String timeToLoudRuleDesc = context.getString(R.string.timeToLoudRuleDesc);
          long timeToLoudRuleID = ruleAdapter.insert(timeEventID, timeToLoudRuleName,
              timeToLoudRuleDesc, false);
          Cursor loudActionCursor = actionAdapter.fetchAll(SetPhoneLoudAction.ACTION_NAME, null);
          if (loudActionCursor.moveToNext()) {
            long loudActionID = getLongFromCursor(loudActionCursor,
                RegisteredActionDbAdapter.KEY_ACTIONID);
            ruleActionAdapter.insert(timeToLoudRuleID, loudActionID);
            ruleFilterAdapter.insert(timeToLoudRuleID, timeAttrbutesID, -1l, timeToLoudFilterID,
                -1l, timeToLoudRuleTime);
          }
          loudActionCursor.close();
        }
        timeToLoudFilterCursor.close();
      }
      timeAttributesCursor.close();
    }
    timeEventCursor.close();

    // Set default rule: Disable WiFi, when battery is low
    Cursor batteryLowEventCursor = eventsAdapter.fetchAll(SystemEvent.BatteryLowEvent.EVENT_NAME,
        null);
    if (batteryLowEventCursor.moveToNext()) {
      long batteryLowEventID = getLongFromCursor(batteryLowEventCursor,
          RegisteredEventDbAdapter.KEY_EVENTID);
      Cursor setWifiOffActionCursor = actionAdapter.fetchAll(TurnOffWifiAction.ACTION_NAME, null);
      if (setWifiOffActionCursor.moveToNext()) {
        long setWifiOffActionID = getLongFromCursor(setWifiOffActionCursor,
            RegisteredActionDbAdapter.KEY_ACTIONID);
        String batteryLowToSetWifiOffRuleName = context
            .getString(R.string.batteryLowToSetWifiOffRuleName);
        String batteryLowToSetWifiOffRuleDesc = context
            .getString(R.string.batteryLowToSetWifiOffRuleDesc);
        long batteryLowToSetWifiOffRuleID = ruleAdapter.insert(batteryLowEventID,
            batteryLowToSetWifiOffRuleName, batteryLowToSetWifiOffRuleDesc, false);
        ruleActionAdapter.insert(batteryLowToSetWifiOffRuleID, setWifiOffActionID);
      }
      setWifiOffActionCursor.close();
    }
    batteryLowEventCursor.close();
  }

  /**
   * Create the initial version of the Omnidroid database along with prepopulated data.
   * 
   * @param db
   *          SQLiteDatabase object to work with
   */
  @SuppressWarnings("deprecation")
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
    long appIdMedia = appDbAdapter.insert(DbHelper.AppName.MEDIA, "", true);
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
    
    actionDbAdapter.insert(PlayMediaAction.ACTION_NAME, appIdMedia);
    actionDbAdapter.insert(PauseMediaAction.ACTION_NAME, appIdMedia);

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
  @SuppressWarnings("deprecation")
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

  /**
   * Modify the Send Gmail and Update Twitter actions by replacing the username and password
   * attributes into user account (this is done for possible multi-account support and supporting
   * different authentication methods like OAuth). Also retrieves username and password from
   * existing rules and the latest entry is used if there are multiple actions that has username and
   * password.
   * 
   * @param db
   *          the database instance to work with
   */
  @SuppressWarnings("deprecation")
  private static void modifyGmailAndTwitterParam(SQLiteDatabase db) {
    DataTypeDbAdapter dataTypeDbAdapter = new DataTypeDbAdapter(db);
    long dataTypeIdAccount = dataTypeDbAdapter.insert(OmniUserAccount.DB_NAME,
        OmniUserAccount.class.getName());

    modifyActionToSupportUserAccount(db, DbHelper.AppName.GMAIL, SendGmailAction.ACTION_NAME,
        SendGmailAction.PARAM_USERNAME, SendGmailAction.PARAM_PASSWORD,
        SendGmailAction.PARAM_USER_ACCOUNT, dataTypeIdAccount);

    modifyActionToSupportUserAccount(db, DbHelper.AppName.TWITTER,
        UpdateTwitterStatusAction.ACTION_NAME, UpdateTwitterStatusAction.PARAM_USERNAME,
        UpdateTwitterStatusAction.PARAM_PASSWORD, UpdateTwitterStatusAction.PARAM_USER_ACCOUNT,
        dataTypeIdAccount);
  }

  /**
   * Modify the actions by replacing the username and password attributes into user account (this is
   * done for possible multi-account support and supporting different authentication methods like
   * OAuth). Also retrieves username and password from existing rules and the latest entry is used
   * if there are multiple actions that has username and password.
   * 
   * @param db
   *          the database instance to work with
   * @param appName
   *          the name of the application associated with the action
   * @param actionName
   *          the name of the action
   * @param usernameParamName
   *          the action's parameter name for username
   * @param passwordParamName
   *          the action's parameter name for password
   * @param userAccountParamName
   *          the action's parameter name for user account
   * @param dataTypeIdAccount
   *          primary key id for UserAccount datatype
   */
  private static void modifyActionToSupportUserAccount(SQLiteDatabase db, String appName,
      String actionName, String usernameParamName, String passwordParamName,
      String userAccountParamName, long dataTypeIdAccount) {
    // Get the App ID
    RegisteredAppDbAdapter appDbAdapter = new RegisteredAppDbAdapter(db);
    Cursor cursor = appDbAdapter.fetchAll(appName, "", true);
    cursor.moveToFirst();
    long appID = CursorHelper.getLongFromCursor(cursor, RegisteredAppDbAdapter.KEY_APPID);
    cursor.close();

    // Get the Action ID
    RegisteredActionDbAdapter actionDbAdapter = new RegisteredActionDbAdapter(db);
    cursor = actionDbAdapter.fetchAll(actionName, appID);
    cursor.moveToFirst();
    long actionId = CursorHelper.getLongFromCursor(cursor, RegisteredActionDbAdapter.KEY_ACTIONID);
    cursor.close();

    RegisteredActionParameterDbAdapter actionParameterDbAdapter = new 
        RegisteredActionParameterDbAdapter(db);

    /*
     * Modify the username parameter to user account. Update was used instead of delete then insert
     * to have the user account parameter appear on the top position when {@code
     * FactoryActions.buildUIFromAction} is called.
     */
    cursor = actionParameterDbAdapter.fetchAll(usernameParamName, actionId, null);
    cursor.moveToFirst();
    long paramID = CursorHelper.getLongFromCursor(cursor,
        RegisteredActionParameterDbAdapter.KEY_ACTIONPARAMETERID);

    actionParameterDbAdapter.update(paramID, userAccountParamName, null, dataTypeIdAccount);
    cursor.close();

    /*
     * Get the username from existing rules and set it to the application. Use the last entry if
     * there are multiple actions in the database.
     */
    RuleActionParameterDbAdapter ruleActionParamDb = new RuleActionParameterDbAdapter(db);
    cursor = ruleActionParamDb.fetchAll(null, paramID, null);
    if (cursor.moveToLast()) {
      String username = CursorHelper.getStringFromCursor(cursor,
          RuleActionParameterDbAdapter.KEY_RULEACTIONPARAMETERDATA);

      appDbAdapter.update(appID, null, null, null, null, username, null);
    }
    // No need to delete since paramID is now user account
    cursor.close();

    // Remove the password parameter
    cursor = actionParameterDbAdapter.fetchAll(passwordParamName, actionId, null);
    cursor.moveToFirst();
    paramID = CursorHelper.getLongFromCursor(cursor,
        RegisteredActionParameterDbAdapter.KEY_ACTIONPARAMETERID);
    actionParameterDbAdapter.delete(paramID);
    cursor.close();

    /*
     * Get the password from existing rules and set it to the application. Use the last entry if
     * there are multiple gmail send actions in the database. And remove all rule action password
     * parameter entries.
     */
    cursor = ruleActionParamDb.fetchAll(null, paramID, null);
    if (cursor.moveToLast()) {
      String password = CursorHelper.getStringFromCursor(cursor,
          RuleActionParameterDbAdapter.KEY_RULEACTIONPARAMETERDATA);

      appDbAdapter.update(appID, null, null, null, null, null, password);

      do {
        ruleActionParamDb.delete(CursorHelper.getLongFromCursor(cursor,
            RuleActionParameterDbAdapter.KEY_RULEACTIONPARAMETERID));
      } while (cursor.moveToPrevious());
    }
    cursor.close();
  }

  private static void addWifiActions(SQLiteDatabase db) {
    RegisteredAppDbAdapter appDbAdapter = new RegisteredAppDbAdapter(db);
    long appIdOmnidroid = appDbAdapter.getAppId(OmniAction.APP_NAME);

    RegisteredActionDbAdapter actionDbAdapter = new RegisteredActionDbAdapter(db);
    actionDbAdapter.insert(TurnOffWifiAction.ACTION_NAME, appIdOmnidroid);
    actionDbAdapter.insert(TurnOnWifiAction.ACTION_NAME, appIdOmnidroid);

  }

  private static void addNotification(SQLiteDatabase db) {
    db.execSQL(RuleDbAdapter.ADD_NOTIFICATION_COLUMN);
  }

  private static void addFailedActions(SQLiteDatabase db) {
    db.execSQL(FailedActionsDbAdapter.getSqliteCreateStatement());
    db.execSQL(FailedActionParameterDbAdapter.getSqliteCreateStatement());
  }

  private static void addGeneralLogLevels(SQLiteDatabase db) {
    db.execSQL(LogGeneralDbAdapter.ADD_LEVEL_COLUMN);
  }

  private static void addInternetAndServiceAvailableEvents(SQLiteDatabase db) {
    RegisteredAppDbAdapter registeredAppDbAdapter = new RegisteredAppDbAdapter(db);
    Cursor cursor = registeredAppDbAdapter.fetchAll(OmniAction.APP_NAME, null, null, null, null,
        null);
    cursor.moveToFirst();
    long appId = CursorHelper.getLongFromCursor(cursor, RegisteredAppDbAdapter.KEY_APPID);
    cursor.close();

    RegisteredEventDbAdapter registeredEventDbAdapter = new RegisteredEventDbAdapter(db);
    registeredEventDbAdapter.insert(InternetAvailableEvent.EVENT_NAME, appId);
    registeredEventDbAdapter.insert(ServiceAvailableEvent.EVENT_NAME, appId);
  }

  private static void addPhoneNumberNotEqualsFilter(SQLiteDatabase db) {
    DataFilterDbAdapter dataFilterDbAdapter = new DataFilterDbAdapter(db);
    DataTypeDbAdapter dataTypeDbAdapter = new DataTypeDbAdapter(db);
    Cursor cursor = dataTypeDbAdapter.fetchAll(OmniPhoneNumber.DB_NAME, OmniPhoneNumber.class
        .getName());
    if ((cursor != null) && (cursor.getCount() > 0)) {
      cursor.moveToFirst();
    }
    long dataTypeIdPhoneNumber = CursorHelper.getLongFromCursor(cursor,
        DataTypeDbAdapter.KEY_DATATYPEID);
    if (cursor != null) {
      cursor.close();
    }
    dataFilterDbAdapter.insert(OmniPhoneNumber.Filter.NOTEQUALS.toString(),
        OmniPhoneNumber.Filter.NOTEQUALS.displayName, dataTypeIdPhoneNumber, dataTypeIdPhoneNumber);
  }

  /**
   * Convert all time and location entries in the attributes table to general attributes.
   * 
   * @param db
   *          the database instance to work with
   */
  @SuppressWarnings("deprecation")
  private static void addSupportForGlobalEventAttributes(SQLiteDatabase db) {
    RegisteredEventAttributeDbAdapter eventAttributeDbAdapter = new RegisteredEventAttributeDbAdapter(
        db);

    DataTypeDbAdapter dataTypeDbAdapter = new DataTypeDbAdapter(db);

    // Query is unique enough to return one entry
    Cursor cursor = dataTypeDbAdapter.fetchAll(OmniDate.DB_NAME, OmniDate.class.getName());
    cursor.moveToFirst();
    long dataTypeIdDate = CursorHelper.getLongFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPEID);

    long dateAttributeID = eventAttributeDbAdapter.insertGeneralAttribute(Event.ATTRIBUTE_TIME,
        dataTypeIdDate);

    // Query is unique enough to return one entry
    cursor = dataTypeDbAdapter.fetchAll(OmniArea.DB_NAME, OmniArea.class.getName());
    cursor.moveToFirst();
    long dataTypeIdArea = CursorHelper.getLongFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPEID);

    long areaAttributeID = eventAttributeDbAdapter.insertGeneralAttribute(Event.ATTRIBUTE_LOCATION,
        dataTypeIdArea);

    cursor.close();

    RuleFilterDbAdapter ruleFilterDbAdapter = new RuleFilterDbAdapter(db);
    RuleActionParameterDbAdapter ruleActionParamDbAdapter = new RuleActionParameterDbAdapter(db);

    generalizeAttribute(SMSReceivedEvent.ATTRIB_MESSAGE_TIME, Event.ATTRIBUTE_TIME,
        dateAttributeID, eventAttributeDbAdapter, ruleFilterDbAdapter, ruleActionParamDbAdapter);
    generalizeAttribute(PhoneCallEvent.ATTRIBUTE_TIMESTAMP, Event.ATTRIBUTE_TIME, dateAttributeID,
        eventAttributeDbAdapter, ruleFilterDbAdapter, ruleActionParamDbAdapter);
    generalizeAttribute(LocationChangedEvent.ATTRIBUTE_CURRENT_LOCATION, Event.ATTRIBUTE_LOCATION,
        areaAttributeID, eventAttributeDbAdapter, ruleFilterDbAdapter, ruleActionParamDbAdapter);
    generalizeAttribute(TimeTickEvent.ATTRIBUTE_CURRENT_TIME, Event.ATTRIBUTE_TIME,
        dateAttributeID, eventAttributeDbAdapter, ruleFilterDbAdapter, ruleActionParamDbAdapter);
  }  
    
  private static void generalizeAttribute(String attributeName, String newAttributeName,
      long newAttributeDbID, RegisteredEventAttributeDbAdapter eventAttributeDbAdapter,
      RuleFilterDbAdapter ruleFilterDbAdapter, RuleActionParameterDbAdapter ruleActionParamDbAdapter) {

    Cursor cursor = eventAttributeDbAdapter.fetchAll(attributeName, null, null);

    while (cursor.moveToNext()) {
      // Delete the entry in the Event Attribute Table
      long primaryKey = CursorHelper.getLongFromCursor(cursor,
          RegisteredEventAttributeDbAdapter.KEY_EVENTATTRIBUTEID);
      eventAttributeDbAdapter.delete(primaryKey);

      // Update the Event Attribute ID on existing rule filters to the more general version
      ContentValues values = new ContentValues();
      values.put(RuleFilterDbAdapter.KEY_EVENTATTRIBUTEID, newAttributeDbID);
      ruleFilterDbAdapter.sqlUpdate(values, RuleFilterDbAdapter.KEY_EVENTATTRIBUTEID + " = "
          + primaryKey);

      // Update all attribute tags in existing rule parameters to the general version
      SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
      queryBuilder.appendWhere(RuleActionParameterDbAdapter.KEY_RULEACTIONPARAMETERDATA
          + " LIKE \"%<" + attributeName + ">%\"");

      Cursor paramCursor = ruleActionParamDbAdapter.sqlQuery(queryBuilder);
      while (paramCursor.moveToNext()) {
        long paramID = CursorHelper.getLongFromCursor(paramCursor,
            RuleActionParameterDbAdapter.KEY_RULEACTIONPARAMETERID);

        String newParamData = CursorHelper.getStringFromCursor(paramCursor,
            RuleActionParameterDbAdapter.KEY_RULEACTIONPARAMETERDATA).replaceAll(
            "<" + attributeName + ">", "<" + newAttributeName + ">");

        ruleActionParamDbAdapter.update(paramID, null, null, newParamData);
      }

      paramCursor.close();
    }

    cursor.close();
  }
  
  private static void alterFailedActionsTable(SQLiteDatabase db) {
   db.execSQL(FailedActionsDbAdapter.DATABASE_DROP);
   db.execSQL(FailedActionsDbAdapter.DATABASE_CREATE);    
  }

  private static void addMissedCallEvent(SQLiteDatabase db) {
    
    RegisteredAppDbAdapter registeredAppDbAdapter = new RegisteredAppDbAdapter(db); 
    Cursor cursor = registeredAppDbAdapter.fetchAll(MissedCallEvent.APPLICATION_NAME, null, null);
    cursor.moveToFirst();
    long appId = getLongFromCursor(cursor, RegisteredAppDbAdapter.KEY_APPID);
    
    RegisteredEventDbAdapter registeredEventDbAdapter = new RegisteredEventDbAdapter(db);
    long eventIdMissedCall = registeredEventDbAdapter.insert(MissedCallEvent.EVENT_NAME, appId);
    
    DataTypeDbAdapter dataTypeDbAdapter = new DataTypeDbAdapter(db);
    RegisteredEventAttributeDbAdapter eventAttributeDbAdapter = new RegisteredEventAttributeDbAdapter(db);
    //
    cursor = dataTypeDbAdapter.fetchAll(OmniPhoneNumber.DB_NAME, OmniPhoneNumber.class.getName());
    cursor.moveToFirst();
    long dataTypeIdPhoneNumber = CursorHelper.getLongFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPEID);
    eventAttributeDbAdapter.insert(MissedCallEvent.ATTRIBUTE_PHONE_NUMBER,
        eventIdMissedCall, dataTypeIdPhoneNumber);
  }

}
