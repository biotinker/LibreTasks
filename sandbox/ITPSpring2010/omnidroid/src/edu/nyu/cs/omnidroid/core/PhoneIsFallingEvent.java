package edu.nyu.cs.omnidroid.core;

import android.content.Intent;

/**
 * This class encapsulates a PhoneIsFalling event. It wraps the intent that triggered this event and
 * provides access to any attribute data associated with it.
 */
public class PhoneIsFallingEvent extends Event {
  /** Event name (to match record in database) */
  public static final String APPLICATION_NAME = "Sensor";
  public static final String EVENT_NAME = "Phone Is Falling";
  public static final String ATTRIBUTE_ACCELERATIONS = "Accelerations";
  public static final String ACTION_NAME = "PHONE_IS_FALLING";

  /**
   * Constructs a new PhoneIsFalling object that holds an PhoneIsFalling event fired intent. This
   * intent holds the data needed to check the event against user defined rules.
   * 
   * @param intent
   *          the intent received when the PhoneIsFalling event was fired
   */
  public PhoneIsFallingEvent(Intent intent) {
    super(APPLICATION_NAME, EVENT_NAME, intent);
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
    throw new IllegalArgumentException("Event has no arguments.");
  }
}
