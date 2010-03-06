package edu.nyu.cs.omnidroid.core;

import android.content.Intent;

public class AndroidEvent extends Event {

  public AndroidEvent(String appName, String eventName, Intent intent) {
    super(appName, eventName, intent);
    // TODO Auto-generated constructor stub
  }

  @Override
  public String getAttribute(String attributeName) throws IllegalArgumentException {
    // TODO Auto-generated method stub
    return null;
  }

}
