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

import edu.nyu.cs.omnidroid.tests.TestData;

/**
 * Gets a list of rules that are triggered by this event and compares the event attributes with the
 * filters defined for each rule. The actions of all rules that pass the filter check are returned
 * in a list.
 */
public class RuleProcessor {

  /**
   * This is a static utility class which cannot be instantiated.
   */
  private RuleProcessor() {
  }

  /**
   * Gets a list of rules that are triggered by the given event and compares the event attributes
   * with the filters defined for each rule. The actions of all rules that pass the filter check are
   * returned in a list.
   * 
   * @param event
   *          the event that will be compared to all defined user rules
   * @return the list of actions to be performed based on the rules triggered by this event
   */
  public static ArrayList<Action> getActions(Event event) {
    // TODO(londinop): replace with database access code from Dmitriy
    ArrayList<Rule> rules = TestData.getRulesForEvent(event.getName());
    ArrayList<Action> actions = new ArrayList<Action>();

    for (Rule currentRule : rules) {
      if (currentRule.checkFilters(event)) {
        actions.addAll(currentRule.getActions(event));
      }
    }
    return actions;
  }
}
