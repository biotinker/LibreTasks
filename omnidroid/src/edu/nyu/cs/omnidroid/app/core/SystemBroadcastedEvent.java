/*******************************************************************************
 * Copyright 2009 OmniDroid - http://code.google.com/p/omnidroid 
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
package edu.nyu.cs.omnidroid.app.core;

import android.content.Intent;

/**
 * This class only represent those system broadcasted events without parameters
 * @see SystemEvent
 */
public class SystemBroadcastedEvent extends Event {
  
  public SystemBroadcastedEvent(Intent intent, SystemEvent systemEvent) {
    super(systemEvent.APPLICATION_NAME, systemEvent.EVENT_NAME, intent);
  }

  /**
   * Looks up attributes associated with this event. In this class, there should always no attribute
   * support.
   * 
   * @param attributeName
   *          the name of the attribute associated with this event
   * @return the data associated with the attribute
   * @throws IllegalArgumentException
   *           if the attribute is not of a type supported by this event
   */
  @Override
  public String getAttribute(String attributeName) throws IllegalArgumentException {
    throw (new IllegalArgumentException());
  }

}
