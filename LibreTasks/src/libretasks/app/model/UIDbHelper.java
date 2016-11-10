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
package libretasks.app.model;

import static libretasks.app.model.CursorHelper.getBooleanFromCursor;
import static libretasks.app.model.CursorHelper.getLongFromCursor;
import static libretasks.app.model.CursorHelper.getStringFromCursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import libretasks.app.R;
import libretasks.app.controller.datatypes.DataType;
import libretasks.app.controller.datatypes.FactoryDataType;
import libretasks.app.model.db.DataFilterDbAdapter;
import libretasks.app.model.db.DataTypeDbAdapter;
import libretasks.app.model.db.DbHelper;
import libretasks.app.model.db.LogActionDbAdapter;
import libretasks.app.model.db.LogDbAdapter;
import libretasks.app.model.db.LogGeneralDbAdapter;
import libretasks.app.model.db.LogEventDbAdapter;
import libretasks.app.model.db.RegisteredActionDbAdapter;
import libretasks.app.model.db.RegisteredActionParameterDbAdapter;
import libretasks.app.model.db.RegisteredAppDbAdapter;
import libretasks.app.model.db.RegisteredEventAttributeDbAdapter;
import libretasks.app.model.db.RegisteredEventDbAdapter;
import libretasks.app.model.db.RuleActionDbAdapter;
import libretasks.app.model.db.RuleActionParameterDbAdapter;
import libretasks.app.model.db.RuleDbAdapter;
import libretasks.app.model.db.RuleFilterDbAdapter;
import libretasks.app.view.simple.model.ModelAction;
import libretasks.app.view.simple.model.ModelApplication;
import libretasks.app.view.simple.model.ModelAttribute;
import libretasks.app.view.simple.model.ModelEvent;
import libretasks.app.view.simple.model.ModelFilter;
import libretasks.app.view.simple.model.ModelLog;
import libretasks.app.view.simple.model.ModelParameter;
import libretasks.app.view.simple.model.ModelRuleAction;
import libretasks.app.view.simple.model.ModelRuleFilter;
import libretasks.app.view.simple.model.Rule;
import libretasks.app.view.simple.model.RuleNode;

/**
 * This class serves as an access layer of the database for Omnidroid's UI data model
 * representation.
 * 
 * TODO(ehotou): Consider throwing exceptions (or Logging) when db operations fail.
 */
public class UIDbHelper {
  private static final String TAG = UIDbHelper.class.getSimpleName();

  // Database management
  private DbHelper dbHelper;
  private SQLiteDatabase database;

  // Database Adapters
  private DataTypeDbAdapter dataTypeDbAdapter;
  private DataFilterDbAdapter dataFilterDbAdapter;
  private RegisteredAppDbAdapter registeredAppDbAdapter;
  private RegisteredEventDbAdapter registeredEventDbAdapter;
  private RegisteredActionDbAdapter registeredActionDbAdapter;
  private RegisteredEventAttributeDbAdapter registeredEventAttributeDbAdapter;
  private RegisteredActionParameterDbAdapter registeredActionParameterDbAdapter;
  private RuleFilterDbAdapter ruleFilterDbAdapter;
  private RuleActionDbAdapter ruleActionDbAdapter;
  private RuleActionParameterDbAdapter ruleActionParameterDbAdapter;
  private RuleDbAdapter ruleDbAdapter;
  private LogEventDbAdapter logEventDbAdapter;
  private LogActionDbAdapter logActionDbAdapter;
  private LogGeneralDbAdapter logGeneralDbAdapter;

  // Hash maps for storing cached data for quick lookup
  private Map<Long, String> dataTypeNames;
  private Map<Long, String> dataTypeClassNames;
  private Map<Long, String> dataFilterNames;
  private Map<Long, ModelApplication> applications;
  private Map<Long, ModelEvent> events;
  private Map<Long, ModelAction> actions;
  private Map<Long, ModelAttribute> globalAttributes;
  private Map<Long, ModelAttribute> specificAttributes;
  private Map<Long, ModelParameter> parameters;

  // Get user configured settings
  private SharedPreferences settings;

  // This flag marks whether this helper is closed
  private boolean isClosed = false;

  /**
   * Reset the db, drop all necessary table, and recreate them and repopulate them again
   */
  public void resetDB() {
    dbHelper.cleanup(database);
  }

  public UIDbHelper(Context context) {
    dbHelper = new DbHelper(context);
    database = dbHelper.getWritableDatabase();

    // Initialize db adapters
    dataTypeDbAdapter = new DataTypeDbAdapter(database);
    dataFilterDbAdapter = new DataFilterDbAdapter(database);
    registeredAppDbAdapter = new RegisteredAppDbAdapter(database);
    registeredEventDbAdapter = new RegisteredEventDbAdapter(database);
    registeredActionDbAdapter = new RegisteredActionDbAdapter(database);
    registeredEventAttributeDbAdapter = new RegisteredEventAttributeDbAdapter(database);
    registeredActionParameterDbAdapter = new RegisteredActionParameterDbAdapter(database);
    ruleFilterDbAdapter = new RuleFilterDbAdapter(database);
    ruleActionDbAdapter = new RuleActionDbAdapter(database);
    ruleActionParameterDbAdapter = new RuleActionParameterDbAdapter(database);
    ruleDbAdapter = new RuleDbAdapter(database);
    logEventDbAdapter = new LogEventDbAdapter(database);
    logActionDbAdapter = new LogActionDbAdapter(database);
    logGeneralDbAdapter = new LogGeneralDbAdapter(database);

    // Initialize db cache
    dataTypeNames = new HashMap<Long, String>();
    dataTypeClassNames = new HashMap<Long, String>();
    dataFilterNames = new HashMap<Long, String>();
    applications = new HashMap<Long, ModelApplication>();
    events = new LinkedHashMap<Long, ModelEvent>();
    actions = new HashMap<Long, ModelAction>();
    globalAttributes = new HashMap<Long, ModelAttribute>();
    specificAttributes = new HashMap<Long, ModelAttribute>();
    parameters = new HashMap<Long, ModelParameter>();

    // Load db cache maps
    loadDbCache();
  }

  /**
   * Close the UIDbHelper. UI needs to call this method when it is done with the database
   * connection. UIDbHelper is not usable after calling this method.
   */
  public void close() {
    isClosed = true;
    dbHelper.close();
    database.close();
  }

  /**
   * Load cached data into hash maps
   */
  private void loadDbCache() {

    // TODO(ehotou) Consider lazy initialization for some of these stuff.

    // Load Preferences
    settings = dbHelper.getSharedPreferences();

    // Load DataTypes
    Cursor cursor = dataTypeDbAdapter.fetchAll();
    while (cursor.moveToNext()) {
      dataTypeNames.put(getLongFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPEID),
          getStringFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPENAME));
      dataTypeClassNames.put(getLongFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPEID),
          getStringFromCursor(cursor, DataTypeDbAdapter.KEY_DATATYPECLASSNAME));
    }
    cursor.close();

    // Load Filters
    cursor = dataFilterDbAdapter.fetchAll();
    while (cursor.moveToNext()) {

      dataFilterNames.put(getLongFromCursor(cursor, DataFilterDbAdapter.KEY_DATAFILTERID),
          getStringFromCursor(cursor, DataFilterDbAdapter.KEY_DATAFILTERDISPLAYNAME));
    }
    cursor.close();

    // Load Applications
    cursor = registeredAppDbAdapter.fetchAll();
    while (cursor.moveToNext()) {
      ModelApplication application = new ModelApplication(getStringFromCursor(cursor,
          RegisteredAppDbAdapter.KEY_APPNAME), "", // TODO(ehotou) After implementing desc for app,
          // load it here
          R.drawable.icon_application_unknown, getLongFromCursor(cursor,
              RegisteredAppDbAdapter.KEY_APPID), getBooleanFromCursor(cursor,
              RegisteredAppDbAdapter.KEY_LOGIN), getStringFromCursor(cursor,
              RegisteredAppDbAdapter.KEY_USERNAME), getStringFromCursor(cursor,
              RegisteredAppDbAdapter.KEY_PASSWORD));
      applications.put(application.getDatabaseId(), application);
    }
    cursor.close();

    // Load Events in alphabetical order
    cursor = registeredEventDbAdapter.fetchAllOrdered();
    while (cursor.moveToNext()) {
      ModelEvent event = new ModelEvent(getLongFromCursor(cursor,
          RegisteredEventDbAdapter.KEY_EVENTID), getStringFromCursor(cursor,
          RegisteredEventDbAdapter.KEY_EVENTNAME), "", // TODO(ehotou) After implementing
          // description for event, load it here
          R.drawable.icon_event_unknown);
      events.put(event.getDatabaseId(), event);
    }
    cursor.close();

    // Load Event Attributes
    cursor = registeredEventAttributeDbAdapter.fetchAllGlobalAttributes();
    while (cursor.moveToNext()) {
      ModelAttribute attribute = new ModelAttribute(getLongFromCursor(cursor,
          RegisteredEventAttributeDbAdapter.KEY_EVENTATTRIBUTEID), getLongFromCursor(cursor,
          RegisteredEventAttributeDbAdapter.KEY_EVENTID), getLongFromCursor(cursor,
          RegisteredEventAttributeDbAdapter.KEY_DATATYPEID), getStringFromCursor(cursor,
          RegisteredEventAttributeDbAdapter.KEY_EVENTATTRIBUTENAME), "", // TODO(ehotou) After
          // implementing desc for attribute, load it here
          R.drawable.icon_attribute_unknown);

      globalAttributes.put(attribute.getDatabaseId(), attribute);
    }
    cursor.close();

    cursor = registeredEventAttributeDbAdapter.fetchAllSpecificAttibutes();
    while (cursor.moveToNext()) {
      ModelAttribute attribute = new ModelAttribute(getLongFromCursor(cursor,
          RegisteredEventAttributeDbAdapter.KEY_EVENTATTRIBUTEID), getLongFromCursor(cursor,
          RegisteredEventAttributeDbAdapter.KEY_EVENTID), getLongFromCursor(cursor,
          RegisteredEventAttributeDbAdapter.KEY_DATATYPEID), getStringFromCursor(cursor,
          RegisteredEventAttributeDbAdapter.KEY_EVENTATTRIBUTENAME), "", // TODO(ehotou) After
          // implementing desc for attribute, load it here
          R.drawable.icon_attribute_unknown);

      specificAttributes.put(attribute.getDatabaseId(), attribute);
    }
    cursor.close();

    // Load Action Parameters
    cursor = registeredActionParameterDbAdapter.fetchAll();
    while (cursor.moveToNext()) {
      ModelParameter parameter = new ModelParameter(getLongFromCursor(cursor,
          RegisteredActionParameterDbAdapter.KEY_ACTIONPARAMETERID), getLongFromCursor(cursor,
          RegisteredActionParameterDbAdapter.KEY_ACTIONID), getLongFromCursor(cursor,
          RegisteredActionParameterDbAdapter.KEY_DATATYPEID), getStringFromCursor(cursor,
          RegisteredActionParameterDbAdapter.KEY_ACTIONPARAMETERNAME), "" // TODO(ehotou) After
      // implementing desc for parameter, load it here
      );

      parameters.put(parameter.getDatabaseId(), parameter);
    }
    cursor.close();

    // Load Actions
    cursor = registeredActionDbAdapter.fetchAll();
    while (cursor.moveToNext()) {
      ModelApplication application = applications.get(getLongFromCursor(cursor,
          RegisteredActionDbAdapter.KEY_APPID));

      long actionID = getLongFromCursor(cursor, RegisteredActionDbAdapter.KEY_ACTIONID);

      // Load parameters for each action
      ArrayList<ModelParameter> parameterList = new ArrayList<ModelParameter>();
      for (ModelParameter parameter : parameters.values()) {
        if (parameter.getForeignKeyActionId() == actionID) {
          parameterList.add(parameter);
        }
      }

      ModelAction action = new ModelAction(getStringFromCursor(cursor,
          RegisteredActionDbAdapter.KEY_ACTIONNAME), "", // TODO(ehotou) After implementing desc for
          // action, load it here
          R.drawable.icon_action_unknown, actionID, application, parameterList);

      actions.put(actionID, action);
    }
    cursor.close();
  }

  /**
   * Create a dataType object with a type matches dataTypeID, containing specific data.
   * 
   * @param dataTypeID
   *          is id of the dataType to be created
   * 
   * @param data
   *          is the content of the data within the dataType object
   * 
   * @return a dataType object
   */
  private DataType getDataType(long dataTypeID, String data) {
    String dataTypeClassName = dataTypeClassNames.get(dataTypeID);
    return FactoryDataType.createObject(dataTypeClassName, data);
  }

  /**
   * @return all applications as an ArrayList
   * @throws IllegalStateException
   *           if this helper is closed
   */
  public ArrayList<ModelApplication> getAllApplications() {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }
    ArrayList<ModelApplication> applicationList = new ArrayList<ModelApplication>(applications
        .size());
    applicationList.addAll(applications.values());
    return applicationList;
  }

  /**
   * Updates the application username and password in the database.
   * 
   * @param modelApp
   *          the application object to be updated
   * @return true if success, or false otherwise.
   * @throws IllegalStateException
   *           if this helper is closed
   */
  public boolean updateApplicationLoginInfo(ModelApplication modelApp) {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }
    return registeredAppDbAdapter.update(modelApp.getDatabaseId(), null, null, null, null, modelApp
        .getUsername(), modelApp.getPassword());
  }

  /**
   * Clears the application username and password in the database. Sets them as empty strings.
   * 
   * @param modelApp
   *          the application object to be updated
   * @return true if success, or false otherwise.
   * @throws IllegalStateException
   *           if this helper is closed
   */
  public boolean clearApplicationLoginInfo(ModelApplication modelApp) {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }
    return registeredAppDbAdapter.update(modelApp.getDatabaseId(), null, null, null, null, "", "");
  }

  /**
   * @param appDatabaseId
   *          the application database Id
   * @return application
   * @throws IllegalStateException
   *           if this helper is closed
   */
  public ModelApplication getApplication(long appDatabaseId) {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    Cursor cursor = registeredAppDbAdapter.fetch(appDatabaseId);
    String typeName = getStringFromCursor(cursor, RegisteredAppDbAdapter.KEY_APPNAME);
    long databaseID = getLongFromCursor(cursor, RegisteredAppDbAdapter.KEY_APPID);
    boolean loginEnabled = getBooleanFromCursor(cursor, RegisteredAppDbAdapter.KEY_LOGIN);
    String username = getStringFromCursor(cursor, RegisteredAppDbAdapter.KEY_USERNAME);
    String password = getStringFromCursor(cursor, RegisteredAppDbAdapter.KEY_PASSWORD);
    cursor.close();

    return new ModelApplication(typeName, "", R.drawable.icon_application_unknown, databaseID,
        loginEnabled, username, password);
  }

  /**
   * @return all events as an ArrayList
   * @throws IllegalStateException
   *           if this helper is closed
   */
  public ArrayList<ModelEvent> getAllEvents() {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }
    ArrayList<ModelEvent> eventList = new ArrayList<ModelEvent>(events.size());
    eventList.addAll(events.values());
    return eventList;
  }

  /**
   * @param application
   *          is a ModelApplication object
   * 
   * @return all actions associated with one application as an ArrayList
   * @throws IllegalStateException
   *           if this helper is closed
   */
  public ArrayList<ModelAction> getActionsForApplication(ModelApplication application) {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }
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
   * @throws IllegalStateException
   *           if this helper is closed
   */
  public List<ModelAttribute> getAttributesForEvent(ModelEvent event) {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    List<ModelAttribute> attributesList = new ArrayList<ModelAttribute>(specificAttributes.size());

    attributesList.addAll(globalAttributes.values());

    for (ModelAttribute attribute : specificAttributes.values()) {
      if (attribute.getForeignKeyEventId() == event.getDatabaseId()) {
        attributesList.add(attribute);
      }
    }

    return attributesList;
  }

  /**
   * Get the application associated with the action
   * 
   * @param action
   *          the action
   * @return the application associated with the action
   */
  public ModelApplication getApplicationFromAction(ModelAction action) {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    Cursor cursor = registeredActionDbAdapter.fetch(action.getDatabaseId());
    long appID = CursorHelper.getLongFromCursor(cursor, RegisteredActionDbAdapter.KEY_APPID);
    cursor.close();

    return getApplication(appID);
  }

  /**
   * @param attribute
   *          is a ModelAttribute object
   * 
   * @return all filters associated with one attribute as an ArrayList
   * @throws IllegalStateException
   *           if this helper is closed
   */
  public ArrayList<ModelFilter> getFiltersForAttribute(ModelAttribute attribute) {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    // Fetch all filter that filters on this attribute's dataType, set filterName to null,
    // set compareWithDatatypeID to null
    Cursor cursor = dataFilterDbAdapter.fetchAll(null, null, attribute.getDatatype(), null);
    ArrayList<ModelFilter> filterList = new ArrayList<ModelFilter>(cursor.getCount());

    while (cursor.moveToNext()) {
      long filterID = getLongFromCursor(cursor, DataFilterDbAdapter.KEY_DATAFILTERID);
      String filterName = dataFilterNames.get(filterID);

      filterList.add(new ModelFilter(filterName, "" // TODO(ehotou) After implementing desc for
          // filter, load it here
          , R.drawable.icon_filter_unknown, filterID, attribute));
    }
    cursor.close();
    return filterList;
  }

  /**
   * @return all Rule objects (sparse version just contains id, name and enabled) as an ArrayList
   * @throws IllegalStateException
   *           if this helper is closed
   */
  public ArrayList<Rule> getRules() {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    Cursor cursor = ruleDbAdapter.fetchAll();
    ArrayList<Rule> ruleList = new ArrayList<Rule>(cursor.getCount());

    while (cursor.moveToNext()) {
      ruleList.add(loadRuleSparse(cursor));
    }
    cursor.close();
    return ruleList;
  }

  /**
   * @param ruleId
   *          is the rule id
   * 
   * @return a fully loaded Rule object with databaseId
   * @throws IllegalStateException
   *           if this helper is closed
   */
  public Rule loadRule(long ruleId) {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    Cursor cursorRule = ruleDbAdapter.fetch(ruleId);

    Rule rule = loadRuleSparse(cursorRule);

    // Fetch and set the root event.
    ModelEvent event = events.get(getLongFromCursor(cursorRule, RuleDbAdapter.KEY_EVENTID));
    rule.setRootEvent(event);
    Log.d("UIDbhelper", "rule name: " + rule.getName());
    // Add all filters for this rule to the root node in a tree format.
    addFiltersToRuleNode(ruleId, rule.getRootNode());
    Log.d("UIDbhelper", "rule root event added: " + rule.getRootNode().getItem().getTypeName());
    // Add all actions for this rule
    ArrayList<ModelRuleAction> actionList = getActionsForRule(ruleId);
    for (ModelRuleAction action : actionList) {
      rule.getRootNode().addChild(action);
    }
    Log.d("UIDbhelper", "rule loaded: " + rule.getNaturalLanguageString());
    cursorRule.close();

    return rule;
  }

  /**
   * @return a sparse version of rule object
   */
  private Rule loadRuleSparse(Cursor cursorRule) {
    Rule rule = new Rule(getLongFromCursor(cursorRule, RuleDbAdapter.KEY_RULEID));
    rule.setName(getStringFromCursor(cursorRule, RuleDbAdapter.KEY_RULENAME));
    rule.setDescription(getStringFromCursor(cursorRule, RuleDbAdapter.KEY_RULEDESC));
    rule.setIsEnabled(getBooleanFromCursor(cursorRule, RuleDbAdapter.KEY_ENABLED));
    rule.setNotification(getBooleanFromCursor(cursorRule, RuleDbAdapter.KEY_NOTIFICATION));
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
   * @param rootEvent
   *          the event node at the root of the rule structure
   */
  private void addFiltersToRuleNode(long ruleId, RuleNode rootEvent) {

    // Map<filterId, filterParentId>, for all filters of the rule.
    HashMap<Long, Long> parentIds = new HashMap<Long, Long>();

    // All filters keyed by filterId for quick lookup below.
    HashMap<Long, ModelRuleFilter> filtersUnlinked = new HashMap<Long, ModelRuleFilter>();

    // Fetch all ruleFilter associated with this rule, set other parameters to be null
    Cursor cursorRuleFilters = ruleFilterDbAdapter.fetchAll(ruleId, null, null, null, null, null);

    while (cursorRuleFilters.moveToNext()) {

      // Get attribute that this ruleFilter associated with
      long attributeID = getLongFromCursor(cursorRuleFilters, RuleFilterDbAdapter.KEY_EVENTATTRIBUTEID);
      ModelAttribute attribute;
      
      if (specificAttributes.containsKey(attributeID)) {
        attribute = specificAttributes.get(attributeID);
      }
      else {
        attribute = globalAttributes.get(attributeID);
      }
      
      // Load the user defined data within this rule filter
      String filterInputFromUser = getStringFromCursor(cursorRuleFilters,
          RuleFilterDbAdapter.KEY_RULEFILTERDATA);
      Log.d("addFiltersToRuleNode", "trying to construct a " + attribute.getDatatype()
          + " dataType with: " + filterInputFromUser);

      // For filters, first load up the model filter, then supply it to
      // the model rule filter instance.
      long filterID = getLongFromCursor(cursorRuleFilters, RuleFilterDbAdapter.KEY_DATAFILTERID);
      String filterName = dataFilterNames.get(filterID);

      Cursor cursorFilter = dataFilterDbAdapter.fetch(filterID);
      long compareWithDataTypeId = getLongFromCursor(cursorFilter,
          DataFilterDbAdapter.KEY_COMPAREWITHDATATYPEID);

      // Construct a data type object
      DataType filterData = getDataType(compareWithDataTypeId, filterInputFromUser);

      Log.d("addFiltersToRuleNode", "The object constructed is : " + filterData);

      ModelFilter modelFilter = new ModelFilter(filterName, "", // TODO(ehotou) After implementing
          // desc for filter, load it here.
          R.drawable.icon_filter_unknown, filterID, attribute);

      ModelRuleFilter filter = new ModelRuleFilter(getLongFromCursor(cursorRuleFilters,
          RuleFilterDbAdapter.KEY_RULEFILTERID), modelFilter, filterData);

      // Insert filterId, filterParentId
      long filterParentId = getLongFromCursor(cursorRuleFilters,
          RuleFilterDbAdapter.KEY_PARENTRULEFILTERID);
      parentIds.put(filter.getDatabaseId(), filterParentId);

      // Store the filter instance itself.
      filtersUnlinked.put(filter.getDatabaseId(), filter);
      cursorFilter.close();
    }
    cursorRuleFilters.close();

    // Keep iterating over all filters until we link each instance. This can be
    // improved if we know we'll get the filter records ordered in a tree hierarchy,
    // otherwise we can stick with this - we just keep passing over all the records
    // until we reconstruct each parent-child relationship.
    HashMap<Long, RuleNode> filtersLinked = new HashMap<Long, RuleNode>();
    Iterator<Long> it = filtersUnlinked.keySet().iterator();
    while (it.hasNext()) {
      Long filterId = it.next();
      ModelRuleFilter filter = filtersUnlinked.get(filterId);
      Long parentFilterId = parentIds.get(filterId);

      if (parentFilterId < 1) {
        // This is a top-level filter, its parent is the root node.
        RuleNode node = rootEvent.addChild(filter);
        filtersLinked.put(filterId, node);
        it.remove();
      } else {
        // Add this filter to its parent node. The node may not have
        // been constructed yet, so we have to skip it and handle it
        // on a subsequent iteration.
        RuleNode nodeParent = filtersLinked.get(parentFilterId);
        if (nodeParent != null) {
          RuleNode nodeChild = nodeParent.addChild(filter);
          filtersLinked.put(filterId, nodeChild);
          it.remove();
        }
      }
    }
  }

  /**
   * Get all actions associated with a saved rule
   */
  private ArrayList<ModelRuleAction> getActionsForRule(long ruleId) {
    Cursor cursorRuleActions = ruleActionDbAdapter.fetchAll(ruleId, null);

    ArrayList<ModelRuleAction> ruleActionList = new ArrayList<ModelRuleAction>(cursorRuleActions
        .getCount());

    while (cursorRuleActions.moveToNext()) {

      long ruleActionID = getLongFromCursor(cursorRuleActions, RuleActionDbAdapter.KEY_RULEACTIONID);

      // Get the action
      ModelAction action = actions.get(getLongFromCursor(cursorRuleActions,
          RuleActionDbAdapter.KEY_ACTIONID));

      // Get parameters of this action
      ArrayList<ModelParameter> actionParameters = action.getParameters();

      // Get user data
      Cursor cursorRuleActionParameters = ruleActionParameterDbAdapter.fetchAll(ruleActionID, null,
          null);

      int count = cursorRuleActionParameters.getCount();

      ArrayList<DataType> userData = new ArrayList<DataType>(count);

      for (int j = 0; j < count; j++) {
        cursorRuleActionParameters.moveToNext();

        DataType data = getDataType(actionParameters.get(j).getDatatype(), getStringFromCursor(
            cursorRuleActionParameters, RuleActionParameterDbAdapter.KEY_RULEACTIONPARAMETERDATA));

        userData.add(data);
      }

      ModelRuleAction ruleAction = new ModelRuleAction(getLongFromCursor(cursorRuleActions,
          RuleActionDbAdapter.KEY_RULEACTIONID), action, userData);

      cursorRuleActionParameters.close();

      ruleActionList.add(ruleAction);
    }
    cursorRuleActions.close();
    return ruleActionList;
  }

  /**
   * Given a rule, save it to the database.
   * 
   * @return id of the saved rule record
   * 
   * @throws IllegalStateException
   *           if this helper is closed
   */
  public long saveRule(Rule rule) throws Exception {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    ModelEvent event = (ModelEvent) rule.getRootNode().getItem();
    ArrayList<RuleNode> ruleFilterList = rule.getFilterBranches();
    ArrayList<ModelRuleAction> ruleActionList = rule.getActions();

    if (rule.getDatabaseId() > 0) {
      deleteRule(rule.getDatabaseId());
    }

    String ruleName = rule.getName();
    String ruleDesc = rule.getDescription();
    long ruleID = ruleDbAdapter.insert(event.getDatabaseId(), ruleName == null
        || ruleName.length() == 0 ? "New Rule" : ruleName, ruleDesc == null
        || ruleDesc.length() == 0 ? "" : ruleDesc, rule.getIsEnabled());

    // Create all ruleAction records
    for (ModelRuleAction ruleAction : ruleActionList) {

      long ruleActionID = ruleActionDbAdapter.insert(ruleID, ruleAction.getModelAction()
          .getDatabaseId());

      ArrayList<ModelParameter> parameterList = ruleAction.getModelAction().getParameters();
      ArrayList<DataType> dataList = ruleAction.getDatas();
      for (int i = 0; i < dataList.size(); i++) {
        ruleActionParameterDbAdapter.insert(ruleActionID, parameterList.get(i).getDatabaseId(),
            dataList.get(i).toString());
      }
    }

    // Save all rule filters
    for (RuleNode filterNode : ruleFilterList) {
      saveFilterRuleNode(ruleID, -1, filterNode);
    }

    return ruleID;
  }

  /**
   * Recursively write each node of the filter branches to the database.
   * 
   * @param node
   *          is root of the ruleFilterNode tree to be added
   */
  private void saveFilterRuleNode(long ruleID, long parentRuleNodeID, RuleNode node) {

    ModelRuleFilter filter = (ModelRuleFilter) node.getItem();

    long ruleFilterID = ruleFilterDbAdapter.insert(ruleID, filter.getModelFilter().getAttribute()
        .getDatabaseId(), -1L, // TODO(ehotou) after implementing external, insert it here
        // TODO: (ehotou) verify ModelFilter id is what we want here (not ModelRuleFilter):
        filter.getModelFilter().getDatabaseId(), parentRuleNodeID, filter.getData().toString());

    // insert all children filters recursively:
    for (RuleNode filterNode : node.getChildren()) {
      saveFilterRuleNode(ruleID, ruleFilterID, filterNode);
    }
  }

  /**
   * Delete a rule record as well as ruleAction, ruleActionParameter and ruleFilters records
   * associated with it.
   * 
   * @param ruleID
   *          is id of the rule to be delete
   */
  public void deleteRule(long ruleID) {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    ruleDbAdapter.delete(ruleID);

    // Delete all rule actions from database
    Cursor cursorRuleAction = ruleActionDbAdapter.fetchAll(ruleID, null);
    while (cursorRuleAction.moveToNext()) {

      long ruleActionID = getLongFromCursor(cursorRuleAction, RuleActionDbAdapter.KEY_RULEACTIONID);
      ruleActionDbAdapter.delete(ruleActionID);

      // Delete all rule parameters of this rule action
      Cursor cursorRuleActionParameter = ruleActionParameterDbAdapter.fetchAll(ruleActionID, null,
          null);
      while (cursorRuleActionParameter.moveToNext()) {
        long ruleActionParameterID = getLongFromCursor(cursorRuleActionParameter,
            RuleActionParameterDbAdapter.KEY_RULEACTIONPARAMETERID);
        ruleActionParameterDbAdapter.delete(ruleActionParameterID);
      }
      cursorRuleActionParameter.close();

    }
    cursorRuleAction.close();

    // Delete all rule filters from database
    Cursor cursorFilter = ruleFilterDbAdapter.fetchAll(ruleID, null, null, null, null, null);
    while (cursorFilter.moveToNext()) {
      long ruleFilterID = getLongFromCursor(cursorFilter, RuleFilterDbAdapter.KEY_RULEFILTERID);
      ruleFilterDbAdapter.delete(ruleFilterID);
    }
    cursorFilter.close();
  }

  /**
   * Delete certain set of rules
   * 
   * @param rules
   *          is the set of Rules to be deleted
   */
  public void deleteRules(List<? extends Rule> rules) {
    for (Rule rule : rules) {
      deleteRule(rule.getDatabaseId());
    }
  }

  /**
   * Delete all rules
   */
  public void deleteAllRules() {
    deleteRules(getRules());
  }

  /**
   * Update enabled flag of one rule record
   * 
   * @param ruleID
   *          is id of the rule record to be updated
   * 
   * @param enabled
   *          is the enabled flag
   */
  public void setRuleEnabled(long ruleID, boolean enabled) {
    ruleDbAdapter.update(ruleID, null, null, null, enabled, null);
  }

  public List<ModelLog> getEventLogs() {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    Cursor cursor = logEventDbAdapter.fetchAll();
    ArrayList<ModelLog> logList = new ArrayList<ModelLog>(cursor.getCount());

    while (cursor.moveToNext()) {
      Long logID = getLongFromCursor(cursor, LogDbAdapter.KEY_ID);
      Long logTimestamp = getLongFromCursor(cursor, LogDbAdapter.KEY_TIMESTAMP);
      String logName = getStringFromCursor(cursor, LogEventDbAdapter.KEY_EVENTNAME);
      String logDesc = getStringFromCursor(cursor, LogDbAdapter.KEY_DESCRIPTION);
      logList.add(new ModelLog(logID, logName, logDesc, R.drawable.icon_event_unknown,
          logTimestamp, ModelLog.TYPE_EVENT));
    }

    cursor.close();
    return logList;
  }

  public Cursor getEventLogsCursor() {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    Cursor cursor = logEventDbAdapter.fetchAll();
    return cursor;
  }

  /**
   * @return the sharedPreferences to allow for get/setting of user preferences.
   */
  public SharedPreferences getSharedPreferences() {
    return settings;
  }

  public Cursor getEventLogCursor(long eventID) {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    Cursor cursor = logEventDbAdapter.fetch(eventID);
    return cursor;
  }

  public ModelLog getEventLog(long eventID) {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    Cursor cursor = logEventDbAdapter.fetch(eventID);
    long logID = getLongFromCursor(cursor, LogDbAdapter.KEY_ID);
    String logName = getStringFromCursor(cursor, LogEventDbAdapter.KEY_EVENTNAME);
    long logTimestamp = getLongFromCursor(cursor, LogDbAdapter.KEY_TIMESTAMP);
    String logDesc = getStringFromCursor(cursor, LogDbAdapter.KEY_DESCRIPTION);
    ModelLog log = new ModelLog(logID, logName, logDesc, R.drawable.icon_event_unknown,
        logTimestamp, ModelLog.TYPE_EVENT);
    cursor.close();
    return log;
  }

  public void deleteEventLogs() {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    logEventDbAdapter.deleteAll();
  }

  public void deleteActionLogs() {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    logActionDbAdapter.deleteAll();
  }

  public List<ModelLog> getActionLogs() {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    Cursor cursor = logActionDbAdapter.fetchAll();
    ArrayList<ModelLog> logList = new ArrayList<ModelLog>(cursor.getCount());

    while (cursor.moveToNext()) {
      long logID = getLongFromCursor(cursor, LogDbAdapter.KEY_ID);
      String logName = getStringFromCursor(cursor, LogActionDbAdapter.KEY_ACTIONEVENTNAME);
      long logTimestamp = getLongFromCursor(cursor, LogDbAdapter.KEY_TIMESTAMP);
      String logDesc = getStringFromCursor(cursor, LogDbAdapter.KEY_DESCRIPTION);
      logList.add(new ModelLog(logID, logName, logDesc, R.drawable.icon_action_unknown,
          logTimestamp, ModelLog.TYPE_ACTION));
    }
    cursor.close();
    return logList;
  }

  public ModelLog getActionLog(long id) {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    Cursor cursor = logActionDbAdapter.fetch(id);
    long logID = getLongFromCursor(cursor, LogDbAdapter.KEY_ID);
    String logName = getStringFromCursor(cursor, LogActionDbAdapter.KEY_ACTIONEVENTNAME);
    long logTimestamp = getLongFromCursor(cursor, LogDbAdapter.KEY_TIMESTAMP);
    String logDesc = getStringFromCursor(cursor, LogDbAdapter.KEY_DESCRIPTION);
    ModelLog log = new ModelLog(logID, logName, logDesc, R.drawable.icon_action_unknown,
        logTimestamp, ModelLog.TYPE_ACTION);
    cursor.close();
    return log;
  }

  public List<ModelLog> getGeneralLogs() {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    Cursor cursor = logGeneralDbAdapter.fetchAll();
    ArrayList<ModelLog> logList = new ArrayList<ModelLog>(cursor.getCount());

    while (cursor.moveToNext()) {
      long logID = getLongFromCursor(cursor, LogDbAdapter.KEY_ID);
      String logName = getStringFromCursor(cursor, LogGeneralDbAdapter.KEY_DESCRIPTION);
      long logTimestamp = getLongFromCursor(cursor, LogDbAdapter.KEY_TIMESTAMP);
      String logDesc = getStringFromCursor(cursor, LogDbAdapter.KEY_DESCRIPTION);
      logList.add(new ModelLog(logID, logName, logDesc, R.drawable.icon_log_general, logTimestamp,
          ModelLog.TYPE_GENERAL));
    }
    cursor.close();
    return logList;
  }

  public ModelLog getGeneralLog(long id) {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    Cursor cursor = logGeneralDbAdapter.fetch(id);
    long logID = getLongFromCursor(cursor, LogDbAdapter.KEY_ID);
    String logName = getStringFromCursor(cursor, LogGeneralDbAdapter.KEY_DESCRIPTION);
    long logTimestamp = getLongFromCursor(cursor, LogDbAdapter.KEY_TIMESTAMP);
    String logDesc = getStringFromCursor(cursor, LogDbAdapter.KEY_DESCRIPTION);
    ModelLog log = new ModelLog(logID, logName, logDesc, R.drawable.icon_log_general, logTimestamp,
        ModelLog.TYPE_GENERAL);
    cursor.close();
    return log;
  }

  public void deleteGeneralLogs() {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    logGeneralDbAdapter.deleteAll();
  }

  public List<ModelLog> getAllLogs() {
    ArrayList<ModelLog> logs = new ArrayList<ModelLog>();
    logs.addAll(getGeneralLogs());
    logs.addAll(getEventLogs());
    logs.addAll(getActionLogs());
    Collections.sort(logs, new Comparator<ModelLog>() {
      public int compare(ModelLog o1, ModelLog o2) {
        return o1.compareTo(o2);
      }
    });

    return logs;
  }

  public ModelLog getLog(int type, long id) {
    if (type == ModelLog.TYPE_EVENT) {
      return getEventLog(id);
    } else if (type == ModelLog.TYPE_ACTION) {
      return getActionLog(id);
    } else if (type == ModelLog.TYPE_GENERAL) {
      return getGeneralLog(id);
    } else {
      return null;
    }
  }

  public void deleteAllLogs() {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    logEventDbAdapter.deleteAll();
    logActionDbAdapter.deleteAll();
    logGeneralDbAdapter.deleteAll();
  }

  /**
   * 
   * @param eventID
   *          database id
   * 
   * @throws IllegarStateException
   *           when database is closed
   * @return number of rules for event or -1 if no matching eventID.
   */
  public int getRuleCount(Long eventID) {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }
    Cursor cursor = ruleDbAdapter.fetchAll(eventID, null, null, null, null);
    if (cursor != null) {
      int count = cursor.getCount();
      cursor.close();
      return count;
    }
    return -1;
  }

  public void setNotification(Long ruleId, Boolean notification) {
    if (isClosed) {
      throw new IllegalStateException(TAG + " is closed.");
    }

    ruleDbAdapter.update(ruleId, null, null, null, null, notification);
  }

}
