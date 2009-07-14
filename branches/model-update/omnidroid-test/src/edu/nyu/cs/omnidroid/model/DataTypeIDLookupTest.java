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
package edu.nyu.cs.omnidroid.model;

import edu.nyu.cs.omnidroid.model.db.DataTypeDbAdapter;
import edu.nyu.cs.omnidroid.model.db.DbHelper;
import android.test.AndroidTestCase;

/**
 * Android Unit Test for {@link DataTypeIDLookup} class.
 */
public class DataTypeIDLookupTest extends AndroidTestCase {
  
  private DataTypeIDLookup dataTypeIDLookup;
  private DataTypeDbAdapter dataTypeDbAdapter;
  private DbHelper omnidroidDbHelper;
  
  //Data for testing
  private static String[] dataTypeNames = { "Type1", "Type2" };
  private Long[] dataTypeIDs;
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    omnidroidDbHelper = new DbHelper(this.getContext());
    dataTypeIDLookup = new DataTypeIDLookup(this.getContext());
    
    dataTypeDbAdapter = new DataTypeDbAdapter(omnidroidDbHelper.getWritableDatabase());
    
    omnidroidDbHelper.backup();
    
    dataTypeDbAdapter.deleteAll();
    
    prePopulate();
  }
  
  @Override
  protected void tearDown() throws Exception {
    dataTypeDbAdapter.deleteAll();
    
    // Try to restore the database
    if (omnidroidDbHelper.isBackedUp()) {
      omnidroidDbHelper.restore();
    }
    
    omnidroidDbHelper.close();
    super.tearDown();
  }
  
  private void prePopulate() {
    dataTypeIDs = new Long[2];
    
    dataTypeIDs[0] = dataTypeDbAdapter.insert(dataTypeNames[0], "");
    dataTypeIDs[1] = dataTypeDbAdapter.insert(dataTypeNames[1], "");
  }
  
  public void testGetDataTypeID() {
    assertEquals(dataTypeIDs[0].longValue(), dataTypeIDLookup.getDataTypeID(dataTypeNames[0]));
    assertEquals(dataTypeIDs[0].longValue(), dataTypeIDLookup.getDataTypeID(dataTypeNames[0]));
  }
  
  // TODO(ehotou) create test for caching performance.
}