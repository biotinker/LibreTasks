/*******************************************************************************
 * Copyright 2010 OmniDroid - http://code.google.com/p/omnidroid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package edu.nyu.cs.omnidroid.app.controller.events;

import java.util.Date;
import edu.nyu.cs.omnidroid.app.controller.Event;
import edu.nyu.cs.omnidroid.app.controller.datatypes.OmniDate;
import android.content.Intent;
import android.util.Log;

/**
 * Abstract class that encapsulates a phone call event. It wraps the intent that triggered this
 * event and provides access to any attribute data associated with it.
 */
public abstract class PhoneCallEvent extends Event {
  /** Event name (to match record in database) */
  // TODO: (renctan) Replace string with constant from database
  public static final String APPLICATION_NAME = "Phone";

  /**
   * Name for the attribute for storing the time when this event occurred.
   * 
   * Note: This was originally named ATTRIBUTE_PHONE_RING_TIME, and the string literal was left as
   * is to make it compatible with existing databases.
   */
  public static final String ATTRIBUTE_TIMESTAMP = "Phone Ring Time";

  /** Cache any values that are requested because it is likely they will be asked for again */
  protected String phoneNumber;
  protected String phoneRingTime;

  private final static String LOG_TAG = PhoneCallEvent.class.getSimpleName();

  /**
   * Constructs a new PhoneRinging object that holds a PhoneRinging event fired intent. This intent
   * holds the data needed to check the event against user defined rules.
   * 
   * @param intent
   *          the intent received when the PhoneRinging event was fired
   */
  public PhoneCallEvent(String eventName, Intent intent) {
    super(APPLICATION_NAME, eventName, intent);

    Date date = new Date(System.currentTimeMillis());
    OmniDate omniDate = new OmniDate(date);
    phoneRingTime = omniDate.toString();
    Log.d(LOG_TAG, "time at " + phoneRingTime);
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
    if (attributeName.equals(PhoneRingingEvent.ATTRIBUTE_TIMESTAMP)) {
      return phoneRingTime;
    } else {
      throw (new IllegalArgumentException());
    }
  }
}
