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
package edu.nyu.cs.omnidroid.app.controller;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import edu.nyu.cs.omnidroid.app.controller.util.Logger;
import edu.nyu.cs.omnidroid.app.controller.util.OmnidroidException;
import edu.nyu.cs.omnidroid.app.model.CoreActionsDbHelper;
import edu.nyu.cs.omnidroid.app.model.CoreRulesDbHelper;
import edu.nyu.cs.omnidroid.app.model.ActionLog;
import edu.nyu.cs.omnidroid.app.model.EventLog;
import edu.nyu.cs.omnidroid.app.model.GeneralLog;

/**
 * This class is the heart of OmniDroid. When this class receives a system intent from
 * {@link BCReceiver} it calls {@link IntentParser} to create an {@link Event} if it is supported by
 * OmniDroid. The event is passed to the {@link RuleProcessor} to see if the event's attributes are
 * matched by the parameters of the user's defined {@link Rule}. The {@link Action}(s) of any rules
 * that match are passed to ActionExecuter where they are packaged into system intents and run.
 */
public class HandlerService extends Service {

  private static final String TAG = HandlerService.class.getSimpleName();

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
      // Log event
      EventLog logEvent = new EventLog(this, event);
      Long logID = logEvent.insert();
      logEvent.setID(logID);
      
      // Open up Rule/Action Database connections
      CoreRulesDbHelper coreRuleDbHelper = new CoreRulesDbHelper(this);
      CoreActionsDbHelper coreActionsDbHelper = new CoreActionsDbHelper(this);

      // Get a list of actions that apply to this event.
      ArrayList<Action> actions = RuleProcessor.getActions(event, coreRuleDbHelper,
          coreActionsDbHelper);
      
      // Close Rule/Action Database connections
      coreActionsDbHelper.close();
      coreRuleDbHelper.close();
      
      // TODO(acase): Consider moving this to the Action Executor so we don't have to loop through
      //              the actions twice. The problem is that we don't have access to the logEvent
      //              there.
      for (Action action : actions) {
        // Log action
        ActionLog logAction = new ActionLog(this, action, logEvent.getID());
        logAction.insert();
      }

      GeneralLog generalLog = new GeneralLog(this, TAG + " got " + actions.size() + " action(s) for event " +
          intent.getAction());
      generalLog.insert();
      Logger.d(TAG, "get " + actions.size() + " action(s) for event " +
          intent.getAction());
      // Execute the list of actions.
      try {
        ActionExecuter.executeActions(this, actions);
      } catch (OmnidroidException e) {
        Logger.w(TAG, e.toString(), e);
        Logger.w(TAG, e.getLocalizedMessage());
        Logger.w(TAG, "Illegal Execution Method");
      }
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