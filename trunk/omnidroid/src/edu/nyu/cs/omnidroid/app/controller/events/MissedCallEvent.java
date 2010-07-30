/*******************************************************************************
 * Copyright 2010 Omnidroid - http://code.google.com/p/omnidroid 
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

import android.content.Intent;

/**
 * This class encapsulates an MissedCall event. It wraps the intent that triggered this event
 * and provides access to any attribute data associated with it.
 */
public class MissedCallEvent extends PhoneCallEvent {
  
  /** Event name (to match record in database) */
  public static final String EVENT_NAME = "Missed Call";
  public static final String ACTION_NAME = "MISSED_CALL";
  
  public static final String ATTRIBUTE_PHONE_NUMBER = "Phone Number";

  
  public MissedCallEvent(Intent intent) {
    super(EVENT_NAME, intent);
  }
  
  @Override
  public String getAttribute(String attributeName) throws IllegalArgumentException {
    if (attributeName.equals(PhoneRingingEvent.ATTRIBUTE_PHONE_NUMBER)) {
      if (phoneNumber == null) {
        phoneNumber = intent.getStringExtra(PhoneRingingEvent.ATTRIBUTE_PHONE_NUMBER);
      }
      return phoneNumber;
    } else {
      return super.getAttribute(attributeName);
    }
  }
}
