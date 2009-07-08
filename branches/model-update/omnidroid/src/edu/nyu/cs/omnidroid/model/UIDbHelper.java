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
import edu.nyu.cs.omnidroid.model.db.DataTypeDbAdapter;
import edu.nyu.cs.omnidroid.model.db.DbHelper;
import edu.nyu.cs.omnidroid.model.db.RegisteredActionDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredAppDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredEventAttributeDbAdapter;
import edu.nyu.cs.omnidroid.model.db.RegisteredEventDbAdapter;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAction;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAttribute;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelEvent;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelFilter;

public class UIDbHelper {
  
  private DataTypeDbAdapter dataTypeDbAdapter;
  private DataFilterDbAdapter dataFilterDbAdapter;
  private RegisteredAppDbAdapter registeredAppDbAdapter;
  private RegisteredEventDbAdapter registeredEventDbAdapter;
  private RegisteredActionDbAdapter registeredActionDbAdapter;
  private RegisteredEventAttributeDbAdapter registeredEventAttributeDbAdapter;
  private DbHelper omnidroidDbHelper;
  
  public UIDbHelper(Context context) {
    omnidroidDbHelper = new DbHelper(context);
    SQLiteDatabase database = omnidroidDbHelper.getWritableDatabase();
    
    dataTypeDbAdapter = new DataTypeDbAdapter(database);
    dataFilterDbAdapter = new DataFilterDbAdapter(database);
    registeredAppDbAdapter = new RegisteredAppDbAdapter(database);
    registeredEventDbAdapter = new RegisteredEventDbAdapter(database);
    registeredActionDbAdapter = new RegisteredActionDbAdapter(database);
    registeredEventAttributeDbAdapter = new RegisteredEventAttributeDbAdapter(database);
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
    return events;
  }
  
  public ArrayList<ModelAction> getActions() {
    ArrayList<ModelAction> actions = new ArrayList<ModelAction>();
    Cursor cursor = registeredActionDbAdapter.fetchAll();
    for (int i = 0; i < cursor.getCount(); i++) {
      cursor.moveToNext();
      actions.add(new ModelAction(
          cursor.getString(cursor.getColumnIndex(RegisteredActionDbAdapter.KEY_ACTIONNAME)), 
          "", R.drawable.icon_action_unknown));
    }
    return actions;
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
    return filters;
  }
}