package edu.nyu.cs.omnidroid.core;

import android.content.Intent;

public class SystemBroadcastedEvent extends Event {
  
  public SystemBroadcastedEvent(Intent intent, SystemEvent systemEvent) {
    super(systemEvent.APPLICATION_NAME, systemEvent.EVENT_NAME, intent);
  }

  /**
   * Looks up attributes associated with this event.
   * 
   * @param attributeName
   *          the name of the attribute associated with this event
   * @return the data associated with the attribute
   * @throws IllegalArgumentException
   *           if the attribute is not of a type supported by this event
   */
  @Override
  public String getAttribute(String attributeName) throws IllegalArgumentException {
    throw (new IllegalArgumentException());
  }

}
