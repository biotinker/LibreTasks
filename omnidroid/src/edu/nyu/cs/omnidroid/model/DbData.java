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
import edu.nyu.cs.omnidroid.core.CallPhoneAction;
import edu.nyu.cs.omnidroid.core.LocationChangedEvent;
import edu.nyu.cs.omnidroid.core.ShowNotificationAction;
import edu.nyu.cs.omnidroid.core.OmniAction;
import edu.nyu.cs.omnidroid.core.PhoneIsFallingEvent;
import edu.nyu.cs.omnidroid.core.PhoneRingingEvent;
import edu.nyu.cs.omnidroid.core.SMSReceivedEvent;
import edu.nyu.cs.omnidroid.core.ShowAlertAction;
import edu.nyu.cs.omnidroid.core.SendGmailAction;
import edu.nyu.cs.omnidroid.core.SendSmsAction;
import edu.nyu.cs.omnidroid.core.ShowWebsiteAction;
import edu.nyu.cs.omnidroid.core.SystemEvent;
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
    dataFilterDbAdapter.insert(OmniText.Filter.EQUALS.toString(), dataTypeIdText, dataTypeIdText);
    dataFilterDbAdapter.insert(OmniText.Filter.CONTAINS.toString(), dataTypeIdText, dataTypeIdText);
    
    long dataTypeIdPhone = dataTypeDbAdapter.insert(
        OmniPhoneNumber.DB_NAME, OmniPhoneNumber.class.getName());
    dataFilterDbAdapter.insert(OmniPhoneNumber.Filter.EQUALS.toString(), dataTypeIdPhone, 
        dataTypeIdPhone);
    
    long dataTypeIdDayOfWeek = dataTypeDbAdapter.insert(
        OmniDayOfWeek.DB_NAME, OmniDayOfWeek.class.getName());
    
    long dataTypeIdTimePeriod = dataTypeDbAdapter.insert(
        OmniTimePeriod.DB_NAME, OmniTimePeriod.class.getName());
    long dataTypeIdDate = dataTypeDbAdapter.insert(OmniDate.DB_NAME, OmniDate.class.getName());
    
    dataFilterDbAdapter.insert(OmniTimePeriod.Filter.DURING_EVERYDAY.toString(), 
        dataTypeIdTimePeriod, dataTypeIdDate);
    dataFilterDbAdapter.insert(OmniTimePeriod.Filter.EXCEPT_EVERYDAY.toString(), 
        dataTypeIdTimePeriod, dataTypeIdDate);
    
    dataFilterDbAdapter.insert(OmniDate.Filter.BEFORE_EVERYDAY.toString(), dataTypeIdDate, 
        dataTypeIdDate);
    dataFilterDbAdapter.insert(OmniDate.Filter.AFTER_EVERYDAY.toString(), dataTypeIdDate, 
        dataTypeIdDate);
    dataFilterDbAdapter.insert(OmniDate.Filter.DURING_EVERYDAY.toString(), dataTypeIdDate, 
        dataTypeIdTimePeriod);
    dataFilterDbAdapter.insert(OmniDate.Filter.EXCEPT_EVERYDAY.toString(), dataTypeIdDate, 
        dataTypeIdTimePeriod);
    dataFilterDbAdapter.insert(OmniDate.Filter.ISDAYOFWEEK.toString(), dataTypeIdDate, 
        dataTypeIdDayOfWeek);
    
    long dataTypeIdArea = dataTypeDbAdapter.insert(
        OmniArea.DB_NAME, OmniArea.class.getName());
    dataFilterDbAdapter.insert(OmniArea.Filter.NEAR.toString(), dataTypeIdArea, dataTypeIdArea);
    dataFilterDbAdapter.insert(OmniArea.Filter.AWAY.toString(), dataTypeIdArea, dataTypeIdArea);
    
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
    long appIdSensor = appDbAdapter.insert("Sensor", "", true);
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
    
    
    long eventIdSmsRec = eventDbAdapter.insert(SMSReceivedEvent.EVENT_NAME, appIdSms);  
    eventAttributeDbAdapter.insert(
        SMSReceivedEvent.ATTRIB_PHONE_NO, eventIdSmsRec, dataTypeIdPhone);
    eventAttributeDbAdapter.insert(
        SMSReceivedEvent.ATTRIB_MESSAGE_TEXT, eventIdSmsRec, dataTypeIdText);
    eventAttributeDbAdapter.insert(
        SMSReceivedEvent.ATTRIB_MESSAGE_TIME, eventIdSmsRec, dataTypeIdDate);

    long eventIdPhoneRings = eventDbAdapter.insert(PhoneRingingEvent.EVENT_NAME, appIdPhone);  
    eventAttributeDbAdapter.insert(
        PhoneRingingEvent.ATTRIBUTE_PHONE_NUMBER, eventIdPhoneRings, dataTypeIdPhone);
    eventAttributeDbAdapter.insert(
        PhoneRingingEvent.ATTRIBUTE_PHONE_RING_TIME, eventIdPhoneRings, dataTypeIdDate);
    
    long eventIdGPSLocationChanged = eventDbAdapter.insert(
        LocationChangedEvent.EVENT_NAME, appIdGPS);  
    eventAttributeDbAdapter.insert(
        LocationChangedEvent.ATTRIBUTE_CURRENT_LOCATION, eventIdGPSLocationChanged, dataTypeIdArea);
    
    eventDbAdapter.insert(PhoneIsFallingEvent.EVENT_NAME, appIdSensor);  

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
    
    long actionIdSmsSend = actionDbAdapter.insert(SendSmsAction.ACTION_NAME, appIdSms);
    actionParameterDbAdapter.insert(SendSmsAction.PARAM_PHONE_NO, actionIdSmsSend, 
        dataTypeIdPhone);
    actionParameterDbAdapter.insert(SendSmsAction.PARAM_SMS, actionIdSmsSend, dataTypeIdText);
    
    long actionIdPhoneCall = actionDbAdapter.insert(CallPhoneAction.ACTION_NAME, appIdPhone);
    actionParameterDbAdapter.insert(
        CallPhoneAction.PARAM_PHONE_NO, actionIdPhoneCall, dataTypeIdPhone);

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
    
    long actionIdTwitterUpdate = actionDbAdapter.insert(UpdateTwitterStatusAction.ACTION_NAME, appIdTwitter);
    actionParameterDbAdapter.insert(
        UpdateTwitterStatusAction.PARAM_USERNAME, actionIdTwitterUpdate, dataTypeIdText);
    actionParameterDbAdapter.insert(
        UpdateTwitterStatusAction.PARAM_PASSWORD, actionIdTwitterUpdate, dataTypeIdPasswordInput);
    actionParameterDbAdapter.insert(
        UpdateTwitterStatusAction.PARAM_MESSAGE, actionIdTwitterUpdate, dataTypeIdText);
    
  }
}