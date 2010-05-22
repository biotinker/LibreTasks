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
import edu.nyu.cs.omnidroid.app.model.db.RegisteredActionDbAdapter;
import android.database.Cursor;
import android.test.AndroidTestCase;

/**
 * Android Unit Test for {@link RegisteredActionDbAdapter} class.
 */
public class RegisteredActionDbAdapterTest extends AndroidTestCase {

  private RegisteredActionDbAdapter dbAdapter;
  private DbHelper omnidroidDbHelper;

  // Data for testing
  private String[] actionNames = { "Action1", "Action2", "Action3" };
  private Long[] appIDs = { Long.valueOf(1), Long.valueOf(2), Long.valueOf(3) };

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    omnidroidDbHelper = new DbHelper(this.getContext());
    dbAdapter = new RegisteredActionDbAdapter(omnidroidDbHelper.getWritableDatabase());
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
    long id1 = dbAdapter.insert(actionNames[0], appIDs[0]);
    assertTrue(id1 != -1);

    long id2 = dbAdapter.insert(actionNames[1], appIDs[1]);
    assertTrue(id1 != id2);
  }

  public void testInsert_illegalArgumentException() {
    Exception expected = null;
    try {
      dbAdapter.insert(null, appIDs[0]);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);

    expected = null;
    try {
      dbAdapter.insert(actionNames[0], null);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);
  }

  public void testFetch() {
    long id = dbAdapter.insert(actionNames[0], appIDs[0]);
    Cursor cursor = dbAdapter.fetch(id);
    // Validate the inserted record.
    assertCursorEquals(cursor, actionNames[0], appIDs[0]);
    // Validate the record count
    assertEquals(dbAdapter.fetchAll().getCount(), 1);
  }

  public void testFetch_notExisting() {
    long id = dbAdapter.insert(actionNames[0], appIDs[0]);
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
    long id = dbAdapter.insert(actionNames[0], appIDs[0]);
    assertTrue(dbAdapter.delete(id));
    // Validate it cannot be fetched.
    Cursor cursor = dbAdapter.fetch(id);
    assertEquals(cursor.getCount(), 0);
  }

  public void testDelete_notExisting() {
    long id = dbAdapter.insert(actionNames[0], appIDs[0]);
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
    long id = dbAdapter.insert(actionNames[0], appIDs[0]);
    assertTrue(dbAdapter.update(id, actionNames[1], appIDs[1]));
    // Validate the updated record.
    Cursor cursor = dbAdapter.fetch(id);
    assertCursorEquals(cursor, actionNames[1], appIDs[1]);
  }

  public void testUpdate_notExisting() {
    long id = dbAdapter.insert(actionNames[0], appIDs[0]);
    dbAdapter.delete(id);
    assertFalse(dbAdapter.update(id, actionNames[1], appIDs[1]));
  }

  public void testUpdate_withNullValues() {
    long id = dbAdapter.insert(actionNames[0], appIDs[0]);
    assertFalse(dbAdapter.update(id, null, null));
    // Validate the updated record.
    Cursor cursor = dbAdapter.fetch(id);
    assertCursorEquals(cursor, actionNames[0], appIDs[0]);
  }

  public void testUpdate_illegalArgumentException() {
    Exception expected = null;
    try {
      dbAdapter.update(null, actionNames[0], appIDs[0]);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);
  }

  public void testFetchAll() {
    dbAdapter.insert(actionNames[0], appIDs[0]);
    dbAdapter.insert(actionNames[1], appIDs[0]);
    dbAdapter.insert(actionNames[2], appIDs[1]);

    Cursor cursor = dbAdapter.fetchAll();
    assertEquals(cursor.getCount(), 3);
  }

  public void testFetchAll_withParameters() {
    dbAdapter.insert(actionNames[0], appIDs[0]);
    dbAdapter.insert(actionNames[1], appIDs[0]);
    dbAdapter.insert(actionNames[2], appIDs[1]);

    assertEquals(dbAdapter.fetchAll(null, null).getCount(), 3);
    assertEquals(dbAdapter.fetchAll(null, appIDs[0]).getCount(), 2);
    assertEquals(dbAdapter.fetchAll(actionNames[0], appIDs[0]).getCount(), 1);
    assertEquals(dbAdapter.fetchAll(actionNames[1], appIDs[1]).getCount(), 0);
  }

  /**
   * Helper method to assert a cursor pointing to a action record that matches the parameters
   */
  private void assertCursorEquals(Cursor cursor, String actionName, Long appID) {
    assertEquals(cursor.getString(cursor.getColumnIndex(RegisteredActionDbAdapter.KEY_ACTIONNAME)),
        actionName);
    assertEquals(cursor.getInt(cursor.getColumnIndex(RegisteredActionDbAdapter.KEY_APPID)), appID
        .intValue());
  }

}
