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

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;
import edu.nyu.cs.omnidroid.tests.TestData;

/**
 * Unit tests for {@link Rule} class.
 */
public class RuleTest extends TestCase {
  Rule rule;
  Event event;

  public void setUp() {
    rule = TestData.getRule();
    event = TestData.getSMSEvent();
  }

  public void testCheckFilters_True() {
    assertTrue(rule.checkFilters(event));
  }

  public void testCheckFilters_False() {
    rule = TestData.getAnotherRule();
    assertFalse(rule.checkFilters(event));
  }

  public void testGetActions() {
    ArrayList<Action> actions = new ArrayList<Action>();
    // Parameters provided to the CallPhoneAction
    HashMap<String, String> phoneCallParameters = new HashMap<String, String>();
    phoneCallParameters.put(CallPhoneAction.PARAM_PHONE_NO, "5556");
    Action action = new CallPhoneAction(phoneCallParameters);
    actions.add(action);
    assertEquals(actions.size(), rule.getActions(event).size());
  }
}