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
package edu.nyu.cs.omnidroid.app.controller;

import edu.nyu.cs.omnidroid.app.controller.events.SMSReceivedEvent;
import android.content.Intent;

/**
 * Mock version of {@link SMSReceivedEvent}, which will reliably return the phone number
 * and message text used to create the intent passed to the constructor.
 */
public class MockSMSReceivedEvent extends SMSReceivedEvent {

  public MockSMSReceivedEvent(Intent intent) {
    super(intent);
    phoneNumber = intent.getExtras().getString(SMSReceivedEvent.ATTRIB_PHONE_NO);
    messageText = intent.getExtras().getString(SMSReceivedEvent.ATTRIB_MESSAGE_TEXT);
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
  public String getAttribute(String attributeName) {
    if (attributeName.equals(ATTRIB_PHONE_NO)) {
      return phoneNumber;
    } else if (attributeName.equals(ATTRIB_MESSAGE_TEXT)) {
      return messageText;
    } else {
      throw (new IllegalArgumentException());
    }
    // TODO(londinop): Add exception for invalid data field name
  }
}
