/*  
 * Copyright (c) 2016  LibreTasks - https://github.com/biotinker/LibreTasks  
 *  
 *  This file is free software: you may copy, redistribute and/or modify it  
 *  under the terms of the GNU General Public License as published by the  
 *  Free Software Foundation, either version 3 of the License, or (at your  
 *  option) any later version.  
 *  
 *  This file is distributed in the hope that it will be useful, but  
 *  WITHOUT ANY WARRANTY; without even the implied warranty of  
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  
 *  General Public License for more details.  
 *  
 *  You should have received a copy of the GNU General Public License  
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 *  
 * This file incorporates work covered by the following copyright and  
 * permission notice:  
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
package libretasks.app.model;

import java.util.HashMap;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import libretasks.app.controller.util.DualKey;
import libretasks.app.model.db.DataFilterDbAdapter;
import libretasks.app.model.db.DataTypeDbAdapter;
import libretasks.app.model.db.DbHelper;

/**
 * This class can be used to query the database for dataFilterID efficiently.
 */
public class DataFilterIDLookup {
  private static final String TAG = DataFilterIDLookup.class.getSimpleName();
  private DataTypeDbAdapter dataTypeDbAdapter;
  private DataFilterDbAdapter dataFilterDbAdapter;
  private DbHelper omnidroidDbHelper;
  private SQLiteDatabase database; 
  private HashMap<DualKey<String, String>, Long> dataFilterIDMap;

  public DataFilterIDLookup(Context context) {
    omnidroidDbHelper = new DbHelper(context);
    database = omnidroidDbHelper.getWritableDatabase();
    dataTypeDbAdapter = new DataTypeDbAdapter(database);
    dataFilterDbAdapter = new DataFilterDbAdapter(database);
    dataFilterIDMap = new HashMap<DualKey<String, String>, Long>();
  }

  /**
   * Close this database helper object. Attempting to use this object after this call will cause an
   * {@link IllegalStateException} being raised.
   */
  public void close() {
    Log.i(TAG, "closing database.");
    database.close();
    
    // Not necessary, but also close all omnidroidDbHelper databases just in case.
    omnidroidDbHelper.close();
  }

  /**
   * This is for filter with same filterOn and compareWith dataType, forward to 
   * {@link #getDataFilterID(String, String, String)}. with compareDataTypeName set to dataTypeName.
   * 
   * @param dataTypeName
   * @param dataFilterName
   * @return
   * @throws IllegalStateException
   *           when this object is already closed
   */
  public long getDataFilterID(String dataTypeName, String dataFilterName) {
    if (!database.isOpen()) {
      throw new IllegalStateException(TAG + " is already closed.");
    }
    
    return getDataFilterID(dataTypeName, dataTypeName, dataFilterName);
  }
  
  /**
   * Query the dataFilterID with dataTypeName and dataFilterNames. This method is caching the result
   * into filterIDMap.
   * 
   * @param dataTypeName
   *          is name of the dataType it filters on
   *          
   * @param compareDataTypeName
   *          is name of the dataType it filters with
   * 
   * @param dataFilterName
   *          is name of the dataFilter
   * 
   * @return filterID that matches dataTypeName and dataFilterName or -1 if no match
   * @throws IllegalStateException
   *           when this object is already closed
   */
  public long getDataFilterID(String dataTypeName, String compareDataTypeName, String dataFilterName) {
    if (dataTypeName == null || dataFilterName == null) {
      throw new IllegalArgumentException("Arguments null.");
    } else if (!database.isOpen()) {
      throw new IllegalStateException(TAG + " is already closed.");
    }

    DualKey<String, String> key = new DualKey<String, String>(dataTypeName, dataFilterName);

    // Return it if the id is already cached.
    Long cachedDataFilterID = dataFilterIDMap.get(key);
    if (cachedDataFilterID != null) {
      return cachedDataFilterID;
    }

    // Try to find dataTypeID
    long dataTypeID = -1;
    Cursor cursor = dataTypeDbAdapter.fetchAll(dataTypeName, null);
    if (cursor.getCount() > 0) {
      cursor.moveToFirst();
      dataTypeID = cursor.getLong(cursor.getColumnIndex(DataTypeDbAdapter.KEY_DATATYPEID));
    }
    cursor.close();
    
    // Try to find compareDataTypeID
    long compareDataTypeID = -1;
    cursor = dataTypeDbAdapter.fetchAll(compareDataTypeName, null);
    if (cursor.getCount() > 0) {
      cursor.moveToFirst();
      compareDataTypeID = cursor.getLong(cursor.getColumnIndex(DataTypeDbAdapter.KEY_DATATYPEID));
    }
    cursor.close();

    // Try to find dataFilterID
    long dataFilterID = -1;
    cursor = dataFilterDbAdapter.fetchAll(dataFilterName, null, dataTypeID, compareDataTypeID);
    if (cursor.getCount() > 0) {
      cursor.moveToFirst();
      dataFilterID = cursor.getLong(cursor.getColumnIndex(DataFilterDbAdapter.KEY_DATAFILTERID));
    }
    cursor.close();

    // Cache it if the id is valid
    if (dataFilterID > 0) {
      dataFilterIDMap.put(key, dataFilterID);
    }

    return dataFilterID;
  }
}
