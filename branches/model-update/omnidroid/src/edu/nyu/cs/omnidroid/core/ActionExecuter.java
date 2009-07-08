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

import java.util.List;

import edu.nyu.cs.omnidroid.util.ExceptionMessageMap;
import edu.nyu.cs.omnidroid.util.OmLogger;
import edu.nyu.cs.omnidroid.util.OmnidroidException;

import android.content.Context;
import android.util.Log;

/**
 * The ActionExecuter is the engine which will execute all actions.
 */
public class ActionExecuter {
  /**
   * Execute a list of actions in the given context.
   * 
   * @param context
   *          Context in which actions will be executed
   * @param actions
   *          List of actions to be executed
   * @throws OmnidroidException
   */
  public static void executeActions(Context context, List<Action> actions) {
    for (Action action : actions) {
      try {
        if (action.getExecutionMethod().equals(Action.BY_ACTIVITY)) {
          context.startActivity(action.getIntent());
        } else if (action.getExecutionMethod().equals(Action.BY_SERVICE)) {
          context.startService(action.getIntent());
        } else if (action.getExecutionMethod().equals(Action.BY_BROADCAST)) {
          context.sendBroadcast(action.getIntent());
        } else {
          // Illegal Action execution method.
          throw new OmnidroidException(120001, ExceptionMessageMap.getMessage(new Integer(120001)
              .toString()));
        }
      } catch (Exception e) {
        Log.i("Action Executer:", e.toString());
        Log.i("Action Executer: ", e.getLocalizedMessage());
        OmLogger.write(context, "Failed to execute action: " + action.getActionName());
      }
    }
  }

}
