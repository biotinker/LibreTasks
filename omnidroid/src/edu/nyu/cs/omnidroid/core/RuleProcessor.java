package edu.nyu.cs.omnidroid.core;

import java.util.ArrayList;

import edu.nyu.cs.omnidroid.tests.TestData;

public class RuleProcessor {

  private RuleProcessor() { }

  /**
   * Gets a list of rules that are triggered by this event and compares the event attributes with
   * the filters defined for each rule. The actions of all rules that pass the filter check are
   * returned in a list.
   * 
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
