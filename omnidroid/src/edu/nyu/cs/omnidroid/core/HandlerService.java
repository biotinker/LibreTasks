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

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import edu.nyu.cs.omnidroid.util.AGParser;

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
   * @see android.app.Service#onCreate()
   */
  @Override
  public void onCreate() {
    super.onCreate();
  }

  /**
   * Gets the event type from the intent, checks it against defined rules, and launches any
   * triggered actions
   * 
   * @see android.app.Service#onStart(Intent, int)
   */
  @Override
  public void onStart(Intent intent, int id) {
    Event event = IntentParser.getEvent(intent);
    ArrayList<Action> actions = RuleProcessor.getActions(event);
    // TODO(londinop): Integrate with action launcher code from Rutvij
    // TODO(londinop): Log events/actions to a database or content provider
    sendIntent("SMS", "SMS_SEND", event.getAttribute(SMSReceivedEvent.ATTRIB_PHONE_NO), event
        .getAttribute(SMSReceivedEvent.ATTRIB_MESSAGE_TEXT));
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
   * @see android.app.Service#onBind(Intent)
   */
  @Override
  public IBinder onBind(Intent intent) {
    // Client binding not supported for this service
    return null;
  }
}