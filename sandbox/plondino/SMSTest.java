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
package edu.nyu.cs.omnidroid.core;

import android.content.Intent;
import junit.framework.TestCase;

/**
 * Unit tests for {@link SMS} class.
 */
public class SMSTest extends TestCase {
  private SMS sms;
  Intent intent;

  @Override
  public void setUp() {
    intent = new Intent();
    sms = new SMS(intent);
  }
    
  /** Test the lookup of the sender's phone number */
  public void testGetData_Phone() {
    String field = SMS.ATTRIB_PHONE_NO;
    String data = "908-220-2280";
    assertEquals("908-220-2280", sms.getData(field));
  }
  
  /** Test the lookup of the message text */
  public void testGetData_Text() {
    String field = SMS.ATTRIB_MESSAGE_TEXT;
    String data = "The moon in June is a big, big balloon.";
    assertEquals(data, sms.getData(field));
  }
}
