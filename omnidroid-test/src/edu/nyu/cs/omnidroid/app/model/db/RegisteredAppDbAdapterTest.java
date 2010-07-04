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
import edu.nyu.cs.omnidroid.app.model.db.RegisteredAppDbAdapter;
import android.database.Cursor;
import android.test.AndroidTestCase;

/**
 * Android Unit Test for {@link RegisteredAppDbAdapter} class.
 */
public class RegisteredAppDbAdapterTest extends AndroidTestCase {

  private RegisteredAppDbAdapter dbAdapter;
  private DbHelper omnidroidDbHelper;

  // Data for testing
  private String[] appNames = { "App1", "App2", "App3" };
  private String[] pkgNames = { "Pkg1", "Pkg2", "Pkg3" };

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    omnidroidDbHelper = new DbHelper(this.getContext());
    dbAdapter = new RegisteredAppDbAdapter(omnidroidDbHelper.getWritableDatabase());
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
    long id1 = dbAdapter.insert(appNames[0], pkgNames[0], true);
    assertTrue(id1 != -1);

    long id2 = dbAdapter.insert(appNames[1], pkgNames[1], true);
    assertTrue(id1 != id2);
  }

  public void testInsert_illegalArgumentException() {
    Exception expected = null;
    try {
      dbAdapter.insert(null, pkgNames[0], true);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);

    expected = null;
    try {
      dbAdapter.insert(appNames[0], null, true);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);

    expected = null;
    try {
      dbAdapter.insert(appNames[0], pkgNames[0], null);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);
  }

  public void testFetch() {
    long id = dbAdapter.insert(appNames[0], pkgNames[0], true);
    Cursor cursor = dbAdapter.fetch(id);
    // Validate the inserted record.
    assertCursorEquals(cursor, appNames[0], pkgNames[0], true);
    // Validate the record count
    assertEquals(dbAdapter.fetchAll().getCount(), 1);
  }

  public void testFetch_notExisting() {
    long id = dbAdapter.insert(appNames[0], pkgNames[0], true);
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
    long id = dbAdapter.insert(appNames[0], pkgNames[0], true);
    assertTrue(dbAdapter.delete(id));
    // Validate it cannot be fetched.
    Cursor cursor = dbAdapter.fetch(id);
    assertEquals(cursor.getCount(), 0);
  }

  public void testDelete_notExisting() {
    long id = dbAdapter.insert(appNames[0], pkgNames[0], true);
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
    long id = dbAdapter.insert(appNames[0], pkgNames[0], true);
    assertTrue(dbAdapter.update(id, appNames[1], pkgNames[1], false, false, null, null));
    // Validate the updated record.
    Cursor cursor = dbAdapter.fetch(id);
    assertCursorEquals(cursor, appNames[1], pkgNames[1], false);
  }

  public void testUpdate_notExisting() {
    long id = dbAdapter.insert(appNames[0], pkgNames[0], true);
    dbAdapter.delete(id);
    assertFalse(dbAdapter.update(id, appNames[1], pkgNames[1], false, false, null, null));
  }

  public void testUpdate_withNullValues() {
    long id = dbAdapter.insert(appNames[0], pkgNames[0], true);
    assertFalse(dbAdapter.update(id, null, null, null, null, null, null));
    // Validate the updated record.
    Cursor cursor = dbAdapter.fetch(id);
    assertCursorEquals(cursor, appNames[0], pkgNames[0], true);
  }

  public void testUpdate_illegalArgumentException() {
    Exception expected = null;
    try {
      dbAdapter.update(null, appNames[0], pkgNames[0], true, false, null, null);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);
  }

  public void testFetchAll() {
    dbAdapter.insert(appNames[0], pkgNames[0], true);
    dbAdapter.insert(appNames[1], pkgNames[1], true);
    dbAdapter.insert(appNames[2], pkgNames[2], false);

    Cursor cursor = dbAdapter.fetchAll();
    assertEquals(cursor.getCount(), 3);
  }

  public void testFetchAll_withParameters() {
    dbAdapter.insert(appNames[0], pkgNames[0], true);
    dbAdapter.insert(appNames[1], pkgNames[1], true);
    dbAdapter.insert(appNames[2], pkgNames[2], false);

    assertTrue(dbAdapter.fetchAll(null, null, null).getCount() == 3);
    assertTrue(dbAdapter.fetchAll(null, null, true).getCount() == 2);
    assertTrue(dbAdapter.fetchAll(null, pkgNames[1], true).getCount() == 1);
    assertTrue(dbAdapter.fetchAll(appNames[2], pkgNames[1], true).getCount() == 0);
  }

  /**
   * Helper method to assert a cursor pointing to a action record that matches the parameters
   */
  private void assertCursorEquals(Cursor cursor, String appName, String pkgName, boolean enabled) {
    assertEquals(cursor.getString(cursor.getColumnIndex(RegisteredAppDbAdapter.KEY_APPNAME)),
        appName);
    assertEquals(cursor.getString(cursor.getColumnIndex(RegisteredAppDbAdapter.KEY_PKGNAME)),
        pkgName);
    assertEquals(cursor.getInt(cursor.getColumnIndex(RegisteredAppDbAdapter.KEY_ENABLED)) == 1,
        enabled);
  }

}
