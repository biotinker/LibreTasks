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

import java.util.ArrayList;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import edu.nyu.cs.omnidroid.app.controller.Action;
import edu.nyu.cs.omnidroid.app.controller.Event;
import edu.nyu.cs.omnidroid.app.controller.Rule;
import edu.nyu.cs.omnidroid.app.controller.util.OmnidroidException;
import edu.nyu.cs.omnidroid.app.model.CoreActionsDbHelper;
import edu.nyu.cs.omnidroid.app.model.db.DbHelper;

/**
 * Unit tests for {@link Rule} class.
 */

public class RuleTest extends AndroidTestCase {
  private Rule rule;
  Event event;
  
  private SQLiteDatabase database;
  private DbHelper omnidroidDbHelper;
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    
    Intent intent = TestData.getIntent("123-456-7890", "Some Other Text");
    event = new MockSMSReceivedEvent(intent);
   
    omnidroidDbHelper = new DbHelper(this.getContext());
    database = omnidroidDbHelper.getWritableDatabase();
    
    omnidroidDbHelper.backup();
    
    RuleTestData.prePopulateDatabase(database);
    rule = RuleTestData.getRule(RuleTestData.RULE_SAY_HELLO3);
  }

  @Override
  protected void tearDown() throws Exception {
    // Try to restore the database
    //database.close();
    
    if (omnidroidDbHelper.isBackedUp()) {
      omnidroidDbHelper.restore();
    }
    super.tearDown();
    omnidroidDbHelper.close();
  }
  
  /** Tests that two rules containing the same data are equal */
  public void testEqual() {
    assertEquals(rule, RuleTestData.getRule(RuleTestData.RULE_SAY_HELLO3));
    assertEquals(rule.hashCode(), RuleTestData.getRule(RuleTestData.RULE_SAY_HELLO3).hashCode());
  }

  /** Tests that two rules containing different data are not equal */
  public void testNotEqual() {
    assertFalse(rule.equals(RuleTestData.getRule(RuleTestData.RULE_SAY_HELLO2)));
  }

  /** Tests an event that passes a Rule's filters */
  public void testRulesPass() {
    assertTrue(rule.passesFilters(event));
  }
  
  /** Tests an event does not pass a Rule's filters */
  public void testRulesFail() {
    Intent intent = TestData.getIntent("123-456-7890", "Non-matching text");
    Event anotherEvent = new MockSMSReceivedEvent(intent);
    assertFalse(rule.passesFilters(anotherEvent));
  }
  
  public void testGetActions() throws OmnidroidException {
    // Create expected actions
    ArrayList<Action> expectedActions = new ArrayList<Action>();
    expectedActions.add(RuleTestData.getAction(RuleTestData.ACTION_SAYHELLO, event));
    expectedActions.add(RuleTestData.getAction(RuleTestData.ACTION_DND2, event));
    
    // Get rule's actions from database
    CoreActionsDbHelper coreActionsDbHelper = new CoreActionsDbHelper(this.getContext());
    ArrayList<Action> actualActions = rule.getActions(coreActionsDbHelper, event);
    
    // Compare actions with available fields
    assertEquals(expectedActions.size(), actualActions.size());
    for (int i = 0; i < expectedActions.size(); i++) {
      Action expectedAction = expectedActions.get(i);
      Action actualAction = actualActions.get(i);
      assertEquals(expectedAction.getActionName(), actualAction.getActionName());
      assertEquals(expectedAction.getClass(), actualAction.getClass());
      assertEquals(expectedAction.getExecutionMethod(), actualAction.getExecutionMethod());
    }
  }
}