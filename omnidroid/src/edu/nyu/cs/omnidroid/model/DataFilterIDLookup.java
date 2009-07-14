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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.nyu.cs.omnidroid.model.db.DataFilterDbAdapter;
import edu.nyu.cs.omnidroid.model.db.DataTypeDbAdapter;
import edu.nyu.cs.omnidroid.model.db.DbHelper;
import edu.nyu.cs.omnidroid.util.DualKeyHashMap;
import edu.nyu.cs.omnidroid.util.DualKeyMap;

/**
 * This class can be used to query the database for filterID efficiently.
 */
public class DataFilterIDLookup {

  private DataTypeDbAdapter dataTypeDbAdapter;
  private DataFilterDbAdapter dataFilterDbAdapter;
  private DbHelper omnidroidDbHelper;
  private DualKeyMap<String, String, Long> filterIDMap;

  public DataFilterIDLookup(Context context) {
    omnidroidDbHelper = new DbHelper(context);
    SQLiteDatabase database = omnidroidDbHelper.getWritableDatabase();
    dataTypeDbAdapter = new DataTypeDbAdapter(database);
    dataFilterDbAdapter = new DataFilterDbAdapter(database);
    filterIDMap = new DualKeyHashMap<String, String, Long>();
  }

  public void close() {
    omnidroidDbHelper.close();
  }
  
  /**
   * Query the filterID with dataTypeName and dataFilterNames. This method is caching the result
   * into filterIDMap.
   * 
   * @param dataTypeName
   *          is name of the dataType
   * 
   * @param dataFilterName
   *          is name of the dataFilter
   * 
   * @return filterID that matches dataTypeName and dataFilterName or -1 if no match
   */
  public long getFilterID(String dataTypeName, String dataFilterName) {
    if (dataTypeName == null || dataFilterName == null) {
      throw new IllegalArgumentException("Arguments null.");
    }
    
    // Return it if the id is already cached.
    if (filterIDMap.get(dataTypeName, dataFilterName) != null) {
      return filterIDMap.get(dataTypeName, dataFilterName);
    }

    // Try to find dataTypeID
    long dataTypeID = -1;
    Cursor cursor1 = dataTypeDbAdapter.fetchAll(dataTypeName, null);
    if (cursor1.getCount() > 0) {
      cursor1.moveToFirst();
      dataTypeID = cursor1.getLong(cursor1.getColumnIndex(DataTypeDbAdapter.KEY_DATATYPEID));
    }
    cursor1.close();

    // Try to find dataFilterID
    long dataFilterID = -1;
    Cursor cursor2 = dataFilterDbAdapter.fetchAll(dataFilterName, dataTypeID);
    if (cursor2.getCount() > 0) {
      cursor2.moveToFirst();
      dataFilterID = cursor2.getLong(cursor2.getColumnIndex(DataFilterDbAdapter.KEY_DATAFILTERID));
    }
    cursor2.close();
    
    // Cache it if the id is valid
    if(dataFilterID > 0) {
      filterIDMap.put(dataTypeName, dataFilterName, dataFilterID);
    }
    
    return dataFilterID;
  }
}