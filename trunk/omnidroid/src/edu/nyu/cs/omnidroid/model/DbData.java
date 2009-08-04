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

import edu.nyu.cs.omnidroid.model.db.DataFilterDbAdapter;
import edu.nyu.cs.omnidroid.model.db.DataTypeDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredActionDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredActionParameterDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredAppDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredEventAttributeDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredEventDbAdapter;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class store data that needs to be populated into the DB by default
 */
public class DbData {
  
  /* DataType names and their DataFilter names */
  public static final String DATATYPE_TEXT = "Text";
  public static final String DATAFILTER_TEXT_EQUALS = "equals";
  public static final String DATAFILTER_TEXT_CONTAINS = "contains";
  
  public static final String DATATYPE_PHONENUMBER = "PhoneNumber";
  public static final String DATAFILTER_PHONENUMBER_EQUALS = "equals";

  public static final String DATATYPE_DAYOFWEEK = "DayOfWeek";
  
  public static final String DATATYPE_DATE = "Date";
  public static final String DATAFILTER_DATE_BEFORE = "before";
  public static final String DATAFILTER_DATE_AFTER = "after";
  public static final String DATAFILTER_DATE_ISDAYOFWEEK = "isDayOfWeek";
  
  public static final String DATATYPE_AREA = "Area";
  public static final String DATAFILTER_AREA_NEAR = "near";
  public static final String DATAFILTER_AREA_AWAY = "away";
  
  /* Registered App Names */
  public static final String APP_SMS = "SMS";
  public static final String APP_PHONE = "Phone";
  public static final String APP_GPS = "GPS";
  public static final String APP_SENSOR = "Sensor";
  
  /* Registered Events and their Attributes names */
  public static final String EVENT_SMS_REC = "SMS Received";
  public static final String ATTR_SMS_REC_PHONENUMBER = "SMS Phonenumber";
  public static final String ATTR_SMS_RES_MESSAGE = "SMS Text";
  public static final String EVENT_GPS_LOCATION_CHANGED = "GPS Location Changed";
  public static final String ATTR_GPS_CURRENT_LOCATION = "Current Location";
  public static final String EVENT_SENSOR_PHONE_IS_FALLING = "Phone Is Falling";
  public static final String EVENT_PHONE_RING = "Phone is Ringing";
  public static final String ATTR_PHONE_PHONE_NUMBER = "Phone Number";
  
  /* Registered Actions and their parameters name */
  public static final String ACTION_SMS_SEND = "SMS Send";
  public static final String PARAM_SMS_SEND_PHONENUMBER = "Phone Number";
  public static final String PARAM_SMS_SEND_MESSAGE = "Text Message";
  
  public static final String ACTION_DIAL_PHONECALL = "Dial Number";
  public static final String PARAM_DIAL_PHONECALL_PHONENUMBER = "Phone Number";
  
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

    /* Populate data types and their data filters */
    DataTypeDbAdapter dataTypeDbAdapter = new DataTypeDbAdapter(db);
    DataFilterDbAdapter dataFilterDbAdapter = new DataFilterDbAdapter(db);
    
    long dataTypeIdText = dataTypeDbAdapter.insert(DATATYPE_TEXT, "OmniText");
    dataFilterDbAdapter.insert(DATAFILTER_TEXT_EQUALS, dataTypeIdText, dataTypeIdText);
    dataFilterDbAdapter.insert(DATAFILTER_TEXT_CONTAINS, dataTypeIdText, dataTypeIdText);
    
    long dataTypeIdPhone = dataTypeDbAdapter.insert(DATATYPE_PHONENUMBER, "OmniPhoneNumber");
    dataFilterDbAdapter.insert(DATAFILTER_PHONENUMBER_EQUALS, dataTypeIdPhone, dataTypeIdPhone);
    
    long dataTypeIdDayOfWeek = dataTypeDbAdapter.insert(DATATYPE_DAYOFWEEK, "OmniDayOfWeek");
    
    long dataTypeIdDate = dataTypeDbAdapter.insert(DATATYPE_DATE, "OmniDate");
    dataFilterDbAdapter.insert(DATAFILTER_DATE_BEFORE, dataTypeIdDate, dataTypeIdDate);
    dataFilterDbAdapter.insert(DATAFILTER_DATE_AFTER, dataTypeIdDate, dataTypeIdDate);
    dataFilterDbAdapter.insert(DATAFILTER_DATE_ISDAYOFWEEK, dataTypeIdDate, dataTypeIdDayOfWeek);
    
    long dataTypeIdArea = dataTypeDbAdapter.insert(DATATYPE_AREA, "OmniArea");
    dataFilterDbAdapter.insert(DATAFILTER_AREA_NEAR, dataTypeIdArea, dataTypeIdArea);
    dataFilterDbAdapter.insert(DATAFILTER_AREA_AWAY, dataTypeIdArea, dataTypeIdArea);
    
    /* Populate registered applications */
    RegisteredAppDbAdapter appDbAdapter = new RegisteredAppDbAdapter(db);
    long appIdSms = appDbAdapter.insert(APP_SMS, "", true);
    long appIdPhone = appDbAdapter.insert(APP_PHONE, "", true);
    long appIdGPS = appDbAdapter.insert(APP_GPS, "", true);
    long appIdSensor = appDbAdapter.insert(APP_SENSOR, "", true);
    
    /* Populate registered events and event attributes */ 
    RegisteredEventDbAdapter eventDbAdapter = new RegisteredEventDbAdapter(db);
    RegisteredEventAttributeDbAdapter eventAttributeDbAdapter = 
        new RegisteredEventAttributeDbAdapter(db);
    
    long eventIdSmsRec = eventDbAdapter.insert(EVENT_SMS_REC, appIdSms);  
    eventAttributeDbAdapter.insert(ATTR_SMS_REC_PHONENUMBER, eventIdSmsRec, dataTypeIdPhone);
    eventAttributeDbAdapter.insert(ATTR_SMS_RES_MESSAGE, eventIdSmsRec, dataTypeIdText);

    long eventIdPhoneRings = eventDbAdapter.insert(EVENT_PHONE_RING, appIdPhone);  
    eventAttributeDbAdapter.insert(ATTR_PHONE_PHONE_NUMBER, eventIdPhoneRings, dataTypeIdPhone);
    
    long eventIdGPSLocationChanged = eventDbAdapter.insert(EVENT_GPS_LOCATION_CHANGED, appIdGPS);  
    eventAttributeDbAdapter.insert(ATTR_GPS_CURRENT_LOCATION, eventIdGPSLocationChanged, dataTypeIdArea);
    
    long eventIdSensorPhoneIsFalling = eventDbAdapter.insert(EVENT_SENSOR_PHONE_IS_FALLING, appIdSensor);  

    /* Populate registered actions and action parameters */
    RegisteredActionDbAdapter actionDbAdapter = new RegisteredActionDbAdapter(db);
    RegisteredActionParameterDbAdapter actionParameterDbAdapter = 
      new RegisteredActionParameterDbAdapter(db);
    
    long actionIdSmsSend = actionDbAdapter.insert(ACTION_SMS_SEND, appIdSms);
    actionParameterDbAdapter.insert(PARAM_SMS_SEND_PHONENUMBER, actionIdSmsSend, dataTypeIdPhone);
    actionParameterDbAdapter.insert(PARAM_SMS_SEND_MESSAGE, actionIdSmsSend, dataTypeIdText);
    
    long actionIdPhoneCall = actionDbAdapter.insert(ACTION_DIAL_PHONECALL, appIdPhone);
    actionParameterDbAdapter.insert(PARAM_DIAL_PHONECALL_PHONENUMBER, actionIdPhoneCall,
        dataTypeIdPhone);
  }
}