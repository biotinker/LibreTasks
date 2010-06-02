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
package edu.nyu.cs.omnidroid.app.controller;

import edu.nyu.cs.omnidroid.app.controller.events.SMSReceivedEvent;
import junit.framework.TestCase;
import android.content.Intent;

/**
 * Unit tests for {@link SMSReceivedEvent} class.
 */
public class SMSReceivedEventTest extends TestCase {
  private SMSReceivedEvent sms;
  Intent intent;

  @Override
  public void setUp() {
    sms = TestData.getSMSEvent();
  }

  /** Tests the lookup of the sender's phone number */
  public void testGetData_Phone() {
    String field = SMSReceivedEvent.ATTRIB_PHONE_NO;
    assertEquals(TestData.TEST_PHONE_NO, sms.getAttribute(field));
  }

  /** Tests the lookup of the message text */
  public void testGetData_Text() {
    String field = SMSReceivedEvent.ATTRIB_MESSAGE_TEXT;
    assertEquals(TestData.TEST_MESSAGE_TEXT, sms.getAttribute(field));
  }

  /** Tests an invalid lookup */
  public void testGetInvalidField() {
    String field = "Bad fieldname";
    try {
      sms.getAttribute(field);
      fail("Should raise an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
    }
  }
}
