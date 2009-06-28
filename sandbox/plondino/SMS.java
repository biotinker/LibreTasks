package edu.nyu.cs.omnidroid.core;

import android.content.Intent;

public class SMS {
  /** Stores the intent that triggered this event, which contains data associated with it */
  private final Intent intent;

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
    this.intent = intent;
  }

  /**
   * Looks up attributes associated with this event.
   * 
   * @param attributeName
   *          the name of the attribute associated with this event
   * @return the data associated with the attribute
   */
  public String getData(String attributeName) {
    if (attributeName.equals(ATTRIB_PHONE_NO)) {
      return getPhoneNumber();
    } else if (attributeName.equals(ATTRIB_MESSAGE_TEXT)) {
      return getMessageText();
    }
    return null;
  }

  /**
   * Gets the phone number from the SMS intent <br>
   * TODO(londinop) Replace with intent-specific code that retrieves the phone number
   * 
   * @return the phone number of the sender of the SMS
   * 
   */
  private String getPhoneNumber() {
    if (phoneNumber != null) {
      return phoneNumber;
    }
    else {
      phoneNumber = "908-220-2280";
      return phoneNumber;
    }
  }

  /**
   * Gets the message text from the SMS intent <br>
   * TODO(londinop) Replace with intent-specific code that retrieves the message text
   * 
   * @return the phone number of the sender of the SMS
   * 
   */
  private String getMessageText() {
    if (messageText != null) {
      return messageText;
    }
    else {
      messageText = "The moon in June is a big, big balloon.";
      return messageText;
    }    
  }
}
