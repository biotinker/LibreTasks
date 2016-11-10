/*******************************************************************************
 * Copyright 2009, 2010 Omnidroid - http://code.google.com/p/omnidroid
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
package libretasks.app.controller;

import android.content.Intent;

/**
 * This class encapsulates a generic event. It wraps the {@link android.content.Intent} that
 * triggered this event and provides access to any attribute data associated with it. Event should
 * be overridden by a class that describes the actual event and provides access to its specific data
 * attributes.
 */
public abstract class Event {
  // Constants used in display and attribute tags
  public static final String ATTRIBUTE_LOCATION = "Location";
  public static final String ATTRIBUTE_TIME = "Time";

  /** The name of the event for data lookup */
  private final String appName;
  private final String eventName;

  protected final String timeAttribute;
  protected final String locationAttribute;

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
  public Event(String appName, String eventName, Intent intent) {
    this.intent = intent;

    if (intent.hasExtra(ATTRIBUTE_TIME)) {
      timeAttribute = intent.getStringExtra(ATTRIBUTE_TIME);
    } else {
      timeAttribute = "";
    }

    if (intent.hasExtra(ATTRIBUTE_LOCATION)) {
      locationAttribute = intent.getStringExtra(ATTRIBUTE_LOCATION);
    } else {
      locationAttribute = "";
    }

    this.appName = appName;
    this.eventName = eventName;
  }

  /**
   * Returns the name of the application for this event
   * 
   * @return name of the application for this event
   */
  public String getAppName() {
    return appName;
  }

  /**
   * Returns the name of the event
   * 
   * @return name of the event
   */
  public String getEventName() {
    return eventName;
  }

  /**
   * Looks up attributes associated with this event.
   * 
   * @param attributeName
   *          the name of the attribute associated with this event
   * @return the data associated with the attribute
   * @throws IllegalArgumentException
   *           if the attribute name is not valid for this event
   */
  public String getAttribute(String attributeName) throws IllegalArgumentException {
    if (attributeName.equals(ATTRIBUTE_TIME)) {
      return timeAttribute;
    } else if (attributeName.equals(ATTRIBUTE_LOCATION)) {
      return locationAttribute;
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * @return the parameters of the event as they are passed through the android intent.
   */
  public String getParameters() {
    if (intent.getExtras() != null) {
      return intent.getExtras().toString();
    } else {
      return "";
    }
  }
}