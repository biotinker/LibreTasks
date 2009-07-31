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
import edu.nyu.cs.omnidroid.model.CoreActionsDbHelper;

/**
 * This class represents a user defined rule. It consists of the name of the {@link Event} that will
 * trigger the rule, the filters that apply to the event, and the actions that will be performed if
 * the event matches the rule.
 */
public class Rule {

  /** Rule parameters */
  public final String ruleName;
  public final String eventAppName;
  public final ArrayList<Filter> filters;
  private long ruleId;

  /**
   * Constructs a rule from all rule parameters
   * 
   * @param ruleName
   *          user-defined name of the rule
   * @param eventAppName
   *          event that triggers this rule
   * @param filters
   *          any filters on the event attributes, can be null if there are no filters
   * @param actions
   *          the actions that are triggered by this event
   * @throws IllegalArgumentException
   *           if required parameters are null or if there are no actions associated with this rule
   */
  public Rule(String ruleName, String eventAppName, ArrayList<Filter> filters,
      ArrayList<Action> actions) throws IllegalArgumentException {

    if (ruleName == null) {
      throw new IllegalArgumentException("ruleName cannot be null");
    } else if (eventAppName == null) {
      throw new IllegalArgumentException("eventAppName cannot be null");
    } else if (actions.size() < 1) {
      throw new IllegalArgumentException("must provide at least one action");
    }
    this.ruleName = ruleName;
    this.eventAppName = eventAppName;
    this.filters = filters;
  }

  /**
   * Matches the event to all filters associated with this rule
   * 
   * @param event
   *          the event that triggered this rule
   * @return true if this event passes all rule filters, false otherwise
   */
  public boolean matchesEvent(Event event) {
    // TODO(londinop): Support "and/or" structure of filters
    for (Filter currentFilter : filters) {
      if (!event.getAttribute(currentFilter.type).equals(currentFilter.data)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns the actions associated with this rule. Populates the action parameter fields, which may
   * require retrieving them from the event
   * 
   * @param coreActionsDbHelper
   *          The helper class to get actions data from database
   * @param event
   *          the event which triggered this rule and may contain data for the action parameters
   * @return the action(s) fired by this rule
   */
  public ArrayList<Action> getActions(CoreActionsDbHelper coreActionsDbHelper, Event event) {
    // Get actions arraylist for this rule
    ArrayList<Action> actionsList = coreActionsDbHelper.getActions(ruleId, event);
    coreActionsDbHelper.close();
    return actionsList;
  }
}
