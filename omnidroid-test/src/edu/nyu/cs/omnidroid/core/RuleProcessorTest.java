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
 * Unit tests for {@link RuleProcessor} class.
 */
public class RuleProcessorTest extends TestCase {
  Rule rule;
  Event event;

  public void setUp() {
    event = TestData.getSMSEvent();

  }

  /**
   * Tests that the rule processor correctly retrieves an action if the event passes all rule
   * filters
   */
  public void testRuleProcessor_pass() {
    // Parameters provided to the CallPhoneAction
    HashMap<String, String> phoneCallParameters = new HashMap<String, String>();
    phoneCallParameters.put(CallPhoneAction.PARAM_PHONE_NO, "5556");
    ArrayList<Action> actions = new ArrayList<Action>();
    Action action = new CallPhoneAction(phoneCallParameters);
    actions.add(action);
    assertEquals(actions.size(), RuleProcessor.getActions(event).size());
  }

  /**
   * Tests that the rule processor does not retrieve an action if the event does not pass all rule
   * filters
   */
  public void testRuleProcessor_noPass() {
    event = TestData.getAnotherSMSEvent();
    assertEquals(0, RuleProcessor.getActions(event).size());
  }
}
