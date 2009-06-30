package edu.nyu.cs.omnidroid.core;

import android.content.Intent;

/**
 * This class encapsulates a generic event. It wraps the intent that triggered this event and
 * provides access to any attribute data associated with it. Event should be overridden by a class
 * that describes the actual event and provides access to its specific data attributes.
 */
public abstract class Event {
  /** The name of the event for data lookup */
  private final String eventName;

  /** Stores the intent that triggered this event, which contains data associated with it */
  protected final Intent intent;

  /**
   * Create a new event based on the received intent.
   * 
   * @param eventName
   *          the name of the event for data lookup
   * @param intent
   *          the intent that triggered this event
   */
  public Event(String eventName, Intent intent) {
    this.intent = intent;
    this.eventName = eventName;
  }

  /**
   * Returns the name of the event
   * @return name of the event
   */
  public String getName() {
    return eventName;
  }

  /**
   * Looks up attributes associated with this event.
   * 
   * @param attributeName
   *          the name of the attribute associated with this event
   * @return the data associated with the attribute
   */
  public abstract String getAttribute(String attributeName);
  // TODO(londinop): Add exception for invalid attribute name
  
}