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
package edu.nyu.cs.omnidroid.tests;

import java.util.ArrayList;

import android.content.Intent;
import edu.nyu.cs.omnidroid.core.Action;
import edu.nyu.cs.omnidroid.core.Filter;
import edu.nyu.cs.omnidroid.core.Rule;
import edu.nyu.cs.omnidroid.core.SMSReceivedEvent;

// TODO(londinop): Replace with database retrieval code

public class TestData {
  /**
   * Sample phone number and message text for an SMS event
   */
  public static final String TEST_PHONE_NO = "5556";
  public static final String TEST_MESSAGE_TEXT = "The moon in June is a big, big balloon";

  public static Rule getRule() {
    String name = "Auto-Reply DND";
    String eventName = "SMS";
    ArrayList<Filter> filters = new ArrayList<Filter>();
    Filter filter = new Filter(SMSReceivedEvent.ATTRIB_PHONE_NO, TEST_PHONE_NO);
    filters.add(filter);
    ArrayList<Action> actions = new ArrayList<Action>();
    // TODO(Rohit) - add send SMS action here
    
    return new Rule(name, eventName, filters, actions);
  }

  public static Rule getAnotherRule() {
    String name = "Auto-Reply DND";
    String eventName = "SMS";
    ArrayList<Filter> filters = new ArrayList<Filter>();
    Filter filter = new Filter(SMSReceivedEvent.ATTRIB_PHONE_NO, "555-555-5555");
    filters.add(filter);
    ArrayList<Action> actions = new ArrayList<Action>();
    // TODO(Rohit) - add send SMS action here
    
    return new Rule(name, eventName, filters, actions);
  }

  public static ArrayList<Rule> getRulesForEvent(String eventName) {
    ArrayList<Rule> rules = new ArrayList<Rule>();
    rules.add(getRule());
    return rules;
  }

  public static SMSReceivedEvent getSMSEvent() {
    return new SMSReceivedEvent(getIntent(TEST_PHONE_NO, TEST_MESSAGE_TEXT));
  }

  public static SMSReceivedEvent getAnotherSMSEvent() {
    return new SMSReceivedEvent(getIntent("555-555-5555", TEST_MESSAGE_TEXT));
  }

  public static Intent getIntent(String phone, String text) {
    Intent intent = new Intent();
    intent.putExtra(SMSReceivedEvent.ATTRIB_PHONE_NO, phone);
    intent.putExtra(SMSReceivedEvent.ATTRIB_MESSAGE_TEXT, text);
    return intent;
  }
}
