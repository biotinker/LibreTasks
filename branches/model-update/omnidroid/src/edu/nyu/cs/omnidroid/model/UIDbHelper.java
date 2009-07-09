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
import edu.nyu.cs.omnidroid.model.db.RegisteredEventAttributeDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredEventDbAdapter;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAction;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelApplication;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAttribute;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelEvent;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelFilter;
import edu.nyu.cs.omnidroid.ui.simple.model.Rule;
import edu.nyu.cs.omnidroid.ui.simple.model.RuleNode;

public class UIDbHelper {
  
  private DataFilterDbAdapter dataFilterDbAdapter;
  private RegisteredEventDbAdapter registeredEventDbAdapter;
  private RegisteredActionDbAdapter registeredActionDbAdapter;
  private RegisteredEventAttributeDbAdapter registeredEventAttributeDbAdapter;
  private DbHelper omnidroidDbHelper;
  
  public UIDbHelper(Context context) {
    omnidroidDbHelper = new DbHelper(context);
    SQLiteDatabase database = omnidroidDbHelper.getWritableDatabase();
    
    dataFilterDbAdapter = new DataFilterDbAdapter(database);
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
	  // TODO: Return all applications that have usable actions.
	  /*
	  ArrayList<ModelApplication> applications = new ArrayList<ModelApplication>();
	  Cursor cursor = registeredApplicationDbAdapter.fetchAll();
	  for (int i = 0; i < cursor.getCount(); i++) {
	      cursor.moveToNext();
	      applications.add(new ModelApplication(
	          cursor.getString(cursor.getColumnIndex(RegisteredActionDbAdapter.KEY_APPLICATIONNAME)), 
	          "", R.drawable.icon_event_unknown));
	  }
	  cursor.close();
	  return applications;
	  */
	  return null;
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
  
  public ArrayList<Rule> getRules() {
	  // TODO: Return a list of all rules saved in the database.
	  // If you can give me a flat list of the event, all filters,
	  // all actions associated with a Rule, I can construct it
	  // as a tree in-memory here, something like:
	  //
	  // for (every rule in database) {
	  // 
	  //   ModelEvent             event   = getRootEventForRule(ruleid);
	  //   ArrayList<ModelFilter> filters = getFiltersForRule(ruleid);
	  //   ArrayList<ModelAction> actions = getActionsForRule(ruleid);
	  //
	  //   // Then I will construct this rule:
	  //   restoreRule(event, filters, actions);
	  // }
	  return null;
  }
  
  public void saveRule(Rule rule) throws Exception {
	  // TODO: Given a rule, try to save it to the database.
	  // 1) First check if the rule has a valid ID - if so, delete all preexisting
	  //    records associated with this rule before saving.
	  // ...
	  //
	  // 2) Save the Rule to the Rule table, I'm guessing this would be something
	  //    like just a rule name, creation date timestamp etc.
	  //
	  // 3) Recursively iterate over all nodes, adding each node to the database.
	  saveRuleNode(rule.getRootNode());  
  }
  
  /**
   * Recursively write each node of the rule to the database.
   * We can use instanceof like below, or add a writeToDatabase()
   * method for each subclass of <code>ModelItem</code> to make
   * this cleaner.
   */
  private void saveRuleNode(RuleNode node) {
	  if (node.getItem() instanceof ModelEvent) {
          ModelEvent event = (ModelEvent)node.getItem();	
          // TODO: Write this event to the database.
	  }
	  else if (node.getItem() instanceof ModelFilter) {
		  ModelFilter filter = (ModelFilter)node.getItem();
		  // TODO: Write this filter to the database.
		  // filter.getFilterData() will give you the associated OmniData.
	  }
	  else if (node.getItem() instanceof ModelAction) {
		  ModelAction action = (ModelAction)node.getItem();
		  // TODO: Write this action to the database.
	  }
	  
	  // Now all children.
	  for (int i = 0; i < node.getChildren().size(); i++) {
		  saveRuleNode(node.getChildren().get(i));
	  }
  }
}