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
package edu.nyu.cs.omnidroid.model;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import edu.nyu.cs.omnidroid.core.CallPhoneAction;
import edu.nyu.cs.omnidroid.core.LocationChangedEvent;
import edu.nyu.cs.omnidroid.core.SetPhoneLoudAction;
import edu.nyu.cs.omnidroid.core.SetPhoneSilentAction;
import edu.nyu.cs.omnidroid.core.SetPhoneVibrateAction;
import edu.nyu.cs.omnidroid.core.SetScreenBrightnessAction;
import edu.nyu.cs.omnidroid.core.ShowNotificationAction;
import edu.nyu.cs.omnidroid.core.OmniAction;
import edu.nyu.cs.omnidroid.core.PhoneRingingEvent;
import edu.nyu.cs.omnidroid.core.SMSReceivedEvent;
import edu.nyu.cs.omnidroid.core.ShowAlertAction;
import edu.nyu.cs.omnidroid.core.SendGmailAction;
import edu.nyu.cs.omnidroid.core.SendSmsAction;
import edu.nyu.cs.omnidroid.core.ShowWebsiteAction;
import edu.nyu.cs.omnidroid.core.SystemEvent;
import edu.nyu.cs.omnidroid.core.TimeTickEvent;
import edu.nyu.cs.omnidroid.core.UpdateTwitterStatusAction;
import edu.nyu.cs.omnidroid.core.datatypes.OmniArea;
import edu.nyu.cs.omnidroid.core.datatypes.OmniDate;
import edu.nyu.cs.omnidroid.core.datatypes.OmniDayOfWeek;
import edu.nyu.cs.omnidroid.core.datatypes.OmniPasswordInput;
import edu.nyu.cs.omnidroid.core.datatypes.OmniPhoneNumber;
import edu.nyu.cs.omnidroid.core.datatypes.OmniText;
import edu.nyu.cs.omnidroid.core.datatypes.OmniTimePeriod;
import edu.nyu.cs.omnidroid.model.db.DataFilterDbAdapter;
import edu.nyu.cs.omnidroid.model.db.DataTypeDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredActionDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredActionParameterDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredAppDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredEventAttributeDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredEventDbAdapter;

/**
 * This class store data that needs to be populated into the DB by default
 */
public class DbData {
  
  /**
   * This class does not need to be instantiated.
   */
  private DbData() {
  }
  
  /**
   * Pre-populate data into the database
   * 
   * @param db
   *          SQLiteDatabase object to work with
   */
  public static void prepopulate(SQLiteDatabase db) {

    /*
     * Populate data types and their data filters
     */
    DataTypeDbAdapter dataTypeDbAdapter = new DataTypeDbAdapter(db);
    DataFilterDbAdapter dataFilterDbAdapter = new DataFilterDbAdapter(db);
    
    long dataTypeIdText = dataTypeDbAdapter.insert(
        OmniText.DB_NAME, OmniText.class.getName());
    dataFilterDbAdapter.insert(OmniText.Filter.EQUALS.toString(), 
        OmniText.Filter.EQUALS.displayName, dataTypeIdText, dataTypeIdText);
    dataFilterDbAdapter.insert(OmniText.Filter.CONTAINS.toString(), 
        OmniText.Filter.CONTAINS.displayName, dataTypeIdText, dataTypeIdText);
    
    long dataTypeIdPhoneNumber = dataTypeDbAdapter.insert(
        OmniPhoneNumber.DB_NAME, OmniPhoneNumber.class.getName());
    dataFilterDbAdapter.insert(OmniPhoneNumber.Filter.EQUALS.toString(), 
        OmniPhoneNumber.Filter.EQUALS.displayName, dataTypeIdPhoneNumber, 
        dataTypeIdPhoneNumber);
    
    long dataTypeIdDayOfWeek = dataTypeDbAdapter.insert(
        OmniDayOfWeek.DB_NAME, OmniDayOfWeek.class.getName());
    
    long dataTypeIdTimePeriod = dataTypeDbAdapter.insert(
        OmniTimePeriod.DB_NAME, OmniTimePeriod.class.getName());
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
        OmniDate.Filter.ISDAYOFWEEK.displayName, dataTypeIdDate, 
        dataTypeIdDayOfWeek);
    
    long dataTypeIdArea = dataTypeDbAdapter.insert(
        OmniArea.DB_NAME, OmniArea.class.getName());
    dataFilterDbAdapter.insert(OmniArea.Filter.NEAR.toString(), OmniArea.Filter.NEAR.displayName, 
        dataTypeIdArea, dataTypeIdArea);
    dataFilterDbAdapter.insert(OmniArea.Filter.AWAY.toString(), OmniArea.Filter.AWAY.displayName, 
        dataTypeIdArea, dataTypeIdArea);
    
    long dataTypeIdPasswordInput = dataTypeDbAdapter.insert(
        OmniPasswordInput.DB_NAME, OmniPasswordInput.class.getName());
    
    /*
     *  Populate registered applications
     */ 
    // TODO(ehotou) Consider move these static strings to a storage class.
    RegisteredAppDbAdapter appDbAdapter = new RegisteredAppDbAdapter(db);
    long appIdSms = appDbAdapter.insert("SMS", "", true);
    long appIdPhone = appDbAdapter.insert("Phone", "", true);
    long appIdGPS = appDbAdapter.insert("GPS", "", true);
    long appIdGmail = appDbAdapter.insert("GMAIL", "", true);
    long appIdTwitter = appDbAdapter.insert("Twitter", "", true);
    long appIdOmnidroid = appDbAdapter.insert(OmniAction.APP_NAME, "", true);
    long appIdAndroid = appDbAdapter.insert(SystemEvent.PowerConnectedEvent.APPLICATION_NAME
        , "", true);
    
    
    /*
     *  Populate registered events and event attributes
     */ 
    RegisteredEventDbAdapter eventDbAdapter = new RegisteredEventDbAdapter(db);
    RegisteredEventAttributeDbAdapter eventAttributeDbAdapter = 
        new RegisteredEventAttributeDbAdapter(db);
    
    for (SystemEvent e : SystemEvent.values()) {
      eventDbAdapter.insert(e.EVENT_NAME, appIdAndroid);
    }
    
    //TODO(Roger): for testing, remove later..
    Log.d("DbData", "updating..");
    
    long eventIdSmsRec = eventDbAdapter.insert(SMSReceivedEvent.EVENT_NAME, appIdSms);  
    eventAttributeDbAdapter.insert(
        SMSReceivedEvent.ATTRIB_PHONE_NO, eventIdSmsRec, dataTypeIdPhoneNumber);
    eventAttributeDbAdapter.insert(
        SMSReceivedEvent.ATTRIB_MESSAGE_TEXT, eventIdSmsRec, dataTypeIdText);
    eventAttributeDbAdapter.insert(
        SMSReceivedEvent.ATTRIB_MESSAGE_TIME, eventIdSmsRec, dataTypeIdDate);

    long eventIdPhoneRings = eventDbAdapter.insert(PhoneRingingEvent.EVENT_NAME, appIdPhone);  
    eventAttributeDbAdapter.insert(
        PhoneRingingEvent.ATTRIBUTE_PHONE_NUMBER, eventIdPhoneRings, dataTypeIdPhoneNumber);
    eventAttributeDbAdapter.insert(
        PhoneRingingEvent.ATTRIBUTE_PHONE_RING_TIME, eventIdPhoneRings, dataTypeIdDate);
    
    long eventIdGPSLocationChanged = eventDbAdapter.insert(
        LocationChangedEvent.EVENT_NAME, appIdGPS);  
    eventAttributeDbAdapter.insert(
        LocationChangedEvent.ATTRIBUTE_CURRENT_LOCATION, eventIdGPSLocationChanged, dataTypeIdArea);

    long eventIdTimeTick = eventDbAdapter.insert(
        TimeTickEvent.EVENT_NAME, appIdAndroid);  
    eventAttributeDbAdapter.insert(
        TimeTickEvent.ATTRIBUTE_CURRENT_TIME, eventIdTimeTick, dataTypeIdDate);
    
    /*
     *  Populate registered actions and action parameters
     */
    RegisteredActionDbAdapter actionDbAdapter = new RegisteredActionDbAdapter(db);
    RegisteredActionParameterDbAdapter actionParameterDbAdapter = 
      new RegisteredActionParameterDbAdapter(db);
    
    long actionIdDisplayMessage = actionDbAdapter.insert(ShowAlertAction.ACTION_NAME, 
        appIdOmnidroid);
    actionParameterDbAdapter.insert(ShowAlertAction.PARAM_ALERT_MESSAGE, 
        actionIdDisplayMessage, dataTypeIdText);  
    long actionIdNotifyMessage = actionDbAdapter.insert(ShowNotificationAction.ACTION_NAME, 
        appIdOmnidroid);
    actionParameterDbAdapter.insert(ShowNotificationAction.PARAM_ALERT_MESSAGE, 
        actionIdNotifyMessage, dataTypeIdText);  
    long actionIdShowWebsite = actionDbAdapter.insert(ShowWebsiteAction.ACTION_NAME, 
        appIdOmnidroid);
    actionParameterDbAdapter.insert(ShowWebsiteAction.PARAM_WEB_URL, 
        actionIdShowWebsite, dataTypeIdText);  
    long actionIdSetBrightness = actionDbAdapter.insert(SetScreenBrightnessAction.ACTION_NAME, 
        appIdOmnidroid);
    actionParameterDbAdapter.insert(SetScreenBrightnessAction.PARAM_BRIGHTNESS, 
        actionIdSetBrightness, dataTypeIdText);  
     actionDbAdapter.insert(SetPhoneLoudAction.ACTION_NAME, 
        appIdOmnidroid);
     actionDbAdapter.insert(SetPhoneSilentAction.ACTION_NAME, 
         appIdOmnidroid);
     actionDbAdapter.insert(SetPhoneVibrateAction.ACTION_NAME, 
         appIdOmnidroid);
     
    long actionIdSmsSend = actionDbAdapter.insert(SendSmsAction.ACTION_NAME, appIdSms);
    actionParameterDbAdapter.insert(SendSmsAction.PARAM_PHONE_NO, actionIdSmsSend, 
        dataTypeIdPhoneNumber);
    actionParameterDbAdapter.insert(SendSmsAction.PARAM_SMS, actionIdSmsSend, dataTypeIdText);
    
    long actionIdPhoneCall = actionDbAdapter.insert(CallPhoneAction.ACTION_NAME, appIdPhone);
    actionParameterDbAdapter.insert(
        CallPhoneAction.PARAM_PHONE_NO, actionIdPhoneCall, dataTypeIdPhoneNumber);

    long actionIdGmailSend = actionDbAdapter.insert(SendGmailAction.ACTION_NAME, appIdGmail);
    actionParameterDbAdapter.insert(
        SendGmailAction.PARAM_USERNAME, actionIdGmailSend, dataTypeIdText);
    actionParameterDbAdapter.insert(
        SendGmailAction.PARAM_PASSWORD, actionIdGmailSend, dataTypeIdPasswordInput);
    actionParameterDbAdapter.insert(
        SendGmailAction.PARAM_TO, actionIdGmailSend, dataTypeIdText);
    actionParameterDbAdapter.insert(
        SendGmailAction.PARAM_SUBJECT, actionIdGmailSend, dataTypeIdText);
    actionParameterDbAdapter.insert(
        SendGmailAction.PARAM_BODY, actionIdGmailSend, dataTypeIdText);
    
    long actionIdTwitterUpdate = actionDbAdapter.insert(UpdateTwitterStatusAction.ACTION_NAME, 
        appIdTwitter);
    actionParameterDbAdapter.insert(
        UpdateTwitterStatusAction.PARAM_USERNAME, actionIdTwitterUpdate, dataTypeIdText);
    actionParameterDbAdapter.insert(
        UpdateTwitterStatusAction.PARAM_PASSWORD, actionIdTwitterUpdate, dataTypeIdPasswordInput);
    actionParameterDbAdapter.insert(
        UpdateTwitterStatusAction.PARAM_MESSAGE, actionIdTwitterUpdate, dataTypeIdText);
    
  }
}