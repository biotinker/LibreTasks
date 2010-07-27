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
package edu.nyu.cs.omnidroid.app.controller;

import java.util.ArrayList;
import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniArea;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate;
import edu.nyu.cs.omnidroid.app.controller.events.InternetAvailableEvent;
import edu.nyu.cs.omnidroid.app.controller.events.ServiceAvailableEvent;
import edu.nyu.cs.omnidroid.app.controller.events.TimeTickEvent;
import android.preference.PreferenceManager;
import android.util.Log;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.util.Logger;
import edu.nyu.cs.omnidroid.app.controller.util.OmnidroidException;
import edu.nyu.cs.omnidroid.app.model.CoreActionLogsDbHelper;
import edu.nyu.cs.omnidroid.app.model.CoreActionsDbHelper;
import edu.nyu.cs.omnidroid.app.model.CoreEventLogsDbHelper;
import edu.nyu.cs.omnidroid.app.model.CoreGeneralLogsDbHelper;
import edu.nyu.cs.omnidroid.app.model.CoreRulesDbHelper;
import edu.nyu.cs.omnidroid.app.model.ActionLog;
import edu.nyu.cs.omnidroid.app.model.EventLog;
import edu.nyu.cs.omnidroid.app.model.FailedActionsDbHelper;
import edu.nyu.cs.omnidroid.app.model.GeneralLog;
import edu.nyu.cs.omnidroid.app.view.simple.UtilUI;

/**
 * This class is the heart of Omnidroid. When this class receives a system intent from
 * {@link BCReceiver} it calls {@link IntentParser} to create an {@link Event} if it is supported by
 * Omnidroid. The event is passed to the {@link RuleProcessor} to see if the event's attributes are
 * matched by the parameters of the user's defined {@link Rule}. The {@link Action}(s) of any rules
 * that match are passed to ActionExecuter where they are packaged into system intents and run.
 */
public class HandlerService extends Service {
  private static final String TAG = HandlerService.class.getSimpleName();

  // Limit the number of rules that can be applied in any minute (stored in string form)
  private static final String THROTTLE_DEFAULT = "15";

  // Throttle disabled value
  private static final int THROTTLE_DISABLED = 0;

  /**
   * @see android.app.Service#onCreate()
   */
  @Override
  public void onCreate() {
    super.onCreate();
  }

  /**
   * @return true if over throttle limit, false otherwise.
   */
  // TODO(acase): Write Test Units for this.
  private boolean throttled() {
    // Default is not throttled
    boolean throttled = false;

    // Get throttle setting
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    String sThrottle = prefs.getString(getString(R.string.pref_key_throttle), THROTTLE_DEFAULT);
    int throttle = Integer.parseInt(sThrottle);

    CoreEventLogsDbHelper coreEventLogsDbHelper = new CoreEventLogsDbHelper(this);

    /*
     * See how many events we've taken in the last minute and if it's higher than our throttle
     * limit, then log/notify that this event is being ignored due to a possible loop or abuse.
     */
    if ((throttle != THROTTLE_DISABLED)
        && (coreEventLogsDbHelper.getLogCountDuringLastMinute() > throttle)) {
      // Log event to logcat
      String log = getString(R.string.throttle_alert_msg, throttle, coreEventLogsDbHelper
          .getLogCountDuringLastMinute());
      Logger.w(TAG, log);

      // Log event in DB
      CoreGeneralLogsDbHelper coreGeneralLogsDbHelper = new CoreGeneralLogsDbHelper(this);
      GeneralLog generalLog = new GeneralLog(log, Logger.INFO);
      coreGeneralLogsDbHelper.insert(generalLog);
      coreGeneralLogsDbHelper.close();

      // Send user notification
      UtilUI.showNotification(this, UtilUI.NOTIFICATION_WARN,
          getString(R.string.throttle_alert_title), log.toString());
      throttled = true;
    }
    
    coreEventLogsDbHelper.close();
    return throttled;
  }

  /**
   * Gets the event type from the intent, checks it against defined rules, and launches any
   * triggered actions
   * 
   * @see android.app.Service#onStart(Intent, int)
   */
  @Override
  public void onStart(Intent intent, int id) {
    addGlobalAttributesToIntent(intent);
    Event event = IntentParser.getEvent(intent);

    if (event != null) {
      // Log the event that occurred
      CoreEventLogsDbHelper coreEventLogsDbHelper = new CoreEventLogsDbHelper(this);
      EventLog logEvent = new EventLog(event);
      Long logID = coreEventLogsDbHelper.insert(logEvent);
      coreEventLogsDbHelper.close();
      logEvent.setID(logID);

      // Don't run if we're over our throttle threshold
      if (throttled()) {
        return;
      }

      // Open up Rule/Action Database connections
      CoreRulesDbHelper coreRuleDbHelper = new CoreRulesDbHelper(this);
      CoreActionsDbHelper coreActionsDbHelper = new CoreActionsDbHelper(this);

      // Open our Log accessor utilities to do some log checking/updating
      CoreActionLogsDbHelper coreActionLogsDbHelper = new CoreActionLogsDbHelper(this);

      // Get a list of actions that apply to this event.
      ArrayList<Action> actions = RuleProcessor.getActions(event, coreRuleDbHelper,
          coreActionsDbHelper);

      // Close Rule/Action Database connections
      coreActionsDbHelper.close();
      coreRuleDbHelper.close();

      // Log the actions taking place
      for (Action action : actions) {
        ActionLog logAction = new ActionLog(action, logEvent.getID());
        coreActionLogsDbHelper.insert(logAction);
      }
      coreActionLogsDbHelper.close();

      // Create a general log about what is going on
      CoreGeneralLogsDbHelper coreGeneralLogsDbHelper = new CoreGeneralLogsDbHelper(this);
      GeneralLog generalLog = new GeneralLog(TAG + " got " + actions.size()
          + " action(s) for event " + intent.getAction(), Logger.INFO);
      coreGeneralLogsDbHelper.insert(generalLog);
      coreGeneralLogsDbHelper.close();
      Logger.d(TAG, "got " + actions.size() + " action(s) for event " + intent.getAction());

      // Execute the list of actions.
      try {
        ActionExecuter.executeActions(this, actions);
      } catch (OmnidroidException e) {
        Logger.w(TAG, e.toString(), e);
        Logger.w(TAG, e.getLocalizedMessage());
        Logger.w(TAG, "Illegal Execution Method");
      } finally {
        actions.clear();
      }
      
      FailedActionsDbHelper failedActionsDbHelper = new FailedActionsDbHelper(this);
      if (event.getEventName().equals(InternetAvailableEvent.EVENT_NAME)) {
        actions = failedActionsDbHelper.getActions(ResultProcessor.RESULT_FAILURE_INTERNET);
      } else if (event.getEventName().equals(ServiceAvailableEvent.EVENT_NAME)) {
        actions = failedActionsDbHelper.getActions(ResultProcessor.RESULT_FAILURE_SERVICE);
      } else if (event.getEventName().equals(TimeTickEvent.EVENT_NAME)) {
        //TODO clean up old actions;
        actions = failedActionsDbHelper.getActions(ResultProcessor.RESULT_FAILURE_UNKNOWN);
      }
      failedActionsDbHelper.close();
      try {
        Logger.i(TAG, "Retrying to execute queued actions");
        ActionExecuter.executeActions(this, actions);;
      } catch (OmnidroidException e) {
        Logger.w(TAG, e.toString(), e);
      }
      
    }
    // Nothing left to do for this event
    stopSelf();
  }

  /**
   * Add global attributes of an event to the extra values of the intent.
   * 
   * @param intent
   *          the intent to modify
   */
  private void addGlobalAttributesToIntent(Intent intent) {
    if (!intent.hasExtra(Event.ATTRIBUTE_TIME)) {
      insertTimeStamp(intent);
    }

    if (!intent.hasExtra(Event.ATTRIBUTE_LOCATION)) {
      insertLocationData(intent);
    }
  }

  /**
   * Insert a time stamp to the intent.
   * 
   * @param intent
   *          the intent to modify
   */
  private void insertTimeStamp(Intent intent) {
    Date date = new Date(System.currentTimeMillis());
    OmniDate omniDate = new OmniDate(date);

    intent.putExtra(Event.ATTRIBUTE_TIME, omniDate.toString());
  }

  /**
   * Insert GPS location data to the intent.
   * 
   * @param intent
   *          the intent to modify
   */
  private void insertLocationData(Intent intent) {
    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    String bestProvider = locationManager.getBestProvider(new Criteria(), true);
    Location location = locationManager.getLastKnownLocation(bestProvider);

    String locationData;
    try {
      OmniArea newLocation = new OmniArea(null, location.getLatitude(), location.getLongitude(),
          location.getAccuracy());
      locationData = newLocation.toString();
    } catch (Exception e) {
      locationData = "";

      final String GET_LOCATION_FAILURE_LOG_MSG = "Unable to retrieve location data";

      if (location == null) {
        /*
         * Use the normal logging since this case happens quite often, and we don't want to clutter
         * the logs very much.
         */
        Log.i(TAG, GET_LOCATION_FAILURE_LOG_MSG);
      } else {
        Log.e(TAG, GET_LOCATION_FAILURE_LOG_MSG, e);
      }
    }

    intent.putExtra(Event.ATTRIBUTE_LOCATION, locationData);
  }
  
  /**
   * @see android.app.Service#onBind(Intent)
   */
  @Override
  public IBinder onBind(Intent intent) {
    // Client binding not supported for this service
    return null;
  }
}