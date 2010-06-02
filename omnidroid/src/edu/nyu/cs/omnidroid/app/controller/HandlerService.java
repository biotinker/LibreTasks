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
package edu.nyu.cs.omnidroid.app.controller;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import edu.nyu.cs.omnidroid.app.controller.bkgservice.BCReceiver;
import edu.nyu.cs.omnidroid.app.controller.util.OmLogger;
import edu.nyu.cs.omnidroid.app.controller.util.OmnidroidException;
import edu.nyu.cs.omnidroid.app.model.CoreActionsDbHelper;
import edu.nyu.cs.omnidroid.app.model.CoreRuleDbHelper;

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

    if (event != null) {
      CoreRuleDbHelper coreRuleDbHelper = new CoreRuleDbHelper(this);
      CoreActionsDbHelper coreActionsDbHelper = new CoreActionsDbHelper(this);
      ArrayList<Action> actions = RuleProcessor.getActions(event, coreRuleDbHelper,
          coreActionsDbHelper);
      Log.d("HandlerService", "get " + actions.size() + " action(s) for event " + intent.getAction());
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
    
    stopSelf();
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