package edu.nyu.cs.omnidroid.tests;

import java.util.ArrayList;

import android.content.Intent;
import edu.nyu.cs.omnidroid.core.Rule;
import edu.nyu.cs.omnidroid.core.SMS;

// Temporary class to be replaced with database code

public class TestData {
  public static final String TEST_PHONE_NO = "908-220-2280";
  public static final String TEST_MESSAGE_TEXT = "The moon in June is a big, big balloon";
  
  public static Rule getRule() {
    String name = "Auto-Reply DND";
    String eventName = "SMS";
    String[] filterTypes = { SMS.ATTRIB_PHONE_NO };
    String[] filterData = { TEST_PHONE_NO };
    String actionAppName = "SMS";
    String[] actionParameters = { SMS.ATTRIB_MESSAGE_TEXT };
    String[] actionParameterData = { "Do not disturb." };

    return new Rule(name, eventName, filterTypes, filterData, actionAppName, actionParameters,
        actionParameterData);
  }
  
  public static Rule getAnotherRule() {
    String name = "Auto-Reply DND";
    String eventName = "SMS";
    String[] filterTypes = { SMS.ATTRIB_PHONE_NO };
    String[] filterData = { "555-555-5555" };
    String actionAppName = "SMS";
    String[] actionParameters = { SMS.ATTRIB_MESSAGE_TEXT };
    String[] actionParameterData = { "Do not disturb." };

    return new Rule(name, eventName, filterTypes, filterData, actionAppName, actionParameters,
        actionParameterData);
  }

  public static ArrayList<Rule> getRulesForEvent(String eventName) {
    ArrayList<Rule> rules = new ArrayList<Rule>();
    rules.add(getRule());
    return rules;
  }
  
  public static SMS getSMSEvent() {
    return new SMS(getIntent(TEST_PHONE_NO, TEST_MESSAGE_TEXT));    
  }
  
  public static SMS getAnotherSMSEvent() {
    return new SMS(getIntent("555-555-5555", TEST_MESSAGE_TEXT));
  }
  
  public static Intent getIntent(String phone, String text) {
    Intent intent = new Intent();
    intent.putExtra(SMS.ATTRIB_PHONE_NO, phone);
    intent.putExtra(SMS.ATTRIB_MESSAGE_TEXT, text);
    return intent;
  }
}
