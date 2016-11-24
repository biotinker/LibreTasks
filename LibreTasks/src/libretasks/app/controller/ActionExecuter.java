/*  
 * Copyright (c) 2016  LibreTasks - https://github.com/biotinker/LibreTasks  
 *  
 *  This file is free software: you may copy, redistribute and/or modify it  
 *  under the terms of the GNU General Public License as published by the  
 *  Free Software Foundation, either version 3 of the License, or (at your  
 *  option) any later version.  
 *  
 *  This file is distributed in the hope that it will be useful, but  
 *  WITHOUT ANY WARRANTY; without even the implied warranty of  
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  
 *  General Public License for more details.  
 *  
 *  You should have received a copy of the GNU General Public License  
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 *  
 * This file incorporates work covered by the following copyright and  
 * permission notice:  
 /*******************************************************************************
 * Copyright 2009 Omnidroid - http://code.google.com/p/omnidroid 
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
package libretasks.app.controller;

import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Context;
import libretasks.app.controller.util.ExceptionMessageMap;
import libretasks.app.controller.util.Logger;
import libretasks.app.controller.util.OmnidroidException;

/**
 * The ActionExecuter is the engine which will execute all actions.
 */
public class ActionExecuter {
  private static final String TAG = ActionExecuter.class.getSimpleName();

  /**
   * Execute a list of actions in the given context.
   * 
   * @param context
   *          Context in which actions will be executed
   * @param actions
   *          List of actions to be executed
   * @throws OmnidroidException
   *           if an illegal execution method is specified
   */
  public static void executeActions(Context context, List<Action> actions)
      throws OmnidroidException {
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
              .toString())
              + action.getActionName());
        }
      } catch (SecurityException e) {
        // Omnidroid does not have permission to perform this action
        Logger.w(TAG, e.toString(), e);
        Logger.w(TAG, e.getLocalizedMessage());
        Logger.w(TAG, "No permissions to perform this action: " + action.getActionName());
      } catch (ActivityNotFoundException e) {
        // No activity found to perform this action
        Logger.w(TAG, e.toString(), e);
        Logger.w(TAG, e.getLocalizedMessage());
        Logger.w(TAG, "No activity found to perform this action: " + action.getActionName());
      }      
    }
  }

}
