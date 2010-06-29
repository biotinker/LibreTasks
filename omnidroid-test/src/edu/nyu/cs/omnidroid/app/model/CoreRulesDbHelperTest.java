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
package edu.nyu.cs.omnidroid.app.model;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import edu.nyu.cs.omnidroid.app.controller.Rule;
import edu.nyu.cs.omnidroid.app.controller.RuleTestData;
import edu.nyu.cs.omnidroid.app.controller.events.SMSReceivedEvent;
import edu.nyu.cs.omnidroid.app.model.CoreRulesDbHelper;
import edu.nyu.cs.omnidroid.app.model.db.DbHelper;

/**
 * Unit tests for {@link CoreRuleDbHelper} class.
 */
public class CoreRulesDbHelperTest extends AndroidTestCase {

  private DbHelper omnidroidDbHelper;
  private SQLiteDatabase database;
  private CoreRulesDbHelper coreDbHelper;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    omnidroidDbHelper = new DbHelper(this.getContext());
    database = omnidroidDbHelper.getWritableDatabase();

    coreDbHelper = new CoreRulesDbHelper(getContext());

    omnidroidDbHelper.backup();
    RuleTestData.prePopulateDatabase(database);
  }

  @Override
  protected void tearDown() throws Exception {
    // Try to restore the database
    if (omnidroidDbHelper.isBackedUp()) {
      omnidroidDbHelper.restore();
    }
    super.tearDown();
    omnidroidDbHelper.close();
  }

  /** Test that given an event, the correct list of rules is retrieved from the database */
  public void testGetRulesMatchingEvent() {
    String appName = SMSReceivedEvent.APPLICATION_NAME;
    String eventName = SMSReceivedEvent.EVENT_NAME;
    ArrayList<Rule> expectedRules = RuleTestData.getRules();
    ArrayList<Rule> actualRules = coreDbHelper.getRulesMatchingEvent(appName, eventName);
    assertEquals(actualRules, expectedRules);
  }
}