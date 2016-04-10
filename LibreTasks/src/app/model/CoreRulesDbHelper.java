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

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import libretasks.app.controller.Filter;
import libretasks.app.controller.Rule;
import libretasks.app.controller.util.ExceptionMessageMap;
import libretasks.app.controller.util.OmnidroidRuntimeException;
import libretasks.app.controller.util.Tree;
import libretasks.app.model.db.DataFilterDbAdapter;
import libretasks.app.model.db.DataTypeDbAdapter;
import libretasks.app.model.db.DbHelper;
import libretasks.app.model.db.RegisteredAppDbAdapter;
import libretasks.app.model.db.RegisteredEventAttributeDbAdapter;
import libretasks.app.model.db.RegisteredEventDbAdapter;
import libretasks.app.model.db.RuleDbAdapter;
import libretasks.app.model.db.RuleFilterDbAdapter;

/**
 * This class serves as a access layer to the database for Omnidroid's {@code Rule} data model
 * representation.
 */
public class CoreRulesDbHelper {
  private static final String TAG = CoreActionsDbHelper.class.getSimpleName();
  private DbHelper dbHelper;
  private SQLiteDatabase database;

  private RegisteredAppDbAdapter applicationDbAdapter;
  private RegisteredEventDbAdapter eventDbAdapter;
  private RegisteredEventAttributeDbAdapter eventAttributeDbAdapter;
  private RuleDbAdapter ruleDbAdapter;
  private RuleFilterDbAdapter filterDbAdapter;
  private DataFilterDbAdapter filterComparisonDbAdapter;
  private DataTypeDbAdapter filterDataTypeDbAdapter;

  private final long rootID = -1;

  /**
   * Creates a new CoreDbHelper within the current context and initializes all necessary database
   * adapters.
   * 
   * @param context
   *          context for the application database resource
   */
  public CoreRulesDbHelper(Context context) {
    dbHelper = new DbHelper(context);
    database = dbHelper.getWritableDatabase();

    applicationDbAdapter = new RegisteredAppDbAdapter(database);
    eventDbAdapter = new RegisteredEventDbAdapter(database);
    eventAttributeDbAdapter = new RegisteredEventAttributeDbAdapter(database);
    ruleDbAdapter = new RuleDbAdapter(database);
    filterDbAdapter = new RuleFilterDbAdapter(database);
    filterComparisonDbAdapter = new DataFilterDbAdapter(database);
    filterDataTypeDbAdapter = new DataTypeDbAdapter(database);
  }

  /**
   * Builds a list of {@link Rule} objects from the data stored in the Omnidroid database.
   * 
   * @param eventName
   *          the name of the event to be matched in the database
   * @return a list of rules and their associated filter tree that matches the provided event
   * @throws IllegalStateException
   *           when this object is already closed
   */
  public ArrayList<Rule> getRulesMatchingEvent(String appName, String eventName) {
    if (appName == null || eventName == null) {
      throw new OmnidroidRuntimeException(140000, ExceptionMessageMap
          .getMessage(new Integer(140000).toString()));
    } else if (!database.isOpen()) {
      throw new IllegalStateException(TAG + " is already closed.");
    }

    ArrayList<Rule> rules = new ArrayList<Rule>();

    // Use the appName and eventName to find a unique event in the database
    Cursor appCursor = applicationDbAdapter.fetchAll(appName, null, true);

    if (appCursor.getCount() == 0) {
      Log.d(TAG, "No enabled applications match this event's application " + appName);
      appCursor.close();
      return rules;
    }

    appCursor.moveToFirst();
    long appID = CursorHelper.getLongFromCursor(appCursor, RegisteredAppDbAdapter.KEY_APPID);
    Cursor eventCursor = eventDbAdapter.fetchAll(eventName, appID);
    appCursor.close();

    if (eventCursor.getCount() == 0) {
      Log.d(TAG, "This application does not have an event matching this event's name");
      eventCursor.close();
      return rules;
    }

    // Gets the event id to retrieve the list of rules that match it
    eventCursor.moveToFirst();
    long eventID = CursorHelper
        .getLongFromCursor(eventCursor, RegisteredEventDbAdapter.KEY_EVENTID);
    eventCursor.close();

    // Fetch all rules that match this event and are enabled
    Cursor ruleTable = ruleDbAdapter.fetchAll(eventID, null, null, true, null);

    if (ruleTable.getCount() == 0) {
      Log.d(TAG, "No rules matched this event, return empty list");
      ruleTable.close();
      return rules;
    }

    // Build a rule for each row in the database and add it to the rule list
    while (ruleTable.moveToNext()) {
      rules.add(getRule(ruleTable));
    }

    ruleTable.close();
    return rules;
  }

  /**
   * Retrieves a {@link Rule} and associated {@link Filter}s from the database and returns it in a
   * Rule data structure.
   * 
   * @param ruleRecord
   *          a {@link Cursor} that points to the rule record to retrieve from the database.
   * @return a Rule object built from the database record
   */
  private Rule getRule(Cursor ruleRecord) {
    long ruleID = CursorHelper.getLongFromCursor(ruleRecord, RuleDbAdapter.KEY_RULEID);
    String ruleName = CursorHelper.getStringFromCursor(ruleRecord, RuleDbAdapter.KEY_RULENAME);
    boolean notify = CursorHelper.getBooleanFromCursor(ruleRecord, RuleDbAdapter.KEY_NOTIFICATION);
  
    // Get all filters that belong to this rule
    Cursor filterTable = filterDbAdapter.fetchAll(ruleID, null, null, null, null, null);
    Tree<Filter> filterTree = buildFilterTree(filterTable);

    filterTable.close();
    return new Rule(ruleName, ruleID, filterTree, notify);
  }

  /**
   * Builds a {@link Tree} of {@link Filters} from a table of filters.
   * 
   * @param filterTable
   *          a table of filters where each filter has a reference to its parent filter
   * @return a tree of filters whose structure represents the and/or relationships between the
   *         filters, or null if there are no filters for this rule
   */
  private Tree<Filter> buildFilterTree(Cursor filterTable) {

    if (filterTable.getCount() == 0) {
      return null;
    }

    Tree<Filter> root = new Tree<Filter>(null, null);
    Tree<Filter> parentNode;

    // Keep track of processed filters
    HashMap<Long, Tree<Filter>> visited = new HashMap<Long, Tree<Filter>>();
    visited.put(rootID, root);

    // Iterate through each filter for this rule and construct a tree
    while (filterTable.moveToNext()) {
      long filterID = CursorHelper.getLongFromCursor(filterTable,
          RuleFilterDbAdapter.KEY_RULEFILTERID);

      Cursor currentFilter;
      currentFilter = filterDbAdapter.fetch(filterID);

      while (!visited.containsKey(filterID)) {
        currentFilter.moveToFirst();

        // Create a new Filter node from the database
        Filter filter = getFilter(currentFilter);
        Tree<Filter> newNode = new Tree<Filter>(null, filter);

        visited.put(filterID, newNode);

        long parentID = CursorHelper.getLongFromCursor(currentFilter,
            RuleFilterDbAdapter.KEY_PARENTRULEFILTERID);
        filterID = parentID;

        /*
         * Create a branch of descending nodes until we find a visited node to hook it to or reach
         * the root
         */
        if (!visited.containsKey(parentID)) {
          /* TODO(londinop) Can a filter be stored before its parent filter? */
          parentNode = new Tree<Filter>(null, null);
          parentNode.addSubTree(newNode);
          newNode = parentNode;

          currentFilter = filterDbAdapter.fetch(filterID);
          currentFilter.moveToFirst();
        } else {
          parentNode = visited.get(filterID);
          parentNode.addSubTree(newNode);
        }
      }

      currentFilter.close();
    }
    return root;
  }

  /**
   * Populate a {@link Filter} from the RuleFilters, FilterData, and FilterType database tables
   * 
   * @param filterRecord
   *          a {@link Cursor} that points to the filter record to retrieve from the database.
   * @return a Filter built from the database records
   */
  private Filter getFilter(Cursor filterRecord) {
    Cursor cursor;

    // Gets the name of the event attribute for this filter
    cursor = eventAttributeDbAdapter.fetch(CursorHelper.getLongFromCursor(filterRecord,
        RuleFilterDbAdapter.KEY_EVENTATTRIBUTEID));
    cursor.moveToFirst();
    String eventAttributeName = CursorHelper.getStringFromCursor(cursor,
        RegisteredEventAttributeDbAdapter.KEY_EVENTATTRIBUTENAME);

    // Gets the comparison filter of the OmniType for this filter
    cursor = filterComparisonDbAdapter.fetch(CursorHelper.getLongFromCursor(filterRecord,
        RuleFilterDbAdapter.KEY_DATAFILTERID));
    cursor.moveToFirst();
    String comparison = CursorHelper.getStringFromCursor(cursor,
        DataFilterDbAdapter.KEY_DATAFILTERNAME);

    long filterOnDataTypeID = CursorHelper.getLongFromCursor(cursor,
        DataFilterDbAdapter.KEY_FILTERONDATATYPEID);
    long compareWithDataTypeID = CursorHelper.getLongFromCursor(cursor,
        DataFilterDbAdapter.KEY_COMPAREWITHDATATYPEID);

    // Gets the OmniType of the event attribute to be compared
    cursor = filterDataTypeDbAdapter.fetch(filterOnDataTypeID);
    String filterOnDataType = CursorHelper.getStringFromCursor(cursor,
        DataTypeDbAdapter.KEY_DATATYPECLASSNAME);

    // Gets the OmniType of the user filter data
    cursor = filterDataTypeDbAdapter.fetch(compareWithDataTypeID);
    String compareWithDataType = CursorHelper.getStringFromCursor(cursor,
        DataTypeDbAdapter.KEY_DATATYPECLASSNAME);

    // Gets the data to be compared using this filter
    String data = CursorHelper.getStringFromCursor(filterRecord,
        RuleFilterDbAdapter.KEY_RULEFILTERDATA);

    cursor.close();

    return new Filter(eventAttributeName, filterOnDataType, comparison, compareWithDataType, data);
  }

  /**
   * Close this database helper object. Attempting to use this object after this call will cause an
   * {@link IllegalStateException} being raised.
   */
  public void close() {
    Log.i(TAG, "closing database.");
    database.close();
    
    // Not necessary, but also close all omnidroidDbHelper databases just in case.
    dbHelper.close();
  }

  public int getActiveRuleCount() {
    // Fetch all rules that are enabled
    Cursor ruleTable = ruleDbAdapter.fetchAll(null, null, null, new Boolean(true), null);
    int ruleCount = ruleTable.getCount();
    ruleTable.close();
    return ruleCount;
  }
}
