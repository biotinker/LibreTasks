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
import edu.nyu.cs.omnidroid.util.OmnidroidException;

/**
 * Gets the {@link Rule}(s) triggered by this {@link Event} and compares the event attributes with
 * the {@link Filter}(s) defined for each rule. Returns the {@link Action}(s) to execute if this
 * rule matches the event.
 */
public class RuleProcessor {

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
   * @param event
   *          the event that will be compared to all defined user rules
   * @return the list of actions to be performed based on the rules triggered by this event
   */
  public static ArrayList<Action> getActions(Event event) {
    // TODO(londinop): replace with database access code from Dmitriy
    ArrayList<Rule> rules = null;
    try {
      rules = TestData.getRulesForEvent(event.getName());
    } catch (OmnidroidException e) {
      // TODO Add log message.
      e.printStackTrace();
    }
    ArrayList<Action> actions = new ArrayList<Action>();

    for (Rule currentRule : rules) {
      if (currentRule.matchesEvent(event)) {
        actions.addAll(currentRule.getActions(event));
      }
    }
    return actions;
  }
}
