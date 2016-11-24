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
package libretasks.app.controller;

import java.util.ArrayList;

import libretasks.app.controller.util.Logger;
import libretasks.app.model.CoreActionsDbHelper;
import libretasks.app.model.CoreRulesDbHelper;

/**
 * Gets the {@link Rule}(s) triggered by this {@link Event} and compares the event attributes with
 * the {@link Filter}(s) defined for each rule. Returns the {@link Action}(s) to execute if this
 * rule matches the event.
 */
public class RuleProcessor {
  private static final String TAG = RuleProcessor.class.getSimpleName();

  /**
   * This is a static utility class which cannot be instantiated.
   */
  private RuleProcessor() {
  }

  /**
   * Gets the {@link Rule}(s) triggered by this {@link Event} and compares the event attributes with
   * the {@link Filter}(s) defined for each rule. Returns the {@link Action}(s) to execute if this
   * rule matches the event.
   * 
   * @param coreActionsDbHelper
   *          The helper class to get actions data from database
   * @param event
   *          the event that will be compared to all defined user rules
   * @return the list of actions to be performed based on the rules triggered by this event
   */
  public static ArrayList<Action> getActions(Event event, CoreRulesDbHelper coreRuleDbHelper,
      CoreActionsDbHelper coreActionsDbHelper) {

    ArrayList<Rule> rules = coreRuleDbHelper.getRulesMatchingEvent(event.getAppName(), event
        .getEventName());
    Logger.d(TAG, "get " + rules.size() + " rule(s) for event " + event.getEventName()
        + " from App " + event.getAppName());

    ArrayList<Action> actions = new ArrayList<Action>();
    for (Rule currentRule : rules) {
      if (currentRule.passesFilters(event)) {
        actions.addAll(currentRule.getActions(coreActionsDbHelper, event)) ;
      }
    }
    Logger.d(TAG, "get " + actions.size() + " action(s) for event " + event.getEventName()
        + " from App " + event.getAppName());
    return actions;
  }
}
