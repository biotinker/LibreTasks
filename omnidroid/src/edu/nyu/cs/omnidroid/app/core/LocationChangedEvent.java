package edu.nyu.cs.omnidroid.app.core;

import android.content.Intent;

/**
 * This class encapsulates an LocationChanged event. It wraps the intent that triggered this event
 * and provides access to any attribute data associated with it.
 */
public class LocationChangedEvent extends Event {
  /** Event name (to match record in database) */
  public static final String APPLICATION_NAME = "GPS";
  public static final String EVENT_NAME = "GPS Location Changed";
  public static final String ACTION_NAME = "LOCATION_CHANGED";
  public static final String ATTRIBUTE_CURRENT_LOCATION = "Current Location";

  /** Cache any values that are requested because it is likely they will be asked for again */
  protected String location;

  /**
   * Constructs a new LocationChanged object that holds an LocationChanged event fired intent. This
   * intent holds the data needed to check the event against user defined rules.
   * 
   * @param intent
   *          the intent received when the LocationChanged event was fired
   */
  public LocationChangedEvent(Intent intent) {
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
    // TODO (dvo203): Replace by a generic method in a super class.
    if (attributeName.equals(LocationChangedEvent.ATTRIBUTE_CURRENT_LOCATION)) {
      if (location == null) {
        location = intent.getStringExtra(LocationChangedEvent.ATTRIBUTE_CURRENT_LOCATION);
      }
      return location;
    } else {
      throw (new IllegalArgumentException());
    }
  }
}
