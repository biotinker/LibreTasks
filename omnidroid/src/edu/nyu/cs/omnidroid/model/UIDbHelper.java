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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.model.db.DataFilterDbAdapter;
import edu.nyu.cs.omnidroid.model.db.DbHelper;
import edu.nyu.cs.omnidroid.model.db.RegisteredActionDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredAppDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredEventAttributeDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredEventDbAdapter;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAction;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelApplication;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAttribute;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelEvent;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelFilter;
import edu.nyu.cs.omnidroid.ui.simple.model.Rule;
import edu.nyu.cs.omnidroid.ui.simple.model.RuleNode;
import edu.nyu.cs.omnidroid.ui.simple.model.RuleSparse;

public class UIDbHelper {
  
  private DataFilterDbAdapter dataFilterDbAdapter;
  private RegisteredAppDbAdapter registeredAppDbAdapter;
  private RegisteredEventDbAdapter registeredEventDbAdapter;
  private RegisteredActionDbAdapter registeredActionDbAdapter;
  private RegisteredEventAttributeDbAdapter registeredEventAttributeDbAdapter;
  private DbHelper omnidroidDbHelper;
  
  public UIDbHelper(Context context) {
    omnidroidDbHelper = new DbHelper(context);
    SQLiteDatabase database = omnidroidDbHelper.getWritableDatabase();
    
    dataFilterDbAdapter = new DataFilterDbAdapter(database);
    registeredAppDbAdapter = new RegisteredAppDbAdapter(database);
    registeredEventDbAdapter = new RegisteredEventDbAdapter(database);
    registeredActionDbAdapter = new RegisteredActionDbAdapter(database);
    registeredEventAttributeDbAdapter = new RegisteredEventAttributeDbAdapter(database);
  }
  
  public void close() {
    omnidroidDbHelper.close();
  }
  
  public ArrayList<ModelEvent> getEvents() {
    ArrayList<ModelEvent> events = new ArrayList<ModelEvent>();
    Cursor cursor = registeredEventDbAdapter.fetchAll();
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();
      events.add(new ModelEvent(
          cursor.getInt(cursor.getColumnIndex(RegisteredEventDbAdapter.KEY_EVENTID)), 
          cursor.getString(cursor.getColumnIndex(RegisteredEventDbAdapter.KEY_EVENTNAME))
          , "", R.drawable.icon_event_unknown));
    }
    cursor.close();
    return events;
  }

  public ArrayList<ModelAttribute> getAttributesForEvent(ModelEvent event) {
    ArrayList<ModelAttribute> attributes = new ArrayList<ModelAttribute>();
    Cursor cursor = registeredEventAttributeDbAdapter.fetchAll(
        null, Long.valueOf(event.getDatabaseId()), null);
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();
      attributes.add(new ModelAttribute(cursor.getInt(cursor
          .getColumnIndex(RegisteredEventAttributeDbAdapter.KEY_EVENTATTRIBUTEID)), cursor
          .getInt(cursor.getColumnIndex(RegisteredEventAttributeDbAdapter.KEY_EVENTID)), cursor
          .getInt(cursor.getColumnIndex(RegisteredEventAttributeDbAdapter.KEY_DATATYPEID)), cursor
          .getString(cursor
              .getColumnIndex(RegisteredEventAttributeDbAdapter.KEY_EVENTATTRIBUTENAME)), "",
          R.drawable.icon_attribute_unknown));
    }
    cursor.close();
    return attributes;
  }
  
  public ArrayList<ModelFilter> getFiltersForAttribute(ModelAttribute attribute) {
    ArrayList<ModelFilter> filters = new ArrayList<ModelFilter>();
    Cursor cursor = dataFilterDbAdapter.fetchAll(null, Long.valueOf(attribute.getDatatype()));
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();
      filters.add(new ModelFilter(
          cursor.getInt(cursor.getColumnIndex(DataFilterDbAdapter.KEY_DATAFILTERID)), 
          cursor.getString(cursor.getColumnIndex(DataFilterDbAdapter.KEY_DATAFILTERNAME)), 
          "", R.drawable.icon_filter_unknown, attribute, null));
    }
    cursor.close();
    return filters;
  }
  
  public ArrayList<ModelApplication> getApplications() {
	  ArrayList<ModelApplication> applications = new ArrayList<ModelApplication>();
	  Cursor cursor = registeredAppDbAdapter.fetchAll();
	  for (int i = 0; i < cursor.getCount(); i++) {
	      cursor.moveToNext();
	      applications.add(new ModelApplication(
	    	  -1, // (replace with real application database id)
	          cursor.getString(cursor.getColumnIndex(RegisteredAppDbAdapter.KEY_APPNAME)), 
	          "", R.drawable.icon_event_unknown));
	  }
	  cursor.close();
	  return applications;
  }
  
  public ArrayList<ModelAction> getActions(ModelApplication application) {
	// TODO: Given a ModelApplication, return all ModelAction associated with it.
	/*
    ArrayList<ModelAction> actions = new ArrayList<ModelAction>();
    Cursor cursor = registeredActionDbAdapter.fetchAll(null, Long.valueOf(application.getDatabaseId()));
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();
      actions.add(new ModelAction(
          cursor.getString(cursor.getColumnIndex(RegisteredActionDbAdapter.KEY_ACTIONNAME)), 
          "", R.drawable.icon_event_unknown, application));
    }
    cursor.close();
    return actions;
    */
	return null;
  }
  
  public ArrayList<RuleSparse> getRules() {
	  // TODO: Return a [simple] list of all rules saved in the database.
	  ArrayList<RuleSparse> rules = null; // = fetchRulesFromDb();
	  return rules;
  }
  
  public Rule loadRule(int databaseId) {
	  // TODO: Load the specified rule from the database.
	  //
	  // If you can give me the following then I can reconstruct the 
	  // Rule instance:
	  // ModelEvent event = getRootEventForRule(databaseId);
	  // ArrayList<ModelFilter> filters = getFiltersForRule(databaseId);
	  // ArrayList<ModelAction> actions = getActionsForRule(databaseId);
	  //
	  Rule rule = new Rule();
	  return rule;
  }
  
  /**
   * Given a rule, try to save it to the database.
   * @param rule
   * @throws Exception
   */
  public void saveRule(Rule rule) throws Exception {
	  // TODO: Save root event.
	  ModelEvent event = (ModelEvent)rule.getRootNode().getItem();
	  
	  // TODO: Save each filter chain. We recursively save each branch.
	  ArrayList<RuleNode> filters = rule.getFilterBranches();
	  for (int i = 0; i < filters.size(); i++) {
		  saveRuleNode(filters.get(i));
	  }
	  
	  // TODO: Save all actions.
	  ArrayList<ModelAction> actions = rule.getActions();
  }
  
  /**
   * Recursively write each node of the filter branches to the database.
   */
  private void saveRuleNode(RuleNode node) {
	  // The filter to commit to the database:
	  ModelFilter filter = (ModelFilter)node.getItem();
	  // TODO: commit filter to database here.
	  
	  // Now all our children filters:
	  for (int i = 0; i < node.getChildren().size(); i++) {
		  saveRuleNode(node.getChildren().get(i));
	  }
  }
}