/*  
 * Copyright (c) 2016  LibreTasks - https://github.com/biotinker/LibreTasks  
 *  
 *  This file is free software: you may copy, redistribute and/or modify it  
 *  under the terms of the GNU General Public License as published by the  
 *  Free Software Foundation, either version 3 of the License, or (at your  
 *  option) any later version.  
 *  
 *  This file is distributed in the hope that it will be useful, but  
 *  WITHOUT ANY WARRANTY; without even the implied warranty of  
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  
 *  General Public License for more details.  
 *  
 *  You should have received a copy of the GNU General Public License  
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 *  
 * This file incorporates work covered by the following copyright and  
 * permission notice:  
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

import libretasks.app.controller.events.LocationChangedEvent;
import libretasks.app.controller.events.InternetAvailableEvent;
import libretasks.app.controller.events.MissedCallEvent;
import libretasks.app.controller.events.PhoneRingingEvent;
import libretasks.app.controller.events.CallEndedEvent;
import libretasks.app.controller.events.SMSReceivedEvent;
import libretasks.app.controller.events.ServiceAvailableEvent;
import libretasks.app.controller.events.SystemBroadcastedEvent;
import libretasks.app.controller.events.SystemEvent;
import libretasks.app.controller.events.TimeTickEvent;
import android.content.Intent;
import android.util.Log;

/**
 * This class parses the received {@link android.content.Intent}'s action field to see if it is an
 * Omnidroid supported event. Then it creates an {@link Event} object which is able to parse the
 * intent for any associated data attributes.
 */
public class IntentParser {
  // TODO(londinop): put intent action fields in a database table rather than hard code
  public static final String SMS_INTENT_ACTION = "android.provider.Telephony.SMS_RECEIVED";
  public static final String GMAIL_INTENT_ACTION = "android.intent.action.PROVIDER_CHANGED";

  /**
   * This is a static utility class which cannot be instantiated.
   */
  private IntentParser() {
  }

  /**
   * Given an intent with a supported action type, create and return an Event of the appropriate
   * type. If the action is not supported, null is returned.
   * 
   * @param intent
   *          an intent received by the system describing the event that took place
   * @return an Omnidroid Event type that contains the methods to get at the event's data attributes
   */
  public static Event getEvent(Intent intent) {
    Log.d("IntentParser:", "get Intent with action: " + intent.getAction());
    Event event = null;
    if (intent.getAction().equals(SMS_INTENT_ACTION)) {
      // Handle SMS event
      event = new SMSReceivedEvent(intent);
    } else if (intent.getAction().equals(LocationChangedEvent.ACTION_NAME)) {
      event = new LocationChangedEvent(intent);
    } else if (intent.getAction().equals(PhoneRingingEvent.ACTION_NAME)) {
      event = new PhoneRingingEvent(intent);
    } else if (intent.getAction().equals(CallEndedEvent.ACTION_NAME)) {
      event = new CallEndedEvent(intent);
    } else if (intent.getAction().equals(TimeTickEvent.ACTION_NAME)) {
      event = new TimeTickEvent(intent);
    } else if (intent.getAction().equals(ServiceAvailableEvent.ACTION_NAME)) {
      event = new ServiceAvailableEvent(intent);
    } else if (intent.getAction().equals(InternetAvailableEvent.ACTION_NAME)) {
      event = new InternetAvailableEvent(intent);
    } else if (intent.getAction().equals(MissedCallEvent.ACTION_NAME)) {
      event = new MissedCallEvent(intent);
    } else {
      for (SystemEvent e : SystemEvent.values()) {
        if (intent.getAction().equals(e.ACTION_NAME)) {
          Log.d("IntentParser:", e.ACTION_NAME);
          event = new SystemBroadcastedEvent(intent, e);
        }
      }
    }
    return event;
  }
}
