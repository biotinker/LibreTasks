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

import edu.nyu.cs.omnidroid.app.model.CursorHelper;
import edu.nyu.cs.omnidroid.app.model.db.DbHelper;
import edu.nyu.cs.omnidroid.app.model.db.RegisteredEventDbAdapter;
import android.database.Cursor;
import android.test.AndroidTestCase;

/**
 * Android Unit Test for {@link RegisteredEventDbAdapter} class.
 */
public class RegisteredEventDbAdapterTest extends AndroidTestCase {

  private RegisteredEventDbAdapter dbAdapter;
  private DbHelper omnidroidDbHelper;

  // Data for testing
  private String[] eventNames = { "Event1", "Event2", "event3" };
  private Long[] appIDs = { Long.valueOf(11), Long.valueOf(22), Long.valueOf(33) };

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    omnidroidDbHelper = new DbHelper(this.getContext());
    dbAdapter = new RegisteredEventDbAdapter(omnidroidDbHelper.getWritableDatabase());
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
    assertTrue(cursor.getCount() == 0);
  }

  public void testInsert() {
    long id1 = dbAdapter.insert(eventNames[0], appIDs[0]);
    assertTrue(id1 != -1);

    long id2 = dbAdapter.insert(eventNames[1], appIDs[1]);
    assertTrue(id2 != -1);
    assertTrue(id1 != id2);
    
    final String appName = "app";
    final String pkgName = "package";
    
    RegisteredAppDbAdapter appDbAdapter = new RegisteredAppDbAdapter(dbAdapter.database);
    appDbAdapter.insert(appName, pkgName, true, true);
    
    long id3 = dbAdapter.insert(eventNames[2], appName, pkgName);
    assertTrue(id3 != -1);
    assertTrue(id3 != id1);
    assertTrue(id3 != id2);

    appDbAdapter.deleteAll();
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
      dbAdapter.insert(eventNames[0], null);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);
    
    expected = null;
    try {
      dbAdapter.insert(eventNames[0], "", "");
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);
  }

  public void testFetch() {
    long id = dbAdapter.insert(eventNames[0], appIDs[0]);
    Cursor cursor = dbAdapter.fetch(id);
    // Validate the inserted record.
    assertCursorEquals(cursor, eventNames[0], appIDs[0]);
    // Validate the record count
    assertEquals(dbAdapter.fetchAll().getCount(), 1);
  }

  public void testFetch_notExisting() {
    long id = dbAdapter.insert(eventNames[0], appIDs[0]);
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
    long id = dbAdapter.insert(eventNames[0], appIDs[0]);
    assertTrue(dbAdapter.delete(id));
    Cursor cursor = dbAdapter.fetch(id);
    assertEquals(cursor.getCount(), 0);
  }

  public void testDelete_notExisting() {
    long id = dbAdapter.insert(eventNames[0], appIDs[0]);
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
    long id = dbAdapter.insert(eventNames[0], appIDs[0]);
    assertTrue(dbAdapter.update(id, eventNames[1], appIDs[1]));
    // Validate the updated record
    Cursor cursor = dbAdapter.fetch(id);
    assertCursorEquals(cursor, eventNames[1], appIDs[1]);
  }

  public void testUpdate_notExisting() {
    long id = dbAdapter.insert(eventNames[0], appIDs[0]);
    dbAdapter.delete(id);
    assertFalse(dbAdapter.update(id, eventNames[1], appIDs[1]));
  }

  public void testUpdate_withNullValues() {
    long id = dbAdapter.insert(eventNames[0], appIDs[0]);
    assertFalse(dbAdapter.update(id, null, null));
    // Validate the updated record
    Cursor cursor = dbAdapter.fetch(id);
    assertCursorEquals(cursor, eventNames[0], appIDs[0]);
  }

  public void testUpdate_illegalArgumentException() {
    Exception expected = null;
    try {
      dbAdapter.update(null, eventNames[0], appIDs[0]);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);
  }

  public void testFetchAll() {
    dbAdapter.insert(eventNames[0], appIDs[0]);
    dbAdapter.insert(eventNames[1], appIDs[1]);
    dbAdapter.insert(eventNames[2], appIDs[2]);

    Cursor cursor = dbAdapter.fetchAll();
    assertEquals(cursor.getCount(), 3);
  }

  public void testFetchAllOrdered() {
    dbAdapter.insert(eventNames[1], appIDs[1]);
    dbAdapter.insert(eventNames[2], appIDs[2]);
    dbAdapter.insert(eventNames[0], appIDs[0]);

    Cursor cursor = dbAdapter.fetchAllOrdered();
    cursor.moveToFirst();

    int index = 0;
    do {
      assertEquals(eventNames[index], CursorHelper.getStringFromCursor(cursor,
          RegisteredEventDbAdapter.KEY_EVENTNAME));
      index++;
    } while (cursor.moveToNext());

    cursor.close();
  }

  public void testFetchAll_withParameters() {
    dbAdapter.insert(eventNames[0], appIDs[0]);
    dbAdapter.insert(eventNames[1], appIDs[0]);
    dbAdapter.insert(eventNames[2], appIDs[2]);

    assertEquals(dbAdapter.fetchAll(null, null).getCount(), 3);
    assertEquals(dbAdapter.fetchAll(null, appIDs[0]).getCount(), 2);
    assertEquals(dbAdapter.fetchAll(eventNames[0], appIDs[0]).getCount(), 1);
    assertEquals(dbAdapter.fetchAll(eventNames[2], appIDs[0]).getCount(), 0);
  }

  /**
   * Helper method to assert a cursor pointing to a attribute record that matches the parameters
   */
  private void assertCursorEquals(Cursor cursor, String eventName, Long appID) {
    assertEquals(cursor.getString(cursor.getColumnIndex(RegisteredEventDbAdapter.KEY_EVENTNAME)),
        eventName);
    assertEquals(cursor.getInt(cursor.getColumnIndex(RegisteredEventDbAdapter.KEY_APPID)), appID
        .intValue());
  }

}
