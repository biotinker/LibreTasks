package edu.nyu.cs.omnidroid.core;

import android.content.Intent;
import android.os.Bundle;

/**
 * This class encapsulates an SMS event. It wraps the intent that triggered this event and provides
 * access to any attribute data associated with it.
 */
public class SMS extends Event {
  /** Event name (to match record in database) */
  public static final String EVENT_NAME = "SMS";
  
  /** Attribute field names */
  public static final String ATTRIB_PHONE_NO = "Phone Number";
  public static final String ATTRIB_MESSAGE_TEXT = "Message Text";

  /** Cache any values that are requested because it is likely they will be asked for again */
  private String phoneNumber = null;
  private String messageText = null;

  /**
   * Constructs a new SMS object that holds an SMS event fired intent. This intent holds the data
   * needed to check the event against user defined rules.
   * 
   * @param intent
   *          the intent received when the SMS received event was fired by and external app
   */
  public SMS(Intent intent) {
    super(EVENT_NAME, intent);
  }

  /**
   * Looks up attributes associated with this event.
   * 
   * @param attributeName
   *          the name of the attribute associated with this event
   * @return the data associated with the attribute
   */
  @Override
  public String getAttribute(String attributeName) {
    if (attributeName.equals(ATTRIB_PHONE_NO)) {
      return getPhoneNumber();
    } else if (attributeName.equals(ATTRIB_MESSAGE_TEXT)) {
      return getMessageText();
    } else {
      return null;
    }
    // TODO(londinop): Add exception for invalid data field name
  }

  /**
   * Gets the phone number from the SMS intent
   * 
   * @return the phone number of the sender of the SMS
   * 
   */
  private String getPhoneNumber() {
    // TODO(londinop) Replace with intent-specific code that retrieves the phone number
    if (phoneNumber != null) {
      return phoneNumber;
    } else {
      Bundle b = intent.getExtras();
      phoneNumber = b.getString(ATTRIB_PHONE_NO);
      return phoneNumber;
    }
  }

  /**
   * Gets the message text from the SMS intent
   * 
   * @return the phone number of the sender of the SMS
   * 
   */
  private String getMessageText() {
    // TODO(londinop) Replace with intent-specific code that retrieves the message text
    if (messageText != null) {
      return messageText;
    } else {
      Bundle b = intent.getExtras();
      messageText = b.getString(ATTRIB_MESSAGE_TEXT);
      return messageText;
    }
  }
}
