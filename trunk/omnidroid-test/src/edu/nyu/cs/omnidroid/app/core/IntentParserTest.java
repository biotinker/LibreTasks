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

import edu.nyu.cs.omnidroid.app.core.Event;
import edu.nyu.cs.omnidroid.app.core.IntentParser;
import junit.framework.TestCase;
import android.content.Intent;

/**
 * Unit tests for {@link IntentParser} class.
 */
public class IntentParserTest extends TestCase {
  Intent intent;
  Event event;

  public void setUp() {
    intent = TestData.getIntent(TestData.TEST_PHONE_NO, TestData.TEST_MESSAGE_TEXT);
    intent.setAction(IntentParser.SMS_INTENT_ACTION);
    event = new MockSMSReceivedEvent(intent);
  }

  /** Tests that the name of a created SMS event is correct */
  public void testGetEvent() {
    assertEquals(IntentParser.getEvent(intent).getEventName(), event.getEventName());
  }
}