package edu.nyu.cs.omnidroid.core;

import java.util.ArrayList;

/**
 * This class represents a user defined rule. It consists of the name of the event that will trigger
 * the rule, the filters that apply to the event, and the actions that will be performed if the
 * event matches the rule.
 */
public class Rule {

  /** Rule parameters */
  String ruleName;
  String eventAppName;
  String[] filterTypes;
  String[] filterData;
  // TODO(londinop): support multiple actions in a rule and multiple parameters/rule
  String actionAppName;
  String[] actionParameterNames;
  String[] actionParameterData;

  /**
   * Constructs a rule from all rule parameters
   * 
   * @param ruleName
   *          user-defined name of the rule
   * @param eventAppName
   *          event that triggers this rule
   * @param filterTypes
   *          OmniDroid data types of the filters associated with this rule
   * @param filterData
   *          user-defined data for the filters associated with this rule
   * @param actionAppName
   *          the action to perform on a match for this rule
   * @param actionParameterNames
   *          field names of the parameters for the action to perform
   * @param actionParameterData
   *          parameter data for the action to perform
   */
  public Rule(String ruleName, String eventAppName, String[] filterTypes, String[] filterData,
      String actionAppName, String[] actionParameterNames, String[] actionParameterData) {
    this.ruleName = ruleName;
    this.eventAppName = eventAppName;
    this.filterTypes = filterTypes;
    this.filterData = filterData;
    this.actionAppName = actionAppName;
    this.actionParameterNames = actionParameterNames;
    this.actionParameterData = actionParameterData;
  }

  /**
   * Checks the event against any filters associated with this rule
   * 
   * @param event
   *          the event that triggered this rule
   * @return true if all rule filters match this event, false otherwise
   */
  public boolean checkFilters(Event event) {
    // TODO(londinop): Support "and/or" structure of filters
    for (int i = 0; i < filterTypes.length; i++) {
      if (!event.getAttribute(filterTypes[i]).equals(filterData[i])) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns the actions associated with this rule. Populates the action parameter fields, which may
   * require retrieving them from the event
   * 
   * @param event
   *          the event which triggered this rule and may contain data for the action parameters
   * @return the action(s) fired by this rule
   */
  public ArrayList<Action> getActions(Event event) {
    // Fill in action parameter names
    /*
     * TODO(londinop): We need a method for distinguishing variables we need to look up in the event
     * data from predefined parameters stored as part of the rule
     */
    ArrayList<Action> actions = new ArrayList<Action>();

    for (int i = 0; i < actionParameterNames.length; i++) {
      // replace with action parameter fetching code
    }
    // Temporary code to simulate returning an action
    Action action = new Action();
    actions.add(action);
    return actions;
  }
}
