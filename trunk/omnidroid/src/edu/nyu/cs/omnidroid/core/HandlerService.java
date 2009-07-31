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
import android.util.Log;
import edu.nyu.cs.omnidroid.model.CoreActionsDbHelper;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.OmLogger;
import edu.nyu.cs.omnidroid.util.OmnidroidException;

/**
 * This class is the heart of OmniDroid. When this class receives a system intent from
 * {@link BCReceiver} it calls {@link IntentParser} to create an {@link Event} if it is supported by
 * OmniDroid. The event is passed to the {@link RuleProcessor} to see if the event's attributes are
 * matched by the parameters of the user's defined {@link Rule}. The {@link Action}(s) of any rules
 * that match are passed to ActionExecuter where they are packaged into system intents and run.
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
    CoreActionsDbHelper coreActionsDbHelper = new CoreActionsDbHelper(this);
    ArrayList<Action> actions = RuleProcessor.getActions(coreActionsDbHelper, event);
    // Execute the list of actions.
    try {
      ActionExecuter.executeActions(this, actions);
    } catch (OmnidroidException e) {
      Log.w("Handler Service:", e.toString(), e);
      Log.w("Handler Service: ", e.getLocalizedMessage());
      OmLogger.write(this, "Illegal Execution Method");
    }
    // TODO(londinop): Log events/actions to a database or content provider
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