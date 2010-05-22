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

package edu.nyu.cs.omnidroid.app.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.nyu.cs.omnidroid.app.core.datatypes.OmniPhoneNumber;
import edu.nyu.cs.omnidroid.app.core.datatypes.OmniText;
import edu.nyu.cs.omnidroid.app.model.CursorHelper;
import edu.nyu.cs.omnidroid.app.model.db.DataFilterDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.DataTypeDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.RegisteredActionDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.RegisteredActionParameterDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.RegisteredAppDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.RegisteredEventAttributeDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.RegisteredEventDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.RuleActionDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.RuleActionParameterDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.RuleDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.RuleFilterDbAdapter;
import edu.nyu.cs.omnidroid.app.util.OmnidroidException;
import edu.nyu.cs.omnidroid.app.util.Tree;

/**
 * This class populates the database with generated {@link Rule} objects and their associated
 * {@link Filter} trees and {@link Action} lists, for the purposes of testing core classes without
 * having to generate rules with the UI. It can also be used statically to generate single Rule
 * objects or a list of all Rule objects.
 */
public class RuleTestData {

  /** Mock rules and filters in this class */
  public final static int RULE_DO_NOT_DISTURB = 0;
  public final static int RULE_SAY_HELLO1 = 1;
  public final static int RULE_SAY_HELLO2 = 2;
  public final static int RULE_SAY_HELLO3 = 3;

  public final static int FILTER_SMSPHONE1 = 0;
  public final static int FILTER_SMSPHONE2 = 1;
  public final static int FILTER_SMSTEXT1 = 2;
  public final static int FILTER_SMSTEXT2 = 3;

  public final static int ACTION_DND = 0;
  public static final int ACTION_SAYHELLO = 1;
  public static final int ACTION_DND2 = 2;

  /**
   * The rule data used to build an array of {@link Rule} objects and populate the database,
   * consisting of the following fields: Event Name, Rule Name, Rule Description, Filters, Actions.
   * The rule table contains a string that can be parsed to build a tree of filters. The syntax is
   * <b><i>R:Child-...-Child,Parent:Child-...-Child, ...</i></b> where a parent value of <b>R</b>
   * represents the root, and filters are integers that correspond to entries in the filters array.
   * A node must be created as a child first before it can be used as another node's parent value.
   * <p>
   * TODO(londinop): The filter tree string should support re-using the same filter node as a child
   */
  private static final String[][] ruleData = {
      // Event Name, Rule Name, Rule Description, Filters, Actions
      { SMSReceivedEvent.APPLICATION_NAME, SMSReceivedEvent.EVENT_NAME, "Do Not Disturb",
          "Reply to text message with Do not disturb", "", "0" },
      { SMSReceivedEvent.APPLICATION_NAME, SMSReceivedEvent.EVENT_NAME, "Say Hello1",
          "Reply with hello to a specific number", "R:0", "0" },
      { SMSReceivedEvent.APPLICATION_NAME, SMSReceivedEvent.EVENT_NAME, "Say Hello2",
          "Reply with hello to either of two numbers", "R:0-1", "1" },
      { SMSReceivedEvent.APPLICATION_NAME, SMSReceivedEvent.EVENT_NAME, "Say Hello3",
          "Reply with hello to either of two numbers with specific text", "R:0-1,0:2,1:3", "1,2" } 
  };

  /**
   * The filter data used to build a {@link Filter} tree which is stored in each {@link Rule},
   * consisting of the following fields: Data Type Name, Event Attribute Name, Comparison, Filter
   * Data.
   */
  private static final String[][] filterData = {
      // EventAttributeName, FilterOnDataType, Comparison, CompareWithDataTypeName, Filter Data
      { SMSReceivedEvent.ATTRIB_PHONE_NO, 
        OmniPhoneNumber.class.getName(), OmniPhoneNumber.Filter.EQUALS.toString(), 
        OmniPhoneNumber.class.getName(), "555-555-5555" },
        
      { SMSReceivedEvent.ATTRIB_PHONE_NO, 
        OmniPhoneNumber.class.getName(), OmniPhoneNumber.Filter.EQUALS.toString(), 
        OmniPhoneNumber.class.getName(), "123-456-7890" },
        
      { SMSReceivedEvent.ATTRIB_MESSAGE_TEXT, 
        OmniText.class.getName(), OmniText.Filter.CONTAINS.toString(), 
        OmniText.class.getName(), "Some Text" },
      
      { SMSReceivedEvent.ATTRIB_MESSAGE_TEXT, 
        OmniText.class.getName(), OmniText.Filter.CONTAINS.toString(), 
        OmniText.class.getName(), "Some Other Text" } };

  private static final String[][] actionData = {
      // Action Application, Action Parameter Name, Action Parameter Data
      { SendSmsAction.APP_NAME, SendSmsAction.ACTION_NAME, SendSmsAction.PARAM_PHONE_NO,
          "<" + SMSReceivedEvent.ATTRIB_PHONE_NO + ">", SendSmsAction.PARAM_SMS, "Do not disturb" },
      { SendSmsAction.APP_NAME, SendSmsAction.ACTION_NAME, SendSmsAction.PARAM_PHONE_NO,
          "<" + SMSReceivedEvent.ATTRIB_PHONE_NO + ">", SendSmsAction.PARAM_SMS, "Hello!" },
      { SendSmsAction.APP_NAME, SendSmsAction.ACTION_NAME, SendSmsAction.PARAM_PHONE_NO,
          "555-555-5555", SendSmsAction.PARAM_SMS, "Do not disturb" }, };
  /**
   * List of rule objects build from ruleData array
   */
  private static ArrayList<Rule> rules;

  private static RegisteredAppDbAdapter applicationDbAdapter;
  private static RegisteredEventDbAdapter eventDbAdapter;
  private static RegisteredEventAttributeDbAdapter eventAttributeDbAdapter;
  private static RuleDbAdapter ruleDbAdapter;
  private static RuleFilterDbAdapter ruleFilterDbAdapter;
  private static DataFilterDbAdapter filterComparisonDbAdapter;
  private static DataTypeDbAdapter filterDataTypeDbAdapter;
  private static RegisteredActionDbAdapter actionDbAdapter;
  private static RegisteredActionParameterDbAdapter actionParameterDbAdapter;
  private static RuleActionDbAdapter ruleActionDbAdapter;
  private static RuleActionParameterDbAdapter ruleActionParameterDbAdapter;

  private static final int appNameCol = 0;
  private static final int eventNameCol = 1;
  private static final int ruleNameCol = 2;
  private static final int ruleDescCol = 3;
  private static final int filterTreeCol = 4;
  private static final int actionsCol = 5;

  private static final int filterEventAttribCol = 0;
  private static final int filterOnDataTypeCol = 1;
  private static final int filterComparisonCol = 2;
  private static final int filterCompareTypeCol = 3;
  private static final int filterCompareDataCol = 4;

  private static final int actionAppCol = 0;
  private static final int actionNameCol = 1;
  private static final int actionParam1Col = 2;
  private static final int actionParam1DataCol = 3;
  private static final int actionParam2Col = 4;
  private static final int actionParam2DataCol = 5;

  private static final int root = -1;

  private static long eventID;
  private static long ruleID;

  /**
   * Private constructor - class cannot be instantiated
   */
  private RuleTestData() {
  }

  /**
   * Returns the {@link Rule} representation of the rules populated in the database
   * 
   * @return a Rule that contains its ruleID from the database
   */
  public static ArrayList<Rule> getRules() {
    if (rules == null) {
      return null;
    } else
      return rules;
  }

  /**
   * Returns a specific {@link Rule} representation of a rules populated in the database
   * 
   * @param ruleNumber
   *          the rule number in the rule data array
   * @return a Rule that contains its ruleID from the database
   */
  public static Rule getRule(int ruleNumber) {
    if (rules == null) {
      return null;
    } else
      return rules.get(ruleNumber);
  }

  /**
   * Builds and returns a {@link Filter} from the filter data array
   * 
   * @param filterNumber
   *          number of the filter in the filter data array
   * @return a Filter built from the filter data array
   */
  public static Filter getFilter(int filterNumber) {
    return buildFilter(filterNumber);
  }

  /**
   * Builds an action from the action data array and populates its variable parameter fields with
   * data from the event attributes
   * 
   * @param actionNumber
   *          the number of the action in the action data array
   * @param event
   *          the event used to fill in action parameters from event attributes
   * @return an Action with its variable parameters populated
   */
  public static Action getAction(int actionNumber, Event event) {
    return buildAction(actionNumber, event);
  }

  /**
   * Populates the Rules, RuleFilters, RuleActions, and RuleActionParameters tables of the OmniDroid
   * database with information from the rule data array. Also creates a list of {@link Rule} objects
   * with the same information.
   */
  public static void prePopulateDatabase(SQLiteDatabase database) {
    applicationDbAdapter = new RegisteredAppDbAdapter(database);
    eventDbAdapter = new RegisteredEventDbAdapter(database);
    eventAttributeDbAdapter = new RegisteredEventAttributeDbAdapter(database);
    filterComparisonDbAdapter = new DataFilterDbAdapter(database);
    filterDataTypeDbAdapter = new DataTypeDbAdapter(database);
    actionDbAdapter = new RegisteredActionDbAdapter(database);
    actionParameterDbAdapter = new RegisteredActionParameterDbAdapter(database);
    
    ruleDbAdapter = new RuleDbAdapter(database);
    ruleFilterDbAdapter = new RuleFilterDbAdapter(database);
    ruleActionDbAdapter = new RuleActionDbAdapter(database);
    ruleActionParameterDbAdapter = new RuleActionParameterDbAdapter(database);

    buildRules();
    
    clearRuleDbData();
    
    // Populate the database with data from the Rule array
    for (int i = 0; i < rules.size(); i++) {

      // Use the appName and eventName to find the unique event id in the database
      Cursor cursor = applicationDbAdapter.fetchAll(ruleData[i][appNameCol], null, true);
      cursor.moveToFirst();
      long appID = CursorHelper.getLongFromCursor(cursor, RegisteredAppDbAdapter.KEY_APPID);

      cursor = eventDbAdapter.fetchAll(ruleData[i][eventNameCol], appID);
      cursor.moveToFirst();
      eventID = CursorHelper.getLongFromCursor(cursor, RegisteredEventDbAdapter.KEY_EVENTID);

      String ruleDescription = ruleData[i][ruleDescCol];
      Rule currentRule = rules.get(i);

      addRuleToDatabase(currentRule, ruleDescription);

      String actionListString = ruleData[i][actionsCol];
      if (!actionListString.equals("")) {
        addActionsToDatabase(actionListString);
      }
    }
  }
  
  /**
   * delete all rule data from the db
   */
  private static void clearRuleDbData() {
    ruleDbAdapter.deleteAll();
    ruleFilterDbAdapter.deleteAll();
    ruleActionDbAdapter.deleteAll();
    ruleActionParameterDbAdapter.deleteAll();
  }

  /**
   * Builds a list of {@link Rule} objects from the information in the rule data String array
   * 
   * @return list of Rules created from the rule data array
   */
  private static ArrayList<Rule> buildRules() {
    if (rules != null) {
      return rules;
    }

    // Build a rule from each entry in the ruleData array
    rules = new ArrayList<Rule>();

    for (int i = 0; i < ruleData.length; i++) {
      rules.add(buildRule(i));
    }

    return rules;
  }

  /**
   * Builds a {@link Rule} from a row number in the rule data String array.
   * 
   * @param ruleNumber
   *          the number of the row in the rule data array
   * @return a Rule built from an array of rule parameters
   */
  private static Rule buildRule(int ruleNumber) {
    String[] currentRule = ruleData[ruleNumber];

    String ruleName = currentRule[ruleNameCol];
    Tree<Filter> filterTree = null;

    // Build a filter tree from a string representing the tree's
    // structure
    String filterTreeString = currentRule[filterTreeCol];
    if (filterTreeString != "") {
      filterTree = buildFilterTree(filterTreeString);
    }
    // This rule was never in the database so it doesn't have an ID
    long mockID = -1;
    return new Rule(ruleName, mockID, filterTree);
  }

  /**
   * Builds a {@link Filter} from a row number in the filter data String array.
   * 
   * @param filterNumber
   *          the number of the row in the filter data array
   * @return a Filter built from an array of filter parameters
   */
  private static Filter buildFilter(int filterNumber) {
    String[] currentFilter = filterData[filterNumber];

    return new Filter(currentFilter[filterEventAttribCol], currentFilter[filterOnDataTypeCol],
        currentFilter[filterComparisonCol], currentFilter[filterCompareTypeCol],
        currentFilter[filterCompareDataCol]);
  }

  /**
   * Builds a {@link Action} from a row number in the filter data String array.
   * 
   * @param actionNumber
   *          the number of the row in the action data array
   * @return an Action built from an array of action parameters
   */
  private static Action buildAction(int actionNumber, Event event) {
    String[] currentAction = actionData[actionNumber];

    HashMap<String, String> actionParams = new HashMap<String, String>();
    String actionParam1Data = extractData(currentAction[actionParam1DataCol], event);
    actionParams.put(currentAction[actionParam1Col], actionParam1Data);
    String actionParam2Data = extractData(currentAction[actionParam2DataCol], event);
    actionParams.put(currentAction[actionParam2Col], actionParam2Data);

    Action action;
    try {
      action = new SendSmsAction(actionParams);
    } catch (OmnidroidException e) {
      action = null;
    }
    return action;
  }

  private static String extractData(String paramData, Event event) {
    // Parameter data to be extracted from an event's attributes has the format "<attribute_name>"
    Pattern pattern = Pattern.compile("^<(.*)>$");
    Matcher matcher = pattern.matcher(paramData);
    final int EVENT_ATTRIB = 1; // Name of the event attribute in the pattern.
    String eventAttr;
    if (matcher.find()) {
      eventAttr = matcher.group(EVENT_ATTRIB);
      return event.getAttribute(eventAttr); // return value of the event attribute
    } else {
      return paramData; // return the value in parameter data
    }
  }

  private static void addActionsToDatabase(String actionListString) {

    StringBuffer itemNumber = new StringBuffer();
    for (int i = 0; i < actionListString.length(); i++) {
      char token = actionListString.charAt(i);
      switch (token) {
      case (','):
        int actionNumber = Integer.parseInt(itemNumber.toString());
        addActionToDatabase(actionNumber);

        // Clear buffer
        itemNumber.setLength(0);
        break;
      default:
        itemNumber.append(token);
      }
    }
    // Last item won't be followed by a comma
    int actionNumber = Integer.parseInt(itemNumber.toString());
    addActionToDatabase(actionNumber);
  }

  /**
   * Builds a {@link Tree} of {@link Filter} objects from a string which represents the tree
   * structure.
   * 
   * @param filterTreeString
   *          The format of the string must be <b><i>R:Child-...-Child,Parent:Child-...-Child,
   *          ...</i></b> where a parent value of <b>R</b> represents the root, and filters are
   *          integers that correspond to entries in the filters array. A node must be created as a
   *          child first before it can be used as another node's parent value.
   * @return a Tree of Filters with its hierarchical structure representing the "and/or"
   *         relationships between Filters, where each child is an "and" relationship and siblings
   *         are "or" relationships.
   */
  private static Tree<Filter> buildFilterTree(String filterTreeString) {
    // Keep track of the filter IDs associated with each node number in the parse string
    int parentNumber = root;
    HashMap<Integer, Tree<Filter>> nodes = new HashMap<Integer, Tree<Filter>>();

    Tree<Filter> rootNode = new Tree<Filter>(null, null);
    nodes.put(root, rootNode);

    StringBuffer nodeNumber = new StringBuffer();

    for (int i = 0; i < filterTreeString.length(); i++) {
      char token = filterTreeString.charAt(i);
      switch (token) {
      case (':'):
        // The value stored in the node buffer represents the parent
        // node of subsequent values
        String nodeString = nodeNumber.toString();
        if (nodeString.equals("R")) {
          parentNumber = root;
        } else {
          parentNumber = Integer.parseInt(nodeNumber.toString());
        }
        // Clear buffer
        nodeNumber.setLength(0);
        break;
      case ('-'):
      case (','):
        // The value stored in the node buffer is the row number of the
        // filter values in the filterData string array to use to create a new filter
        int filterNumber = Integer.parseInt(nodeNumber.toString());

        Tree<Filter> parent = nodes.get(parentNumber);
        nodes.put(filterNumber, parent.addChild(buildFilter(filterNumber)));

        nodeNumber.setLength(0);
        break;
      default:
        nodeNumber.append(token);
      }
    }
    // Create the last node since it won't be followed by a control symbol
    int filterNumber = Integer.parseInt(nodeNumber.toString());
    Tree<Filter> parent = nodes.get(parentNumber);
    parent.addChild(buildFilter(filterNumber));

    return rootNode;
  }

  /**
   * Inserts a single rule into Rules table in the OmniDroid database.
   * 
   * @param rule
   *          the Rule object to be inserted into the database
   * @param ruleDescription
   *          a description of the rule (not stored in the Rule object)
   */
  private static void addRuleToDatabase(Rule rule, String ruleDescription) {

    ruleID = ruleDbAdapter.insert(eventID, rule.ruleName, ruleDescription, true);
    rule.ruleID = ruleID;

    Tree<Filter> filterTree = rule.filterTree;

    if (rule.filterTree != null) {
      for (Tree<Filter> childNode : filterTree.getChildren()) {
        addFilterToDatabase(childNode, root);
      }
    }
  }

  /**
   * Inserts a filter and all of its sub-filters into RuleFilters table in the OmniDroid database.
   * 
   * @param filterNode
   *          top level filter node to be inserted into the database
   * @param parentID
   *          the filterID of the parent of the node being inserted
   */
  private static void addFilterToDatabase(Tree<Filter> filterNode, long parentID) {
    Filter filter = filterNode.getItem();

    // Get the id of the filter on data type
    Cursor cursor = filterDataTypeDbAdapter.fetchAll(null, filter.filterOnDataType);
    cursor.moveToFirst();
    long filterOnDataTypeID = CursorHelper.getLongFromCursor(cursor,
        DataTypeDbAdapter.KEY_DATATYPEID);

    // Get the id of the user filter data type
    cursor = filterDataTypeDbAdapter.fetchAll(null, filter.compareWithDataType);
    cursor.moveToFirst();
    long compareWithDataTypeID = CursorHelper.getLongFromCursor(cursor,
        DataTypeDbAdapter.KEY_DATATYPEID);

    // Get the id of the comparison using the comparison name and the ids of the data types of its
    // arguments
    cursor = filterComparisonDbAdapter.fetchAll(filter.filter, null, filterOnDataTypeID,
        compareWithDataTypeID);
    cursor.moveToFirst();
    long dataFilterID = CursorHelper
        .getLongFromCursor(cursor, DataFilterDbAdapter.KEY_DATAFILTERID);

    // Get the id of the event attribute
    cursor = eventAttributeDbAdapter.fetchAll(filter.eventAttribute, eventID, null);
    cursor.moveToFirst();
    long eventAttributeID = CursorHelper.getLongFromCursor(cursor,
        RegisteredEventAttributeDbAdapter.KEY_EVENTATTRIBUTEID);

    // Insert the filter into the database and use its ID as the parent of the next filter
    parentID = ruleFilterDbAdapter.insert(ruleID, eventAttributeID, -1L, dataFilterID, parentID,
        filter.compareWithData);

    for (Tree<Filter> childNode : filterNode.getChildren()) {
      addFilterToDatabase(childNode, parentID);
    }
  }

  private static void addActionToDatabase(int actionNumber) {
    // Get action ID
    String[] currentAction = actionData[actionNumber];

    Cursor cursor = applicationDbAdapter.fetchAll(currentAction[actionAppCol], null, true);
    cursor.moveToFirst();
    long appID = CursorHelper.getLongFromCursor(cursor, RegisteredAppDbAdapter.KEY_APPID);

    cursor = actionDbAdapter.fetchAll(currentAction[actionNameCol], appID);
    cursor.moveToFirst();
    long actionID = CursorHelper.getLongFromCursor(cursor, RegisteredActionDbAdapter.KEY_ACTIONID);

    long ruleActionID = ruleActionDbAdapter.insert(ruleID, actionID);

    cursor = actionParameterDbAdapter.fetchAll(currentAction[actionParam1Col], actionID, null);
    cursor.moveToFirst();
    long actionParam1ID = CursorHelper.getLongFromCursor(cursor,
        RegisteredActionParameterDbAdapter.KEY_ACTIONPARAMETERID);

    ruleActionParameterDbAdapter.insert(ruleActionID, actionParam1ID,
        currentAction[actionParam1DataCol]);

    cursor = actionParameterDbAdapter.fetchAll(currentAction[actionParam2Col], actionID, null);
    cursor.moveToFirst();
    long actionParam2ID = CursorHelper.getLongFromCursor(cursor,
        RegisteredActionParameterDbAdapter.KEY_ACTIONPARAMETERID);

    ruleActionParameterDbAdapter.insert(ruleActionID, actionParam2ID,
        currentAction[actionParam2DataCol]);
  }
}
