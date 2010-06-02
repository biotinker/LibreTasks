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

import java.util.ArrayList;
import java.util.HashSet;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import edu.nyu.cs.omnidroid.app.controller.Action;
import edu.nyu.cs.omnidroid.app.controller.Event;
import edu.nyu.cs.omnidroid.app.controller.Rule;
import edu.nyu.cs.omnidroid.app.controller.RuleProcessor;
import edu.nyu.cs.omnidroid.app.controller.util.OmnidroidException;
import edu.nyu.cs.omnidroid.app.model.CoreActionsDbHelper;
import edu.nyu.cs.omnidroid.app.model.CoreRuleDbHelper;
import edu.nyu.cs.omnidroid.app.model.CursorHelper;
import edu.nyu.cs.omnidroid.app.model.db.DbHelper;
import edu.nyu.cs.omnidroid.app.model.db.RuleDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.RuleFilterDbAdapter;

/**
 * Unit tests for {@link RuleProcessor} class.
 */
public class RuleProcessorTest extends AndroidTestCase {
  Rule rule;
  Event event;
  private SQLiteDatabase database;
  private DbHelper omnidroidDbHelper;
  CoreRuleDbHelper coreRuleDbHelper;
  CoreActionsDbHelper coreActionsDbHelper;

  public void setUp() {
    Intent intent = TestData.getIntent("123-456-7890", "Some Other Text");
    event = new MockSMSReceivedEvent(intent);

    omnidroidDbHelper = new DbHelper(this.getContext());
    database = omnidroidDbHelper.getWritableDatabase();
    coreRuleDbHelper = new CoreRuleDbHelper(getContext());
    coreActionsDbHelper = new CoreActionsDbHelper(getContext());

    omnidroidDbHelper.backup();
    RuleTestData.prePopulateDatabase(database);
  }

  @Override
  protected void tearDown() throws Exception {
    // Try to restore the database
    // database.close();

    if (omnidroidDbHelper.isBackedUp()) {
      omnidroidDbHelper.restore();
    }
    super.tearDown();
    omnidroidDbHelper.close();
  }

  /**
   * Tests that the rule processor correctly retrieves an action if the event passes all rule
   * filters
   * 
   * @throws OmnidroidException
   */
  public void testRuleProcessor_pass() throws OmnidroidException {
    // Create expected actions
    ArrayList<Action> expectedActions = new ArrayList<Action>();
    expectedActions.add(RuleTestData.getAction(RuleTestData.ACTION_DND, event));
    expectedActions.add(RuleTestData.getAction(RuleTestData.ACTION_SAYHELLO, event));
    expectedActions.add(RuleTestData.getAction(RuleTestData.ACTION_SAYHELLO, event));
    expectedActions.add(RuleTestData.getAction(RuleTestData.ACTION_DND2, event));

    ArrayList<Action> actualActions = RuleProcessor.getActions(event, coreRuleDbHelper,
        coreActionsDbHelper);
    assertEquals(expectedActions.size(), actualActions.size());
  }

  /**
   * Tests that the rule processor does not retrieve any actions if the event does not match a rule
   */
  public void testRuleProcessor_noPass() {
    // Make a new event that doesn't match any rules
    Intent intent = TestData.getIntent("000-000-0000", "Some message text");
    Event anotherEvent = new MockSMSReceivedEvent(intent);

    // Delete any rules that have no filters, since they will always pass
    RuleDbAdapter ruleDbAdapter = new RuleDbAdapter(database);
    RuleFilterDbAdapter ruleFilterDbAdapter = new RuleFilterDbAdapter(database);

    Cursor cursor = ruleDbAdapter.fetchAll();

    // Get a set of all rule IDs
    HashSet<Long> ruleIDs = new HashSet<Long>();
    while (cursor.moveToNext()) {
      ruleIDs.add(CursorHelper.getLongFromCursor(cursor, RuleDbAdapter.KEY_RULEID));
    }

    // Get a set of all rules with filters
    HashSet<Long> rulesWithFilters = new HashSet<Long>();
    // Look through the RuleFilters table, and delete any rules that don't have any filters
    cursor = ruleFilterDbAdapter.fetchAll();
    while (cursor.moveToNext()) {
      rulesWithFilters.add(CursorHelper.getLongFromCursor(cursor, RuleFilterDbAdapter.KEY_RULEID));
    }

    // Get the set of rule IDs with no filters and delete them from the rules table
    ruleIDs.removeAll(rulesWithFilters);
    for (Long ruleID : ruleIDs) {
      ruleDbAdapter.delete(ruleID);
    }
    assertEquals(0, RuleProcessor.getActions(anotherEvent, coreRuleDbHelper, coreActionsDbHelper)
        .size());
  }
}
