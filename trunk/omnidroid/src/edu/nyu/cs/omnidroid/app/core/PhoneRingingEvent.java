package edu.nyu.cs.omnidroid.app.core;

import java.util.Date;

import edu.nyu.cs.omnidroid.app.core.datatypes.OmniDate;
import android.content.Intent;
import android.util.Log;

/**
 * This class encapsulates an PhoneRinging event. It wraps the intent that triggered this event and
 * provides access to any attribute data associated with it.
 */
public class PhoneRingingEvent extends Event {
  /** Event name (to match record in database) */
  public static final String APPLICATION_NAME = "Phone";
  public static final String EVENT_NAME = "Phone is Ringing";
  public static final String ATTRIBUTE_PHONE_NUMBER = "Phone Number";
  public static final String ATTRIBUTE_PHONE_RING_TIME = "Phone Ring Time";
  public static final String ACTION_NAME = "PHONE_RINGING";

  /** Cache any values that are requested because it is likely they will be asked for again */
  protected String phoneNumber;
  protected String phoneRingTime;

  /**
   * Constructs a new PhoneRinging object that holds an PhoneRinging event fired intent. This intent
   * holds the data needed to check the event against user defined rules.
   * 
   * @param intent
   *          the intent received when the PhoneRinging event was fired
   */
  public PhoneRingingEvent(Intent intent) {
    super(APPLICATION_NAME, EVENT_NAME, intent);
    Date date = new Date(System.currentTimeMillis());
    OmniDate omniDate = new OmniDate(date);
    phoneRingTime = omniDate.toString();
    Log.d("PhoneRingingEvent", "The phoneRingTime is : " + phoneRingTime);
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
    } else if (attributeName.equals(PhoneRingingEvent.ATTRIBUTE_PHONE_RING_TIME)) {
        return phoneRingTime;
      } else {
      throw (new IllegalArgumentException());
    }
  }
}
