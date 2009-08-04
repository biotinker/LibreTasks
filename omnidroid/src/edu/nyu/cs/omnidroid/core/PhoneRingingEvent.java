package edu.nyu.cs.omnidroid.core;

import edu.nyu.cs.omnidroid.model.DbData;
import android.content.Intent;

/**
 * This class encapsulates an PhoneRinging event. It wraps the intent that triggered this event and
 * provides access to any attribute data associated with it.
 */
public class PhoneRingingEvent extends Event {
  /** Event name (to match record in database) */
  public static final String APPLICATION_NAME = DbData.APP_PHONE;
  public static final String EVENT_NAME = DbData.EVENT_PHONE_RING;
  public static final String ATTRIBUTE_PHONE_NUMBER = DbData.ATTR_PHONE_PHONE_NUMBER;
  public static final String ACTION_NAME = "PHONE_RINGING";

  /** Cache any values that are requested because it is likely they will be asked for again */
  protected String phoneNumber;

  /**
   * Constructs a new PhoneRinging object that holds an PhoneRinging event fired intent. This intent
   * holds the data needed to check the event against user defined rules.
   * 
   * @param intent
   *          the intent received when the PhoneRinging event was fired
   */
  public PhoneRingingEvent(Intent intent) {
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
    if (attributeName.equals(PhoneRingingEvent.ATTRIBUTE_PHONE_NUMBER)) {
      if (phoneNumber == null) {
        phoneNumber = intent.getStringExtra(PhoneRingingEvent.ATTRIBUTE_PHONE_NUMBER);
      }
      return phoneNumber;
    } else {
      throw (new IllegalArgumentException());
    }
  }
}
