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

import edu.nyu.cs.omnidroid.app.model.db.DataTypeDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.DbHelper;
import android.database.Cursor;
import android.test.AndroidTestCase;

/**
 * Android Unit Test for {@link DataTypeDbAdapter} class.
 */
public class DataTypeDbAdapterTest extends AndroidTestCase {

  private DataTypeDbAdapter dbAdapter;
  private DbHelper omnidroidDbHelper;

  // Data for testing
  private String[] dataTypeNames = { "Type1", "Type2", "Type3" };
  private String[] dataTypeClassNames = { "Class1", "Class2", "Class3" };

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    omnidroidDbHelper = new DbHelper(this.getContext());
    dbAdapter = new DataTypeDbAdapter(omnidroidDbHelper.getWritableDatabase());
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
    long id1 = dbAdapter.insert(dataTypeNames[0], dataTypeClassNames[0]);
    assertTrue(id1 != -1);

    long id2 = dbAdapter.insert(dataTypeNames[0], dataTypeClassNames[0]);
    assertTrue(id1 != id2);
  }

  public void testInsert_illegalArgumentException() {
    Exception expected = null;
    try {
      dbAdapter.insert(null, dataTypeClassNames[0]);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);

    expected = null;
    try {
      dbAdapter.insert(dataTypeNames[0], null);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);
  }

  public void testFetch() {
    long id = dbAdapter.insert(dataTypeNames[0], dataTypeClassNames[0]);
    Cursor cursor = dbAdapter.fetch(id);
    // Validate the inserted record.
    assertCursorEquals(cursor, dataTypeNames[0], dataTypeClassNames[0]);
    // Validate the record count
    assertEquals(dbAdapter.fetchAll().getCount(), 1);
  }

  public void testFetch_notExisting() {
    long id = dbAdapter.insert(dataTypeNames[0], dataTypeClassNames[0]);
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
    long id = dbAdapter.insert(dataTypeNames[0], dataTypeClassNames[0]);
    assertTrue(dbAdapter.delete(id));
    // Validate it cannot be fetched.
    Cursor cursor = dbAdapter.fetch(id);
    assertEquals(cursor.getCount(), 0);
  }

  public void testDelete_notExisting() {
    long id = dbAdapter.insert(dataTypeNames[0], dataTypeClassNames[0]);
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
    long id = dbAdapter.insert(dataTypeNames[0], dataTypeClassNames[0]);
    assertTrue(dbAdapter.update(id, dataTypeNames[1], dataTypeClassNames[1]));
    // Validate the updated record.
    Cursor cursor = dbAdapter.fetch(id);
    assertCursorEquals(cursor, dataTypeNames[1], dataTypeClassNames[1]);
  }

  public void testUpdate_notExisting() {
    long id = dbAdapter.insert(dataTypeNames[0], dataTypeClassNames[0]);
    dbAdapter.delete(id);
    assertFalse(dbAdapter.update(id, dataTypeNames[1], dataTypeClassNames[1]));
  }

  public void testUpdate_withNullValues() {
    long id = dbAdapter.insert(dataTypeNames[0], dataTypeClassNames[0]);
    assertFalse(dbAdapter.update(id, null, null));
    // Validate the updated record.
    Cursor cursor = dbAdapter.fetch(id);
    assertCursorEquals(cursor, dataTypeNames[0], dataTypeClassNames[0]);
  }

  public void testUpdate_illegalArgumentException() {
    Exception expected = null;
    try {
      dbAdapter.update(null, dataTypeNames[1], dataTypeClassNames[1]);
    } catch (IllegalArgumentException e) {
      expected = e;
    }
    assertNotNull(expected);
  }

  public void testFetchAll() {
    dbAdapter.insert(dataTypeNames[0], dataTypeClassNames[0]);
    dbAdapter.insert(dataTypeNames[1], dataTypeClassNames[1]);
    dbAdapter.insert(dataTypeNames[2], dataTypeClassNames[2]);

    Cursor cursor = dbAdapter.fetchAll();
    assertEquals(cursor.getCount(), 3);
  }

  public void testFetchAll_withParameters() {
    dbAdapter.insert(dataTypeNames[0], dataTypeClassNames[0]);
    dbAdapter.insert(dataTypeNames[1], dataTypeClassNames[0]);
    dbAdapter.insert(dataTypeNames[2], dataTypeClassNames[2]);

    assertTrue(dbAdapter.fetchAll(null, null).getCount() == 3);
    assertTrue(dbAdapter.fetchAll(null, dataTypeClassNames[0]).getCount() == 2);
    assertTrue(dbAdapter.fetchAll(dataTypeNames[0], null).getCount() == 1);
    assertTrue(dbAdapter.fetchAll(dataTypeNames[0], dataTypeClassNames[1]).getCount() == 0);
  }

  /**
   * Helper method to assert a cursor pointing to a filter record that matches the parameters
   */
  private void assertCursorEquals(Cursor cursor, String dataTypeName, String dataTypeClassName) {
    assertEquals(cursor.getString(cursor.getColumnIndex(DataTypeDbAdapter.KEY_DATATYPENAME)),
        dataTypeName);
    assertEquals(cursor.getString(cursor.getColumnIndex(DataTypeDbAdapter.KEY_DATATYPECLASSNAME)),
        dataTypeClassName);
  }

}
