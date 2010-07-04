/*******************************************************************************
 * Copyright 2009 Omnidroid - http://code.google.com/p/omnidroid
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
 * This class encapsulates an TimeTick event. It wraps the intent that triggered this event and
 * provides access to any attribute data associated with it.
 */
public class TimeTickEvent extends Event {
  /** Event name (to match record in database) */
  //TODO(Roger): store human read strings in String.xml
  public static final String APPLICATION_NAME = "Android";
  public static final String EVENT_NAME = "Time is";
  public static final String ATTRIBUTE_CURRENT_TIME = "Current Time";
  public static final String ACTION_NAME = "TIME_TICK";

  /** Cache any values that are requested because it is likely they will be asked for again */
  protected String currentTime;

  /**
   * Constructs a new TimeTickEvent object that holds an TimeTick event fired intent. This intent
   * holds the data needed to check the event against user defined rules.
   * 
   * @param intent
   *          the intent received when the TimeTick event was fired
   */
  public TimeTickEvent(Intent intent) {
    super(APPLICATION_NAME, EVENT_NAME, intent);
    Date date = new Date(System.currentTimeMillis());
    OmniDate omniDate = new OmniDate(date);
    currentTime = omniDate.toString();
    Log.d("TimeTickEvent", "The currentTime is : " + currentTime);
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
    if (attributeName.equals(TimeTickEvent.ATTRIBUTE_CURRENT_TIME)) {
      return currentTime;
    } else {
      throw (new IllegalArgumentException());
    }
  }
}
