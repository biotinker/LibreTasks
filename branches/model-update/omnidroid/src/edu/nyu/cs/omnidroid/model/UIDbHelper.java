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
import static edu.nyu.cs.omnidroid.model.CursorHelper.getIntFromCursor;
import static edu.nyu.cs.omnidroid.model.CursorHelper.getStringFromCursor;

/**
 * This class serves as a access layer of the database for Omnidroid's UI data model representation.
 */
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
  private SQLiteDatabase database;

  // Hash maps for storing cached data for quick lookup
  private HashMap<Integer, String> dataTypeNames;
  private HashMap<Integer, String> dataTypeClassNames;
  private HashMap<Integer, String> dataFilterNames;
  private HashMap<Integer, ModelApplication> applications;
  private HashMap<Integer, ModelEvent> events;
  private HashMap<Integer, ModelAction> actions;
  private HashMap<Integer, ModelAttribute> attributes;

  public UIDbHelper(Context context) {
    omnidroidDbHelper = new DbHelper(context);
    database = omnidroidDbHelper.getWritableDatabase();
    
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
    database.close();
  }

  /**
   * Load cached data into hash maps
   */
  private void loadDbCache() {

    // TODO(ehotou) Consider lazy initialization for some of these stuff.
    
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
    ArrayList<ModelApplication> applicationList = new ArrayList<ModelApplication>(
        applications.size());
    applicationList.addAll(applications.values());
    return applicationList;
  }

  /**
   * @return all events as an ArrayList
   */
  public ArrayList<ModelEvent> getAllEvents() {
    ArrayList<ModelEvent> eventList = new ArrayList<ModelEvent>(events.size());
    eventList.addAll(events.values());
    return eventList;
  }

  /**
   * @param application
   *          is a ModelApplication object
   *          
   * @return all actions associated with one application as an ArrayList
   */
  public ArrayList<ModelAction> getActionsForApplication(ModelApplication application) {
    ArrayList<ModelAction> actionList = new ArrayList<ModelAction>(actions.size());
    for (ModelAction action : actions.values()) {
      if (action.getApplication().getDatabaseId() == application.getDatabaseId()) {
        actionList.add(action);
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
    ArrayList<ModelAttribute> attributesList = new ArrayList<ModelAttribute>(attributes.size());
    for (ModelAttribute attribute : attributes.values()) {
      if(attribute.getForeignKeyEventId() == event.getDatabaseId()) {
        attributesList.add(attribute);
      }
    }
    return attributesList;
  }

  /**
   * @param attribute
   *          is a ModelAttribute object
   *          
   * @return all filters associated with one attribute as an ArrayList
   */
  public ArrayList<ModelFilter> getFiltersForAttribute(ModelAttribute attribute) {
    // Fetch all filter associated with this attribute's dataType id, set filterName to null
    Cursor cursor = dataFilterDbAdapter.fetchAll(null, Long.valueOf(attribute.getDatatype()));
    
    int count = cursor.getCount();
    
    ArrayList<ModelFilter> filterList = new ArrayList<ModelFilter>(count);
    
    for (int i = 0; i < count; i++) {
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
   * @return all Rule objects (sparse version just contains id, name) as an ArrayList
   */
  public ArrayList<Rule> getRules() {
    Cursor cursor = ruleDbAdapter.fetchAll();
    
    int count = cursor.getCount();
    
    ArrayList<Rule> ruleList = new ArrayList<Rule>(count);
    
    for (int i = 0; i < count; i++) {
      cursor.moveToNext();
      
      Rule rule = new Rule(getIntFromCursor(cursor, RuleDbAdapter.KEY_RULEID));
      rule.setName(getStringFromCursor(cursor, RuleDbAdapter.KEY_RULENAME));
      // TODO(ehotou) need to set rule active flag when it is added into rule object
      
      ruleList.add(rule);
    }
    cursor.close();
    return ruleList;
  }

  /**
   * @param databaseId
   *          is the rule id
   *          
   * @return a fully loaded Rule object with databaseId
   */
  public Rule loadRule(int databaseId) {
    Cursor cursorRule = ruleDbAdapter.fetch(Long.valueOf(databaseId));

    Rule rule = new Rule(databaseId);

    // Fetch and set the root event.
    ModelEvent event = events.get(getIntFromCursor(cursorRule, RuleDbAdapter.KEY_EVENTID));
    rule.setRootEvent(event);

    // Add all filters for this rule to the root node in a tree format.
    addFiltersToRuleNode(databaseId, rule.getRootNode());

    // Add all actions for this rule
    ArrayList<ModelAction> actionList = getActionsForRule(databaseId);
    for (ModelAction action : actionList) {
      rule.getRootNode().addChild(action);
    }

    return rule;
  }

  /**
   * Adds all filters to a root node in tree form.
   * 
   * TODO(ehotou) consider moving part of this method which constructs the tree structure to some
   * util/builder method within Rule class
   * 
   * @param ruleId
   *          is id of the rule record to be loaded
   * 
   * @param is
   *          the event node at the root of the rule structure
   */
  private void addFiltersToRuleNode(int ruleId, RuleNode rootEvent) {

    // Map<filterId, filterParentId>, for all filters of the rule.
    HashMap<Integer, Integer> parentIds = new HashMap<Integer, Integer>();
    
    // All filters keyed by filterId for quick lookup below.
    HashMap<Integer, ModelRuleFilter> filtersUnlinked = new HashMap<Integer, ModelRuleFilter>();

    // Fetch all ruleFilter associated with this rule, set other parameters to be null
    Cursor cursorRuleFilters = ruleFilterDbAdpater.fetchAll(Long.valueOf(ruleId), null, null, null,
        null, null);
    
    int count = cursorRuleFilters.getCount();
    
    for (int i = 0; i < count; i++) {
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
  private ArrayList<ModelAction> getActionsForRule(int ruleId) {
    Cursor cursorRuleActions = ruleActionDbAdpater.fetchAll(Long.valueOf(ruleId), null);
    
    int count = cursorRuleActions.getCount();
    
    ArrayList<ModelAction> actionList = new ArrayList<ModelAction>(count);
    
    for (int i = 0; i < count; i++) {
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
    for (ModelAction action : actionList) {
      ruleActionDbAdpater.insert(ruleID, Long.valueOf(action.getDatabaseId()));
    }

    // Save all rule filters
    for (RuleNode filterNode : filterList) {
      saveFilterRuleNode(ruleID, -1, filterNode);
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
    for (RuleNode filterNode : node.getChildren()) {
      saveFilterRuleNode(ruleID, thisRuleNodeID, filterNode);
    }
  }
}