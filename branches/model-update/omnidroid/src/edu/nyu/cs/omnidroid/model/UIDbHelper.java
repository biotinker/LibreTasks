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
package edu.nyu.cs.omnidroid.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.core.datatypes.DataType;
import edu.nyu.cs.omnidroid.core.datatypes.FactoryDataType;
import edu.nyu.cs.omnidroid.model.db.DataFilterDbAdapter;
import edu.nyu.cs.omnidroid.model.db.DataTypeDbAdapter;
import edu.nyu.cs.omnidroid.model.db.DbHelper;
import edu.nyu.cs.omnidroid.model.db.RegisteredActionDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredAppDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredEventAttributeDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredEventDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RuleActionDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RuleDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RuleFilterDbAdapter;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAction;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelApplication;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAttribute;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelEvent;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelFilter;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelRuleFilter;
import edu.nyu.cs.omnidroid.ui.simple.model.Rule;
import edu.nyu.cs.omnidroid.ui.simple.model.RuleNode;
import edu.nyu.cs.omnidroid.ui.simple.model.RuleSparse;

public class UIDbHelper {

  private DataTypeDbAdapter dataTypeDbAdapter;
  private DataFilterDbAdapter dataFilterDbAdapter;
  private RegisteredAppDbAdapter registeredAppDbAdapter;
  private RegisteredEventDbAdapter registeredEventDbAdapter;
  private RegisteredActionDbAdapter registeredActionDbAdapter;
  private RegisteredEventAttributeDbAdapter registeredEventAttributeDbAdapter;
  private RuleFilterDbAdapter ruleFilterDbAdpater;
  private RuleActionDbAdapter ruleActionDbAdpater;
  private RuleDbAdapter ruleDbAdapter;
  private DbHelper omnidroidDbHelper;

  // Hash maps for storing cached data for quick lookup
  private Map<Integer, String> dataTypeNames;
  private Map<Integer, String> dataTypeClassNames;
  private Map<Integer, String> dataFilterNames;
  private Map<Integer, ModelApplication> applications;
  private Map<Integer, ModelEvent> events;
  private Map<Integer, ModelAction> actions;
  private Map<Integer, ModelAttribute> attributes;

  public UIDbHelper(Context context) {
    omnidroidDbHelper = new DbHelper(context);
    SQLiteDatabase database = omnidroidDbHelper.getWritableDatabase();

    // Initialize db adapters
    dataTypeDbAdapter = new DataTypeDbAdapter(database);
    dataFilterDbAdapter = new DataFilterDbAdapter(database);
    registeredAppDbAdapter = new RegisteredAppDbAdapter(database);
    registeredEventDbAdapter = new RegisteredEventDbAdapter(database);
    registeredActionDbAdapter = new RegisteredActionDbAdapter(database);
    registeredEventAttributeDbAdapter = new RegisteredEventAttributeDbAdapter(database);
    ruleDbAdapter = new RuleDbAdapter(database);

    // Initialize db cache
    dataTypeNames = new HashMap<Integer, String>();
    dataTypeClassNames = new HashMap<Integer, String>();
    dataFilterNames = new HashMap<Integer, String>();
    applications = new HashMap<Integer, ModelApplication>();
    events = new HashMap<Integer, ModelEvent>();
    actions = new HashMap<Integer, ModelAction>();
    attributes = new HashMap<Integer, ModelAttribute>();

    // Load db cache maps
    loadDbCache();
  }

  /**
   * Close the UIHelper
   */
  public void close() {
    omnidroidDbHelper.close();
  }
  
  /**
   * Helper method to get integer column value from cursor.
   * 
   * @param cursor
   *          is the cursor to get value from
   *          
   * @param columnName
   *          is name of the column to get
   *          
   * @return integer value of the specific column within the cursor
   */
  private int getIntFromCursor(Cursor cursor, String columnName) {
    return cursor.getInt(cursor.getColumnIndex(columnName));
  }
  
  /**
   * Helper method to get integer column value from cursor.
   * 
   * @param cursor
   *          is the cursor to get value from
   *          
   * @param columnName
   *          is name of the column to get
   *          
   * @return integer value of the specific column within the cursor
   */
  private String getStringFromCursor(Cursor cursor, String columnName) {
    return cursor.getString(cursor.getColumnIndex(columnName));
  }

  /**
   * Load cached data into hash maps
   */
  private void loadDbCache() {

    // Load DataTypes
    Cursor cursor = dataTypeDbAdapter.fetchAll();
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();
      dataTypeNames.put(getIntFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPEID),
          getStringFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPENAME));
      dataTypeClassNames.put(getIntFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPEID), 
          getStringFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPECLASSNAME));
    }
    cursor.close();

    // Load Filters
    cursor = dataFilterDbAdapter.fetchAll();
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();
      dataFilterNames.put(getIntFromCursor(cursor, DataFilterDbAdapter.KEY_DATAFILTERID),
          getStringFromCursor(cursor, DataFilterDbAdapter.KEY_DATAFILTERNAME));
    }
    cursor.close();

    // Load applications
    cursor = registeredAppDbAdapter.fetchAll();
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();
      ModelApplication application = new ModelApplication(
            getStringFromCursor(cursor, RegisteredAppDbAdapter.KEY_APPNAME), 
            "", //TODO(ehotou) After implementing desc for app, load it here
            R.drawable.icon_application_unknown, 
            getIntFromCursor(cursor, RegisteredAppDbAdapter.KEY_APPID));
      applications.put(application.getDatabaseId(), application);
    }
    cursor.close();

    // Load Events
    cursor = registeredEventDbAdapter.fetchAll();
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();
      ModelEvent event = new ModelEvent(
          getIntFromCursor(cursor, RegisteredEventDbAdapter.KEY_EVENTID),
          getStringFromCursor(cursor, RegisteredEventDbAdapter.KEY_EVENTNAME), 
          "", //TODO(ehotou) After implementing desc for event, load it here
          R.drawable.icon_event_unknown);
      events.put(event.getDatabaseId(), event);
    }
    cursor.close();

    // Load Actions
    cursor = registeredActionDbAdapter.fetchAll();
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();

      ModelApplication application = applications.get(
          getIntFromCursor(cursor, RegisteredActionDbAdapter.KEY_APPID));

      ModelAction action = new ModelAction(
          getStringFromCursor(cursor, RegisteredActionDbAdapter.KEY_ACTIONNAME), 
          "", //TODO(ehotou) After implementing desc for action, load it here
          R.drawable.icon_action_unknown,
          getIntFromCursor(cursor, RegisteredActionDbAdapter.KEY_ACTIONID), application);

      actions.put(action.getDatabaseId(), action);
    }
    cursor.close();

    // Load Attributes
    cursor = registeredEventAttributeDbAdapter.fetchAll();
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();

      ModelAttribute attribute = new ModelAttribute(getIntFromCursor(cursor,
          RegisteredEventAttributeDbAdapter.KEY_EVENTATTRIBUTEID), getIntFromCursor(cursor,
          RegisteredEventAttributeDbAdapter.KEY_EVENTID), getIntFromCursor(cursor,
          RegisteredEventAttributeDbAdapter.KEY_DATATYPEID), getStringFromCursor(cursor,
          RegisteredEventAttributeDbAdapter.KEY_EVENTATTRIBUTENAME), 
          "", //TODO(ehotou) After implementing desc for attribute, load it here
          R.drawable.icon_attribute_unknown);
      
      attributes.put(attribute.getDatabaseId(), attribute);
    }
    cursor.close();
  }

  /**
   * Get an dataType object
   * 
   * @param attribute
   *          is the attribute object
   *          
   * @param data
   *          is the content of the data within the dataType object
   *          
   * @return an dataType object
   */
  private DataType getDataType(int dataTypeID, String data) {
    String dataTypeClassName = dataTypeClassNames.get(dataTypeID);
    return FactoryDataType.createObject(dataTypeClassName, data);
  }

  /**
   * @return all applications as an ArrayList
   */
  public ArrayList<ModelApplication> getAllApplications() {
    ArrayList<ModelApplication> applicationList = new ArrayList<ModelApplication>();
    for (Entry<Integer, ModelApplication> entry : applications.entrySet()) {
      applicationList.add(entry.getValue());
    }
    return applicationList;
  }

  /**
   * @return all events as an ArrayList
   */
  public ArrayList<ModelEvent> getAllEvents() {
    ArrayList<ModelEvent> eventList = new ArrayList<ModelEvent>();
    for (Entry<Integer, ModelEvent> entry : events.entrySet()) {
      eventList.add(entry.getValue());
    }
    return eventList;
  }

  /**
   * @param application
   *          is a ModelApplication object
   *          
   * @return all actions associated with one application as an ArrayList
   */
  public ArrayList<ModelAction> getActionsForApplication(ModelApplication application) {
    ArrayList<ModelAction> actionList = new ArrayList<ModelAction>();
    for (Entry<Integer, ModelAction> entry : actions.entrySet()) {
      if (entry.getValue().getApplication().getDatabaseId() == application.getDatabaseId()) {
        actionList.add(entry.getValue());
      }
    }
    return actionList;
  }

  /**
   * @param event
   *          is a ModelEvent object
   *          
   * @return all attributes associated with one event as an ArrayList
   */
  public ArrayList<ModelAttribute> getAttributesForEvent(ModelEvent event) {
    ArrayList<ModelAttribute> attributesList = new ArrayList<ModelAttribute>();

    // Fetch all attribute associated with this event, set attributeName and its dataTypeId to null
    Cursor cursorAttribute = registeredEventAttributeDbAdapter.fetchAll(
        null, Long.valueOf(event.getDatabaseId()), null);

    for (int i = 0; i < cursorAttribute.getCount(); i++) {
      cursorAttribute.moveToNext();
      ModelAttribute attribute = attributes.get(getIntFromCursor(cursorAttribute, 
          RegisteredEventAttributeDbAdapter.KEY_EVENTATTRIBUTEID));
      attributesList.add(attribute);
    }

    cursorAttribute.close();
    return attributesList;
  }

  /**
   * @param attribute
   *          is a ModelAttribute object
   *          
   * @return all filters associated with one attribute as an ArrayList
   */
  public ArrayList<ModelFilter> getFiltersForAttribute(ModelAttribute attribute) {
    ArrayList<ModelFilter> filterList = new ArrayList<ModelFilter>();
  
    // Fetch all filter associated with this attribute's dataType id, set filterName to null
    Cursor cursor = dataFilterDbAdapter.fetchAll(null, Long.valueOf(attribute.getDatatype()));
    
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();

      int filterID = getIntFromCursor(cursor, DataFilterDbAdapter.KEY_DATAFILTERID);
      String filterName = dataFilterNames.get(filterID);

      filterList.add(
          new ModelFilter(filterName, 
              "" // TODO(ehotou) After implementing desc for filter, load it here 
              , R.drawable.icon_filter_unknown, filterID,
              attribute));
    }
    cursor.close();
    return filterList;
  }

  /**
   * @return all rules as an ArrayList
   */
  public ArrayList<RuleSparse> getRules() {
    ArrayList<RuleSparse> rules = new ArrayList<RuleSparse>();
    Cursor cursor = ruleDbAdapter.fetchAll();
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();
      
      RuleSparse ruleSparse = new RuleSparse(
          getIntFromCursor(cursor, RuleDbAdapter.KEY_RULEID),
          getStringFromCursor(cursor, RuleDbAdapter.KEY_RULENAME), 
          getIntFromCursor(cursor, RuleDbAdapter.KEY_ENABLED) == 1);
      
      rules.add(ruleSparse);
    }
    cursor.close();
    return rules;
  }

  /**
   * @param databaseId
   *          is the rule id
   * @return a fully loaded ModelRule object with databaseId
   */
  public Rule loadRule(int databaseId) {
    Cursor cursorRule = ruleDbAdapter.fetch(Long.valueOf(databaseId));

    // Create a new empty rule instance.
    Rule rule = new Rule();

    // Fetch the root event.
    ModelEvent event = events.get(getIntFromCursor(cursorRule, RuleDbAdapter.KEY_EVENTID));

    // Construct a node for it, set it to be the root of the rule
    rule.setRootEvent(event);

    // Add all filters for this rule to the root node in a tree format.
    addFiltersToRuleNode(databaseId, rule.getRootNode());

    // Get all actions associated with this rule.
    ArrayList<ModelAction> actions = getActionForRule(databaseId);

    // Add each of them to the tree.
    for (int i = 0; i < actions.size(); i++) {
      rule.getRootNode().addChild(actions.get(i));
    }

    return rule;
  }

  /**
   * Adds all filters to a root node in tree form.
   * 
   * @param ruleId
   */
  private void addFiltersToRuleNode(int ruleId, RuleNode rootEvent) {

    // Map<filterId, filterParentId>, for all filters of the rule.
    HashMap<Integer, Integer> parentIds = new HashMap<Integer, Integer>();
    
    // All filters keyed by filterId for quick lookup below.
    HashMap<Integer, ModelRuleFilter> filtersUnlinked = new HashMap<Integer, ModelRuleFilter>();

    // Fetch all ruleFilter associated with this rule, set other parameters to be null
    Cursor cursorRuleFilters = ruleFilterDbAdpater.fetchAll(Long.valueOf(ruleId), null, null, null,
        null, null);

    for (int i = 0; i < cursorRuleFilters.getCount(); i++) {
      cursorRuleFilters.moveToNext();

      // Get attribute that this ruleFilter associated with
      ModelAttribute attribute = attributes.get(
          getIntFromCursor(cursorRuleFilters, RuleFilterDbAdapter.KEY_EVENTATTRIBUTEID));

      // Load the user defined data within this rule filter
      String filterInputFromUser = getStringFromCursor(
          cursorRuleFilters, RuleFilterDbAdapter.KEY_RULEFILTERDATA);

      // Construct a data type object
      DataType filterData = getDataType(attribute.getDatatype(), filterInputFromUser);

      // For filters, first load up the model filter, then supply it to
      // the model rule filter instance.
      int filterID = getIntFromCursor(cursorRuleFilters, RuleFilterDbAdapter.KEY_DATAFILTERID);

      String filterName = dataFilterNames.get(filterID);
      
      ModelFilter modelFilter = new ModelFilter(filterName, 
          "", // TODO(ehotou) After implementing desc for filter, load it here.
          R.drawable.icon_filter_unknown, filterID, attribute);

      ModelRuleFilter filter = new ModelRuleFilter(
          getIntFromCursor(cursorRuleFilters, RuleFilterDbAdapter.KEY_RULEFILTERID), 
          modelFilter, filterData);

      // Insert filterId, filterParentId
      parentIds.put(filter.getDatabaseId(), 
          getIntFromCursor(cursorRuleFilters, RuleFilterDbAdapter.KEY_RULEFILTERID));
      
      // Store the filter instance itself.
      filtersUnlinked.put(filter.getDatabaseId(), filter);
    }
    cursorRuleFilters.close();

    // Keep iterating over all filters until we link each instance. This can be
    // improved if we know we'll get the filter records ordered in a tree hierarchy,
    // otherwise we can stick with this - we just keep passing over all the records
    // until we reconstruct each parent-child relationship.
    HashMap<Integer, RuleNode> filtersLinked = new HashMap<Integer, RuleNode>();
    Iterator<Integer> it = filtersUnlinked.keySet().iterator();
    while (it.hasNext()) {
      Integer filterId = it.next();
      ModelRuleFilter filter = filtersUnlinked.get(filterId);
      Integer parentFilterId = parentIds.get(filterId);

      if (parentFilterId.intValue() < 1) {
        // This is a top-level filter, its parent is the root node.
        RuleNode node = rootEvent.addChild(filter);
        filtersLinked.put(filterId, node);
        filtersUnlinked.remove(filterId);
      } else {
        // Add this filter to its parent node. The node may not have
        // been constructed yet, so we have to skip it and handle it
        // on a subsequent iteration.
        RuleNode nodeParent = filtersLinked.get(parentFilterId);
        if (nodeParent != null) {
          RuleNode nodeChild = nodeParent.addChild(filter);
          filtersLinked.put(filterId, nodeChild);
          filtersUnlinked.remove(filterId);
        }
      }
    }
  }

  /**
   * Get all actions associated with a rule
   */
  private ArrayList<ModelAction> getActionForRule(int ruleId) {
    Cursor cursorRuleActions = ruleActionDbAdpater.fetchAll(Long.valueOf(ruleId), null);
    ArrayList<ModelAction> actionList = new ArrayList<ModelAction>();
    
    for (int i = 0; i < cursorRuleActions.getCount(); i++) {
      cursorRuleActions.moveToNext();
      
      ModelAction action = actions.get(
          getIntFromCursor(cursorRuleActions, RuleActionDbAdapter.KEY_ACTIONID));

      actionList.add(action);
    }
    cursorRuleActions.close();
    return actionList;
  }

  /**
   * Given a rule, save it to the database.
   */
  public void saveRule(Rule rule) throws Exception {
    ModelEvent event = (ModelEvent) rule.getRootNode().getItem();
    ArrayList<RuleNode> filterList = rule.getFilterBranches();
    ArrayList<ModelAction> actionList = rule.getActions();

    // Save the rule record
    // TODO(ehotou) need to specify rule name and desc after we implement them
    long ruleID = ruleDbAdapter.insert(Long.valueOf(event.getDatabaseId()), "RuleName", "RuleDesc",
        true);

    // Save all rule actions
    for (int i = 0; i < actionList.size(); i++) {
      ruleActionDbAdpater.insert(ruleID, Long.valueOf(actionList.get(i).getDatabaseId()));
    }

    // Save all rule filters
    for (int i = 0; i < filterList.size(); i++) {
      saveFilterRuleNode(ruleID, -1, filterList.get(i));
    }
  }

  /**
   * Recursively write each node of the filter branches to the database.
   */
  private void saveFilterRuleNode(long ruleID, long parentRuleNodeID, RuleNode node) {

    ModelRuleFilter filter = (ModelRuleFilter) node.getItem();

    long thisRuleNodeID = ruleFilterDbAdpater.insert(ruleID, 
        Long.valueOf(filter.getModelFilter().getAttribute().getDatabaseId()), 
        Long.valueOf(-1), // TODO(ehotou) after implementing external, insert it here
        Long.valueOf(filter.getDatabaseId()),
        parentRuleNodeID, 
        filter.getData().toString());

    // insert all children filters recursively:
    for (int i = 0; i < node.getChildren().size(); i++) {
      saveFilterRuleNode(ruleID, thisRuleNodeID, node.getChildren().get(i));
    }
  }
}