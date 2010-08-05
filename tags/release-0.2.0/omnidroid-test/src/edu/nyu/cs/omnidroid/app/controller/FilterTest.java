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

import edu.nyu.cs.omnidroid.app.controller.Event;
import edu.nyu.cs.omnidroid.app.controller.Filter;
import junit.framework.TestCase;
import android.content.Intent;

/**
 * Unit tests for {@link Filter} class.
 */
public class FilterTest extends TestCase {
  private Filter filter;
  private Event smsReceivedEvent;

  @Override
  public void setUp() {
    filter = RuleTestData.getFilter(RuleTestData.FILTER_SMSPHONE1);
    Intent intent = TestData.getIntent("555-555-5555", "Some Text");
    smsReceivedEvent = new MockSMSReceivedEvent(intent);
  }

  /**
   * Tests that two Filter objects with the same data are equal
   */
  public void testEqual() {
    Filter sameFilter = RuleTestData.getFilter(RuleTestData.FILTER_SMSPHONE1);
    assertEquals(filter, sameFilter);
    assertEquals(filter.hashCode(), sameFilter.hashCode());
  }

  /**
   * Tests that two Filter objects with different data are not equal
   */
  public void testNotEqual() {
    Filter differentFilter = RuleTestData.getFilter(RuleTestData.FILTER_SMSPHONE2);
    assertFalse(filter.equals(differentFilter));
  }
  
  /**
   * Tests the filter match returns true on a matching event
   */
  public void testMatchTrue() {
    assertTrue(filter.match(smsReceivedEvent));
  }
  
  /**
   * Tests the filter match returns false on a non-matching event
   */
  public void testFilterMatchFalse() {
    Intent intent = TestData.getIntent("123-456-7890", "Some Text");
    smsReceivedEvent = new MockSMSReceivedEvent(intent);
    assertFalse(filter.match(smsReceivedEvent));
  }
}