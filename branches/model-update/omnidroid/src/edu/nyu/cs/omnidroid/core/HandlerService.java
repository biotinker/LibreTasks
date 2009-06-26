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
package edu.nyu.cs.omnidroid.core;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.CallLog;
import android.util.Log;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.OmLogger;
import edu.nyu.cs.omnidroid.util.StringMap;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * This class is the heart of OmniDroid. It picks up the intent from the event received by the
 * broadcast receiver and compares the event action to the rules in the user configuration data
 * file. If there is a match for the event action and any user filters, any parameters needed to for
 * an action are retrieved from the event's content provider and an action is launched via a
 * broadcast intent.<br>
 * TODO(londinop): Add test classes and error handling
 */
public class HandlerService extends Service {

  /**
   * Describes the event that triggered this intent.
   */
  private String eventAction;

  /**
   * Location of the attributes associated with this event.
   */
  private Uri eventAttributesUri;

  /**
   * @see android.app.Service#onCreate()
   */
  @Override
  public void onCreate() {
    super.onCreate();
  }

  /**
   * Gets the event name and attributes URI from the received intent and processes the event
   * 
   * @param intent
   *          the intent that started this service
   * @see android.app.Service#onStart(Intent, int)
   */
  @Override
  public void onStart(Intent intent, int id) {
    eventAttributesUri = getEventAction(intent);
    processEvent();
    this.stopSelf();
  }

  /**
   * Gets the event that started this service and the URI to its data. <br>
   * TODO(londinop): Event names from intents shouldn't be hard coded into our core service
   * 
   * @param intent
   *          the intent describing the event to be handled
   * @return a URI to the content provider of the data associated with the event
   */
  private Uri getEventAction(Intent intent) {
    Uri uri;

    if (intent.getAction().contains("SMS_RECEIVED")) {
      // Handle SMS event
      eventAction = "SMS_RECEIVED";
      uri = Uri.parse("content://sms/inbox");
      uri = ContentUris.withAppendedId(uri, getLastId(uri));
    } else if (intent.getAction().contains("PHONE_STATE")) {
      // Handle phone state event
      eventAction = "PHONE_STATE";
      uri = CallLog.Calls.CONTENT_URI;
      uri = ContentUris.withAppendedId(uri, getLastId(uri));
    } else {
      // Handle other event
      eventAction = intent.getAction();
      Bundle b = intent.getExtras();
      uri = (Uri) b.get("uri");
    }
    return uri;
  }

  /**
   * Retrieves all rules from the user configuration file that match the received event and any
   * filters associated with it, and sends out an action intent where applicable. <br>
   * TODO(londinop): Replace with code that retrieves rule info from the database instead.
   * 
   * @return true if at least one rule was a match and resulted in an action, false otherwise
   */
  private boolean processEvent() {
    UGParser userConfig = new UGParser(getApplicationContext());

    // Check if any rules match the event received
    ArrayList<HashMap<String, String>> matchingRules = userConfig.readbyEventName(eventAction);
    if (matchingRules.size() == 0) {
      return false;
    }

    boolean processedRule = false;

    // Process all matching rules
    for (HashMap<String, String> currentRule : matchingRules) {
      // Get rule parameters
      if (currentRule.get(UGParser.KEY_ENABLE_INSTANCE).equalsIgnoreCase("True")) {
        String eventApp = currentRule.get(UGParser.KEY_EVENT_APP);
        String filterType = currentRule.get(UGParser.KEY_FILTER_TYPE);
        String filterData = currentRule.get(UGParser.KEY_FILTER_DATA);
        String actionName = currentRule.get(UGParser.KEY_ACTION_TYPE);
        String actionApp = currentRule.get(UGParser.KEY_ACTION_APP);
        String actionParam1 = currentRule.get(UGParser.KEY_ACTION_DATA1);
        String actionParam2 = currentRule.get(UGParser.KEY_ACTION_DATA2);

        if (matchesFilter(filterType, filterData)) {
          // Fill in any of the action's dynamic data fields (e.g. sender phone no, message text)
          // if the action data field contains only the data type and not the actual URI to the data
          try {
            if (!actionParam1.contains("content://") && !actionParam1.equals("")) {
              actionParam1 = getActionData(actionParam1, eventApp);
            }
            if (!actionParam2.contains("content://") && !actionParam2.equals("")) {
              actionParam2 = getActionData(actionParam2, eventApp);
            }
          } catch (Exception e) {
            OmLogger.write(getApplicationContext(), "Unable to retrieve information from thrower");
          }

          sendIntent(actionApp, actionName, actionParam1, actionParam2);
          processedRule = true;
        }
      }
    }
    return processedRule;
  }

  /**
   * Gets data for dynamic fields (such as sender phone number, text message body, etc.) from an
   * external content provider specified by the event attributes URI.
   * 
   * @param actionDataType
   *          description of the data to find in the content provider
   * @param eventApp
   *          name of the event application as described in the user configuration file
   * @return the value of the data specified by actionDataType from the external content provider
   */
  private String getActionData(String actionDataType, String eventApp) {
    // Parse the application configuration file to get the field name associated with this action's
    // data type
    AGParser appConfig = new AGParser(getApplicationContext());
    ArrayList<StringMap> contentMaps = appConfig.readContentMap(eventApp);

    String actionData = null;
    for (StringMap contentMap : contentMaps) {
      if (contentMap.get(1).equalsIgnoreCase(actionDataType)) {
        // Query event's content provider for data
        Cursor cur = getContentResolver().query(eventAttributesUri, null, null, null, null);
        cur.moveToFirst();
        String dataColumnName = contentMap.getKey();
        actionData = cur.getString(cur.getColumnIndex(dataColumnName));
        break;
      }
    }

    ContentValues values = new ContentValues();
    values.put("i_name", "temp");
    values.put("a_data", actionData);

    Uri dataUri = getContentResolver().insert(CP.CONTENT_URI, values);
    return dataUri.toString();
  }

  /**
   * Tests the data from the received event against a user specified filter
   * 
   * @param filterType
   *          the data field to filter from user defined filter
   * @param filterData
   *          the data to filter from user defined filter
   * @return true if event passes the filter, false otherwise
   */
  public boolean matchesFilter(String filterType, String filterData) {
    if (filterData.equalsIgnoreCase(null) || filterData.equalsIgnoreCase("")
        || eventAction.contains("PHONE_STATE")) {
      return true;
    } else {
      Cursor cur = getContentResolver().query(eventAttributesUri, null, null, null, null);
      cur.moveToFirst();

      String filter = cur.getString(cur.getColumnIndex(filterType));
      if (filterData.equalsIgnoreCase(filter)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Sends out a broadcast intent to trigger the action application associated with the event.<br>
   * TODO(londinop): replace individual parameter variables with an array of action parameters from
   * database
   * 
   * @param actionApp
   *          application triggered by the event received which is used to get the class name to
   *          launch via the intent
   * @param actionName
   *          name of the action as defined in the user configuration file
   * @param actionParam1
   *          URI to the OmniDroid content provider for the first action parameter
   * @param actionParam2
   *          URI to the OmniDroid content provider for the second action parameter
   */
  public void sendIntent(String actionApp, String actionName, String actionParam1,
      String actionParam2) {
    Intent actionIntent = new Intent();
    actionIntent.setAction(actionName);
    // TODO(londinop): Change URI strings to constants and update receiver classes
    actionIntent.putExtra("uri", actionParam1);
    actionIntent.putExtra("uri2", actionParam2);

    AGParser appConfig = new AGParser(getApplicationContext());
    String packageName = appConfig.readPkgName(actionApp);
    String listener = appConfig.readListenerClass(actionApp);

    if (!packageName.equalsIgnoreCase("")) {
      ComponentName comp = new ComponentName(packageName, listener);
      actionIntent.setComponent(comp);
    }
    // TODO(londinop): Handle this exception
    try {
      sendBroadcast(actionIntent);
    } catch (Exception e) {
      e.toString();
    }
  }

  /**
   * Retrieve the id of the latest updated row of the content provider <br>
   * 
   * @param uri
   *          the URI of the content provider
   * @return the id of the last row in the content provider, -1 if an exception
   */
  private long getLastId(Uri uri) {
    // Sleep to ensure that the data gets to the system database before we try to access it
    // TODO(londinop) This is a temporary workaround to issue 12. A more reliable method of
    // retrieving event attributes should be used.
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      Log.i("getLastId", "Couldn't sleep");
      return -1;
    }

    Cursor cur = getContentResolver().query(uri, null, null, null, null);

    if (cur.moveToFirst()) {
      return cur.getLong(cur.getColumnIndex("_id"));
    } else {
      Log.i("getLastId", "Couldn't retrieve data from content provider.");
      return -1;
    }
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