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

import libretasks.app.controller.Event;
import android.content.Intent;

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
   * is to make it compatible with existing databases. This constant is already deprecated, use the
   * the global attribute {@link Event#ATTRIBUTE_TIME} instead.
   */
  @Deprecated
  public static final String ATTRIBUTE_TIMESTAMP = "Phone Ring Time";

  /** Cache any values that are requested because it is likely they will be asked for again */
  protected String phoneNumber;

  /**
   * Constructs a new PhoneRinging object that holds a PhoneRinging event fired intent. This intent
   * holds the data needed to check the event against user defined rules.
   * 
   * @param intent
   *          the intent received when the PhoneRinging event was fired
   */
  public PhoneCallEvent(String eventName, Intent intent) {
    super(APPLICATION_NAME, eventName, intent);
  }
}
