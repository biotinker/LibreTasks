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
package libretasks.app.controller.events;

import android.content.Intent;
import android.util.Log;

/**
 * This class encapsulates an phone call ended event. It wraps the intent that triggered this event
 * and provides access to any attribute data associated with it. A phone call is considered ended
 * when the user hangs up the phone. This means that this event is not triggered when an unanswered
 * incoming call ended. There may be a need to split this class into 2 different classes in the
 * future to differentiate end of an incoming or outgoing calls.
 * 
 * TODO: (renctan) Support the phone number attribute and refactor it to the base class if possible.
 * As of API level 4, {@code incomingNumber} passed to {@code onCallStateChanged()} is empty. We
 * could work around this by remembering the number during CALL_STATE_RINGING, but that only covers
 * the case for incoming calls.
 */
public class CallEndedEvent extends PhoneCallEvent {
  /** Event name (to match record in database) */
  //TODO: (renctan) Move the string to be displayed on the UI to string.xml 
  public static final String EVENT_NAME = "Phone Call Ended";
  public static final String ACTION_NAME = "PHONE_CALL_ENDED";

  private static final String LOG_TAG = CallEndedEvent.class.getSimpleName();

  /**
   * Constructs a new CallEndedEvent object that holds an PHONE_CALL_ENDED event fired intent. This
   * intent holds the data needed to check the event against user defined rules.
   * 
   * @param intent
   *          the intent received when the PHONE_CALL_ENDED event was fired
   */
  public CallEndedEvent(Intent intent) {
    super(EVENT_NAME, intent);
    Log.d(LOG_TAG, EVENT_NAME);
  }
}
