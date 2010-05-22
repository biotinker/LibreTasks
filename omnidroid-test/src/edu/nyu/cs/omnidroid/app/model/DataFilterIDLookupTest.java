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

import edu.nyu.cs.omnidroid.app.model.DataFilterIDLookup;
import edu.nyu.cs.omnidroid.app.model.db.DataFilterDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.DataTypeDbAdapter;
import edu.nyu.cs.omnidroid.app.model.db.DbHelper;
import android.test.AndroidTestCase;


/**
 * Android Unit Test for {@link DataFilterIDLookup} class.
 */
public class DataFilterIDLookupTest extends AndroidTestCase {

  private DataFilterIDLookup dataFilterIDLookup;
  private DataTypeDbAdapter dataTypeDbAdapter;
  private DataFilterDbAdapter dataFilterDbAdapter;
  private DbHelper omnidroidDbHelper;

  // Data for testing
  private static String[] dataTypeNames = { "Type11", "Type12" };
  private Long[] dataTypeIDs;
  private static String[] dataFilterNames = { "Filter1", "Filter2", "Filter3" };
  private static String[] dataFilterDisplayNames = { "Filter1 display", "Filter2 display", 
    "Filter3 display" };
  private Long[] dataFilterIDs;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    omnidroidDbHelper = new DbHelper(this.getContext());
    dataFilterIDLookup = new DataFilterIDLookup(this.getContext());

    dataTypeDbAdapter = new DataTypeDbAdapter(omnidroidDbHelper.getWritableDatabase());
    dataFilterDbAdapter = new DataFilterDbAdapter(omnidroidDbHelper.getWritableDatabase());

    omnidroidDbHelper.backup();

    dataTypeDbAdapter.deleteAll();
    dataFilterDbAdapter.deleteAll();
    
    prePopulate();
  }

  @Override
  protected void tearDown() throws Exception {
    dataTypeDbAdapter.deleteAll();
    dataFilterDbAdapter.deleteAll();

    // Try to restore the database
    if (omnidroidDbHelper.isBackedUp()) {
      omnidroidDbHelper.restore();
    }

    omnidroidDbHelper.close();
    super.tearDown();
  }

  private void prePopulate() {
    dataTypeIDs = new Long[2];
    dataFilterIDs = new Long[2 * 3];

    dataTypeIDs[0] = dataTypeDbAdapter.insert(dataTypeNames[0], "");
    dataTypeIDs[1] = dataTypeDbAdapter.insert(dataTypeNames[1], "");

    /*
     * DataFilterIDLookup only support filters with the same filterOn and compareWith datatypes now.
     */
    dataFilterIDs[0] = dataFilterDbAdapter.insert(
        dataFilterNames[0], dataFilterDisplayNames[0], dataTypeIDs[0], dataTypeIDs[0]);
    
    dataFilterIDs[1] = dataFilterDbAdapter.insert(
        dataFilterNames[1], dataFilterDisplayNames[1], dataTypeIDs[0], dataTypeIDs[0]);
    
    dataFilterIDs[2] = dataFilterDbAdapter.insert(
        dataFilterNames[2], dataFilterDisplayNames[2], dataTypeIDs[0], dataTypeIDs[0]);

    dataFilterIDs[3] = dataFilterDbAdapter.insert(
        dataFilterNames[0], dataFilterDisplayNames[0], dataTypeIDs[1], dataTypeIDs[1]);
    
    dataFilterIDs[4] = dataFilterDbAdapter.insert(
        dataFilterNames[1], dataFilterDisplayNames[1], dataTypeIDs[1], dataTypeIDs[1]);
    
    dataFilterIDs[5] = dataFilterDbAdapter.insert(
        dataFilterNames[2], dataFilterDisplayNames[2], dataTypeIDs[1], dataTypeIDs[1]);
  }

  public void testGetDataFilterID() {
    assertEquals(dataFilterIDs[0].longValue(), 
        dataFilterIDLookup.getDataFilterID(dataTypeNames[0], dataFilterNames[0]));
    
    assertEquals(dataFilterIDs[1].longValue(), 
        dataFilterIDLookup.getDataFilterID(dataTypeNames[0], dataFilterNames[1]));
    
    assertEquals(dataFilterIDs[2].longValue(), 
        dataFilterIDLookup.getDataFilterID(dataTypeNames[0], dataFilterNames[2]));
    
    assertEquals(dataFilterIDs[3].longValue(), 
        dataFilterIDLookup.getDataFilterID(dataTypeNames[1], dataFilterNames[0]));
    
    assertEquals(dataFilterIDs[4].longValue(), 
        dataFilterIDLookup.getDataFilterID(dataTypeNames[1], dataFilterNames[1]));
    
    assertEquals(dataFilterIDs[5].longValue(), 
        dataFilterIDLookup.getDataFilterID(dataTypeNames[1], dataFilterNames[2]));
  }
  
  // TODO(ehotou) create test for caching performance.
}