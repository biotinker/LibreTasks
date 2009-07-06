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
package edu.nyu.cs.omnidroid.ui.simple;

import java.util.ArrayList;

import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAction;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelAttribute;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelEvent;
import edu.nyu.cs.omnidroid.ui.simple.model.ModelFilter;

/**
 * This class is supposed to represent the database interface for supplying UI instances of events,
 * attributes, and filters.
 * 
 * We want to get rid of this ASAP and have the UI pull from the actual database!!!
 */
public class DummyDatabase {

  public static final int DATATYPE_PHONENUMBER = 0;
  public static final int DATATYPE_TEXT = 1;

  public static final int FILTER_ID_PHONE_NUMBER_EQUALS = 0;
  public static final int FILTER_ID_TEXT_EQUALS = 1;
  public static final int FILTER_ID_TEXT_CONTAINS = 2;

  private DummyDatabase() {
  }

  /**
   * The database should provide us with a master list of all supported events.
   * 
   * @return A cache-able list of events.
   */
  public static ArrayList<ModelEvent> getEvents() {
    ArrayList<ModelEvent> events = new ArrayList<ModelEvent>();
    events.add(new ModelEvent(0, "SMS Received", "An SMS receive event",
        R.drawable.icon_event_unknown));
    events.add(new ModelEvent(1, "Phone Call Received", "A phone call receive event",
        R.drawable.icon_event_unknown));
    return events;
  }

  /**
   * The database should provide us with a master list of all supported actions. This may depend on
   * what event was chosen as the root event, not sure yet.
   * 
   * @return A cache-able list of actions.
   */
  public static ArrayList<ModelAction> getActions() {
    ArrayList<ModelAction> actions = new ArrayList<ModelAction>();
    actions.add(new ModelAction("SMS Send", "Send an SMS", R.drawable.icon_action_unknown));
    return actions;
  }

  /**
   * The database should provide us with a list of attributes linked to a given event type.
   * 
   * @param event
   *          The event for which we want all associated attributes.
   * @return A list of all attributes associated with the given event.
   */
  public static ArrayList<ModelAttribute> getAttributesForEvent(ModelEvent event) {
    ArrayList<ModelAttribute> attributes = new ArrayList<ModelAttribute>();
    if (event.getDatabaseId() == 0) { // Database ID for SMS received event.
      attributes.add(new ModelAttribute(0, 0, DATATYPE_PHONENUMBER, "SMS Phonenumber", "tbd",
          R.drawable.icon_attribute_unknown));
      attributes.add(new ModelAttribute(1, 0, DATATYPE_TEXT, "SMS Text", "tbd",
          R.drawable.icon_attribute_unknown));
    } else if (event.getDatabaseId() == 1) { // Database ID for phone call received event.
      attributes.add(new ModelAttribute(2, 1, DATATYPE_PHONENUMBER, "Phonenumber", "tbd",
          R.drawable.icon_attribute_unknown));
    }
    // TODO: Add all generic system attributes to returned attributes list.
    return attributes;
  }

  /**
   * The database should give provide us with a list of filters linked to an attribute's associated
   * data type.
   * 
   * @param attribute
   *          The attribute for which we want all associated filters.
   * @return A list of filters associated with the given attribute.
   */
  public static ArrayList<ModelFilter> getFiltersForAttribute(ModelAttribute attribute) {
    // Again this is just cheating, we'd actually want to check the associated
    // data type.
    ArrayList<ModelFilter> filters = new ArrayList<ModelFilter>();
    if (attribute.getDatatype() == DATATYPE_PHONENUMBER) {
      filters.add(new ModelFilter(FILTER_ID_PHONE_NUMBER_EQUALS, "Equals", "tbd",
          R.drawable.icon_filter_unknown, attribute, null));
    } else if (attribute.getDatatype() == DATATYPE_TEXT) {
      filters.add(new ModelFilter(FILTER_ID_TEXT_EQUALS, "Equals", "tbd",
          R.drawable.icon_filter_unknown, attribute, null));
      filters.add(new ModelFilter(FILTER_ID_TEXT_CONTAINS, "Contains", "tbd",
          R.drawable.icon_filter_unknown, attribute, null));
    }
    return filters;
  }
}