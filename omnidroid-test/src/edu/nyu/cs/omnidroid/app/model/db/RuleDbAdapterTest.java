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
package edu.nyu.cs.omnidroid.app.model.db;

import edu.nyu.cs.omnidroid.app.model.db.DbHelper;
import edu.nyu.cs.omnidroid.app.model.db.RuleDbAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.test.AndroidTestCase;

/**
 * Android Unit Test for {@link RuleDbAdapter} class.
 */
public class RuleDbAdapterTest extends AndroidTestCase {

  private RuleDbAdapter dbAdapter;
  private DbHelper omnidroidDbHelper;

  // Data for testing
  private Long[] eventIDs = { Long.valueOf(1), Long.valueOf(2), Long.valueOf(3) };
  private String[] ruleNames = { "rule1", "rule2", "rule3" };
  private String[] ruleDescs = { "desc1", "desc2", "desc3" };

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    omnidroidDbHelper = new DbHelper(this.getContext());
    dbAdapter = new RuleDbAdapter(omnidroidDbHelper.getWritableDatabase());
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
    long id1 = dbAdapter.insert(eventIDs[0], ruleNames[0], ruleDescs[0], true);
    assertTrue(id1 != -1);

    long id2 = dbAdapter.insert(eventIDs[1], ruleNames[1], ruleDescs[1], true);
    assertTrue(id1 != id2);
  }

  public void testInsert_illegalArgumentException() {
    Exception expected = null;
    try {
      dbAdapter.insert(null, ruleNames[0], ruleDescs[0], true);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);

    expected = null;
    try {
      dbAdapter.insert(eventIDs[0], null, ruleDescs[0], true);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);

    try {
      dbAdapter.insert(eventIDs[0], ruleNames[0], null, true);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);

    try {
      dbAdapter.insert(eventIDs[0], ruleNames[0], ruleDescs[0], null);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);
  }

  public void testFetch() {
    long id = dbAdapter.insert(eventIDs[0], ruleNames[0], ruleDescs[0], true);
    Cursor cursor = dbAdapter.fetch(id);
    // Validate the inserted record.
    assertCursorEquals(cursor, eventIDs[0], ruleNames[0], ruleDescs[0], true);
    // Validate the record count
    assertEquals(dbAdapter.fetchAll().getCount(), 1);
  }

  public void testFetch_notExisting() {
    long id = dbAdapter.insert(eventIDs[0], ruleNames[0], ruleDescs[0], true);
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
    long id = dbAdapter.insert(eventIDs[0], ruleNames[0], ruleDescs[0], true);
    assertTrue(dbAdapter.delete(id));
    // Validate it cannot be fetched.
    Cursor cursor = dbAdapter.fetch(id);
    assertEquals(cursor.getCount(), 0);
  }

  public void testDelete_notExisting() {
    long id = dbAdapter.insert(eventIDs[0], ruleNames[0], ruleDescs[0], true);
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
    long id = dbAdapter.insert(eventIDs[0], ruleNames[0], ruleDescs[0], false);
    assertTrue(dbAdapter.update(id, eventIDs[1], ruleNames[1], ruleDescs[1], true));
    // Validate the updated record.
    Cursor cursor = dbAdapter.fetch(id);
    assertCursorEquals(cursor, eventIDs[1], ruleNames[1], ruleDescs[1], true);
  }

  public void testUpdate_notExisting() {
    long id = dbAdapter.insert(eventIDs[0], ruleNames[0], ruleDescs[0], true);
    dbAdapter.delete(id);
    assertFalse(dbAdapter.update(id, eventIDs[1], ruleNames[1], ruleDescs[1], false));
  }

  public void testUpdate_withNullValues() {
    long id = dbAdapter.insert(eventIDs[1], ruleNames[1], ruleDescs[1], false);
    assertFalse(dbAdapter.update(id, null, null, null, null));
    // Validate the updated record.
    Cursor cursor = dbAdapter.fetch(id);
    assertCursorEquals(cursor, eventIDs[1], ruleNames[1], ruleDescs[1], false);
  }

  public void testUpdate_illegalArgumentException() {
    Exception expected = null;
    try {
      dbAdapter.update(null, eventIDs[0], ruleNames[0], ruleDescs[0], false);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);
  }

  public void testFetchAll() {
    dbAdapter.insert(eventIDs[0], ruleNames[0], ruleDescs[0], false);
    dbAdapter.insert(eventIDs[1], ruleNames[1], ruleDescs[1], false);
    dbAdapter.insert(eventIDs[2], ruleNames[2], ruleDescs[2], false);

    Cursor cursor = dbAdapter.fetchAll();
    assertEquals(cursor.getCount(), 3);
  }

  public void testFetchAll_withParameters() {
    dbAdapter.insert(eventIDs[0], ruleNames[0], ruleDescs[0], true);
    dbAdapter.insert(eventIDs[0], ruleNames[1], ruleDescs[1], true);
    dbAdapter.insert(eventIDs[2], ruleNames[1], ruleDescs[2], true);

    assertTrue(dbAdapter.fetchAll(null, null, null, null, null).getCount() == 3);
    assertTrue(dbAdapter.fetchAll(eventIDs[0], null, null, null, null).getCount() == 2);
    assertTrue(dbAdapter.fetchAll(null, ruleNames[1], null, null, null).getCount() == 2);
    assertTrue(dbAdapter.fetchAll(null, null, ruleDescs[2], null, null).getCount() == 1);
    assertTrue(dbAdapter.fetchAll(null, null, ruleDescs[2], false, null).getCount() == 0);
  }

  public void testFetchAll_withSortOrder() throws InterruptedException {
    long[] ids = new long[3];

    // Insert record 1, 2, 3
    for (int i = 0; i <= 2; i++) {
      ids[i] = dbAdapter.insert(eventIDs[i], ruleNames[i], ruleDescs[i], true);
      Thread.sleep(1000); // SQLite3 datetime type doesn't support less than 1 second
    }

    // Update record 3, 2, 1
    for (int i = 2; i >= 0; i--) {
      dbAdapter.update(ids[i], eventIDs[i], ruleNames[i], ruleDescs[i], true);
      Thread.sleep(1000); // SQLite3 datetime type doesn't support less than 1 second
    }

    // Sort by latest created, verify the order
    Cursor cursor1 = dbAdapter
        .fetchAll(null, null, null, null, RuleDbAdapter.KEY_CREATED + " DESC");
    for (int i = 2; i >= 0; i--) {
      cursor1.moveToNext();
      assertCursorEquals(cursor1, eventIDs[i], ruleNames[i], ruleDescs[i], true);
    }
    assertFalse(cursor1.moveToNext());

    // Sort by latest updated, verify the order
    Cursor cursor2 = dbAdapter
        .fetchAll(null, null, null, null, RuleDbAdapter.KEY_UPDATED + " DESC");
    for (int i = 0; i <= 2; i++) {
      cursor2.moveToNext();
      assertCursorEquals(cursor2, eventIDs[i], ruleNames[i], ruleDescs[i], true);
    }
    assertFalse(cursor2.moveToNext());
  }

  public void testFetchAll_withSQLiteException() {
    for (int i = 0; i <= 2; i++) {
      dbAdapter.insert(eventIDs[i], ruleNames[i], ruleDescs[i], true);
    }
    Exception expected = null;
    try {
      dbAdapter.fetchAll(null, null, null, null, "InvalidOrderBy");
    } catch (SQLiteException e) {
      expected = e;
    }
    assertNotNull(expected);
  }

  /**
   * Helper method to assert a cursor pointing to a rule record that matches the parameters
   */
  private void assertCursorEquals(Cursor cursor, long eventID, String ruleName, String ruleDesc,
      boolean enabled) {
    assertEquals(cursor.getInt(cursor.getColumnIndex(RuleDbAdapter.KEY_EVENTID)), eventID);
    assertEquals(cursor.getString(cursor.getColumnIndex(RuleDbAdapter.KEY_RULENAME)), ruleName);
    assertEquals(cursor.getString(cursor.getColumnIndex(RuleDbAdapter.KEY_RULEDESC)), ruleDesc);
    assertEquals(cursor.getInt(cursor.getColumnIndex(RuleDbAdapter.KEY_ENABLED)) == 1, enabled);
  }

}