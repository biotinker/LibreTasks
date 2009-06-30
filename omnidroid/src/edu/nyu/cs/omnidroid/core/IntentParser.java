package edu.nyu.cs.omnidroid.core;

import android.content.Intent;

public class IntentParser {
  private IntentParser() { }
  
  public static Event getEvent(Intent intent) {
    Event event = null;
    if (intent.getAction().contains("SMS_RECEIVED")) {
      // Handle SMS event
      event = new SMS(intent);
    }
    return event;
  }
}
