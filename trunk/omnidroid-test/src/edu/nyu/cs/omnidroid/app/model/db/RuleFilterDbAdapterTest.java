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
package edu.nyu.cs.omnidroid.app.model.db;

import edu.nyu.cs.omnidroid.app.model.db.DbHelper;
import edu.nyu.cs.omnidroid.app.model.db.RuleFilterDbAdapter;
import android.database.Cursor;
import android.test.AndroidTestCase;

/**
 * Android Unit Test for {@link RuleFilterDbAdapter} class.
 */
public class RuleFilterDbAdapterTest extends AndroidTestCase {

  private RuleFilterDbAdapter dbAdapter;
  private DbHelper omnidroidDbHelper;

  // Data for testing
  private Long[] ruleIDs = { Long.valueOf(11), Long.valueOf(22), Long.valueOf(33) };
  private Long[] eventAttributeIDs = { Long.valueOf(12), Long.valueOf(23), Long.valueOf(34) };
  private Long[] externalAttributeIDs = { Long.valueOf(13), Long.valueOf(24), Long.valueOf(35) };
  private Long[] dataFilterIDs = { Long.valueOf(14), Long.valueOf(25), Long.valueOf(36) };
  private Long[] parentRuleFilterIDs = { Long.valueOf(15), Long.valueOf(26), Long.valueOf(37) };
  private String[] ruleFilterDatas = { "Data1", "Data2", "Data3" };

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    omnidroidDbHelper = new DbHelper(this.getContext());
    dbAdapter = new RuleFilterDbAdapter(omnidroidDbHelper.getWritableDatabase());
    omnidroidDbHelper.backup();
    dbAdapter.deleteAll();
  }

  @Override
  protected void tearDown() throws Exception {
    dbAdapter.deleteAll();
    
    // Try to restore the database
    if(omnidroidDbHelper.isBackedUp()) {
      omnidroidDbHelper.restore();
    }
    
    omnidroidDbHelper.close();
    super.tearDown();
  }

  public void testPrecondition() {
    Cursor cursor = dbAdapter.fetchAll();
    assertEquals(cursor.getCount(), 0);
  }

  public void testInsert() {
    long id1 = dbAdapter.insert(ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0],
        dataFilterIDs[0], parentRuleFilterIDs[0], ruleFilterDatas[0]);
    assertTrue(id1 != -1);

    long id2 = dbAdapter.insert(ruleIDs[1], eventAttributeIDs[1], externalAttributeIDs[1],
        dataFilterIDs[1], parentRuleFilterIDs[1], ruleFilterDatas[1]);
    assertTrue(id1 != id2);
  }

  public void testInsert_illegalArgumentException() {
    Exception expected = null;
    try {
      dbAdapter.insert(null, eventAttributeIDs[0], externalAttributeIDs[0], dataFilterIDs[0],
          parentRuleFilterIDs[0], ruleFilterDatas[0]);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);

    expected = null;
    try {
      dbAdapter.insert(ruleIDs[0], null, externalAttributeIDs[0], dataFilterIDs[0],
          parentRuleFilterIDs[0], ruleFilterDatas[0]);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);

    expected = null;
    try {
      dbAdapter.insert(ruleIDs[0], eventAttributeIDs[0], null, dataFilterIDs[0],
          parentRuleFilterIDs[0], ruleFilterDatas[0]);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);

    expected = null;
    try {
      dbAdapter.insert(ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0], null,
          parentRuleFilterIDs[0], ruleFilterDatas[0]);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);

    expected = null;
    try {
      dbAdapter.insert(ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0], dataFilterIDs[0],
          null, ruleFilterDatas[0]);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);

    expected = null;
    try {
      dbAdapter.insert(ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0], dataFilterIDs[0],
          parentRuleFilterIDs[0], null);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);
  }

  public void testFetch() {
    long id = dbAdapter.insert(ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0],
        dataFilterIDs[0], parentRuleFilterIDs[0], ruleFilterDatas[0]);
    Cursor cursor = dbAdapter.fetch(id);
    // Validate the inserted record.
    assertCursorEquals(cursor, ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0],
        dataFilterIDs[0], parentRuleFilterIDs[0], ruleFilterDatas[0]);
    // Validate the record count
    assertEquals(dbAdapter.fetchAll().getCount(), 1);
  }

  public void testFetch_notExisting() {
    long id = dbAdapter.insert(ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0],
        dataFilterIDs[0], parentRuleFilterIDs[0], ruleFilterDatas[0]);
    Cursor cursor = dbAdapter.fetch(id + 1);
    assertEquals(cursor.getCount(), 0);
  }

  public void testFetch_illegalArgumentException() {
    Exception expected = null;
    try {
      dbAdapter.fetch(null);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);
  }

  public void testDelete() {
    long id = dbAdapter.insert(ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0],
        dataFilterIDs[0], parentRuleFilterIDs[0], ruleFilterDatas[0]);
    assertTrue(dbAdapter.delete(id));
    // Validate it cannot be fetched.
    Cursor cursor = dbAdapter.fetch(id);
    assertEquals(cursor.getCount(), 0);
  }

  public void testDelete_notExisting() {
    long id = dbAdapter.insert(ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0],
        dataFilterIDs[0], parentRuleFilterIDs[0], ruleFilterDatas[0]);
    assertTrue(dbAdapter.delete(id));
    assertFalse(dbAdapter.delete(id));
  }

  public void testDelete_illegalArgumentException() {
    Exception expected = null;
    try {
      dbAdapter.delete(null);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);
  }

  public void testUpdate() {
    long id = dbAdapter.insert(ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0],
        dataFilterIDs[0], parentRuleFilterIDs[0], ruleFilterDatas[0]);
    assertTrue(dbAdapter.update(id, ruleIDs[1], eventAttributeIDs[1], externalAttributeIDs[1],
        dataFilterIDs[1], parentRuleFilterIDs[1], ruleFilterDatas[1]));
    // Validate the updated record.
    Cursor cursor = dbAdapter.fetch(id);
    assertCursorEquals(cursor, ruleIDs[1], eventAttributeIDs[1], externalAttributeIDs[1],
        dataFilterIDs[1], parentRuleFilterIDs[1], ruleFilterDatas[1]);
  }

  public void testUpdate_notExisting() {
    long id = dbAdapter.insert(ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0],
        dataFilterIDs[0], parentRuleFilterIDs[0], ruleFilterDatas[0]);
    dbAdapter.delete(id);
    assertFalse(dbAdapter.update(id, ruleIDs[1], eventAttributeIDs[1], externalAttributeIDs[1],
        dataFilterIDs[1], parentRuleFilterIDs[1], ruleFilterDatas[1]));
  }

  public void testUpdate_withNullValues() {
    long id = dbAdapter.insert(ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0],
        dataFilterIDs[0], parentRuleFilterIDs[0], ruleFilterDatas[0]);
    assertFalse(dbAdapter.update(id, null, null, null, null, null, null));
    // Validate the updated record.
    Cursor cursor = dbAdapter.fetch(id);
    assertCursorEquals(cursor, ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0],
        dataFilterIDs[0], parentRuleFilterIDs[0], ruleFilterDatas[0]);
  }

  public void testUpdate_illegalArgumentException() {
    Exception expected = null;
    try {
      dbAdapter.update(null, ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0],
          dataFilterIDs[0], parentRuleFilterIDs[0], ruleFilterDatas[0]);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);
  }

  public void testFetchAll() {
    dbAdapter.insert(ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0], dataFilterIDs[0],
        parentRuleFilterIDs[0], ruleFilterDatas[0]);
    dbAdapter.insert(ruleIDs[1], eventAttributeIDs[1], externalAttributeIDs[1], dataFilterIDs[1],
        parentRuleFilterIDs[1], ruleFilterDatas[1]);
    dbAdapter.insert(ruleIDs[2], eventAttributeIDs[2], externalAttributeIDs[2], dataFilterIDs[2],
        parentRuleFilterIDs[2], ruleFilterDatas[2]);

    Cursor cursor = dbAdapter.fetchAll();
    assertEquals(cursor.getCount(), 3);
  }

  public void testFetchAll_withParameters() {
    dbAdapter.insert(ruleIDs[0], eventAttributeIDs[0], externalAttributeIDs[0], dataFilterIDs[0],
        parentRuleFilterIDs[0], ruleFilterDatas[0]);

    dbAdapter.insert(ruleIDs[0], eventAttributeIDs[1], externalAttributeIDs[1], dataFilterIDs[1],
        parentRuleFilterIDs[1], ruleFilterDatas[1]);

    dbAdapter.insert(ruleIDs[2], eventAttributeIDs[2], externalAttributeIDs[2], dataFilterIDs[2],
        parentRuleFilterIDs[2], ruleFilterDatas[2]);

    assertTrue(dbAdapter.fetchAll(null, null, null, null, null, null).getCount() == 3);
    assertTrue(dbAdapter.fetchAll(ruleIDs[0], null, null, null, null, null).getCount() == 2);
    assertTrue(dbAdapter.fetchAll(null, eventAttributeIDs[2], null, null, null, null)
        .getCount() == 1);
    assertTrue(dbAdapter.fetchAll(null, null, externalAttributeIDs[2], null, null,
        ruleFilterDatas[2]).getCount() == 1);
    assertTrue(dbAdapter.fetchAll(null, null, null, dataFilterIDs[1], parentRuleFilterIDs[1], null)
        .getCount() == 1);
    assertTrue(dbAdapter.fetchAll(ruleIDs[0], null, null, null, null, ruleFilterDatas[2])
        .getCount() == 0);
  }

  /**
   * Helper method to assert a cursor pointing to a action record that matches the parameters
   */
  private void assertCursorEquals(Cursor cursor, Long ruleID, Long eventAttributeID,
      Long externalAttributeID, Long dataFilterID, Long parentRuleFilterID, String ruleFilterData) {
    assertEquals(cursor.getInt(cursor.getColumnIndex(RuleFilterDbAdapter.KEY_RULEID)), ruleID
        .intValue());
    assertEquals(cursor.getInt(cursor.getColumnIndex(RuleFilterDbAdapter.KEY_EVENTATTRIBUTEID)),
        eventAttributeID.intValue());
    assertEquals(cursor.getInt(cursor.getColumnIndex(RuleFilterDbAdapter.KEY_EXTERNALATTRIBUTEID)),
        externalAttributeID.intValue());
    assertEquals(cursor.getInt(cursor.getColumnIndex(RuleFilterDbAdapter.KEY_DATAFILTERID)),
        dataFilterID.intValue());
    assertEquals(cursor.getInt(cursor.getColumnIndex(RuleFilterDbAdapter.KEY_PARENTRULEFILTERID)),
        parentRuleFilterID.intValue());
    assertEquals(cursor.getString(cursor.getColumnIndex(RuleFilterDbAdapter.KEY_RULEFILTERDATA)),
        ruleFilterData);
  }
}