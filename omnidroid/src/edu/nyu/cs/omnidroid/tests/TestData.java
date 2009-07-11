package edu.nyu.cs.omnidroid.tests;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import edu.nyu.cs.omnidroid.core.Action;
import edu.nyu.cs.omnidroid.core.Filter;
import edu.nyu.cs.omnidroid.core.Rule;
import edu.nyu.cs.omnidroid.core.SMSReceivedEvent;
import edu.nyu.cs.omnidroid.core.SendSmsAction;

// Temporary class to be replaced with database code

public class TestData {
  public static final String TEST_PHONE_NO = "5556";
  public static final String TEST_MESSAGE_TEXT = "The moon in June is a big, big balloon";
  
  public static Rule getRule() {
    String name = "Auto-Reply DND";
    String eventName = "SMS";
    HashMap<String, String> parameters = new HashMap<String, String>();
    parameters.put(SendSmsAction.PARAM_PHONE_NO, TEST_PHONE_NO);
    ArrayList<Filter> filters = new ArrayList<Filter>();
    Filter filter = new Filter(SMSReceivedEvent.ATTRIB_PHONE_NO, TEST_PHONE_NO);
    filters.add(filter);
    ArrayList<Action> actions = new ArrayList<Action>();
    SendSmsAction action = new SendSmsAction(parameters);
    actions.add(action);

    return new Rule(name, eventName, filters, actions);
  }

  public static Rule getAnotherRule() {
    String name = "Auto-Reply DND";
    String eventName = "SMS";
    HashMap<String, String> parameters = new HashMap<String, String>();
    parameters.put(SendSmsAction.PARAM_PHONE_NO, TEST_PHONE_NO);
    ArrayList<Filter> filters = new ArrayList<Filter>();
    Filter filter = new Filter(SMSReceivedEvent.ATTRIB_PHONE_NO, "555-555-5555");
    filters.add(filter);
    ArrayList<Action> actions = new ArrayList<Action>();
    SendSmsAction action = new SendSmsAction(parameters);
    actions.add(action);

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

  public static String getHexString(byte[] b) throws Exception {
    String result = "";
    for (int i = 0; i < b.length; i++) {
      result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
    }
    return result;
  }
}
