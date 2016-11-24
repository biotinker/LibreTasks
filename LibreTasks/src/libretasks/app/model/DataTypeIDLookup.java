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

import libretasks.app.model.db.DataTypeDbAdapter;
import libretasks.app.model.db.DbHelper;

/**
 * This class can be used to query the database for dataTypeID efficiently.
 */
public class DataTypeIDLookup {
  private static final String TAG = DataTypeIDLookup.class.getSimpleName();
  private DataTypeDbAdapter dataTypeDbAdapter;
  private DbHelper omnidroidDbHelper;
  private SQLiteDatabase database;
  private HashMap<String, Long> dataTypeIDMap;
  
  public DataTypeIDLookup(Context context){
    omnidroidDbHelper = new DbHelper(context);
    database = omnidroidDbHelper.getWritableDatabase();
    dataTypeDbAdapter = new DataTypeDbAdapter(database);
    dataTypeIDMap= new HashMap<String, Long>();
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
   * Query the dataTypeID with dataTypeName. This method is caching the result into dataTypeIDMap.
   * 
   * @param dataTypeName
   *          is name of the dataType
   *          
   * @return dataTypeID that matches dataTypeName or -1 if no match
   * @throws IllegalStateException
   *           when this object is already closed
   */
  public long getDataTypeID(String dataTypeName) {
    if (dataTypeName == null) {
      throw new IllegalArgumentException("Arguments null.");
    } else if (!database.isOpen()) {
      throw new IllegalStateException(TAG + " is already closed.");
    }
    
    // Return it if the id is already cached.
    Long cachedDataTypeID = dataTypeIDMap.get(dataTypeName);
    if (cachedDataTypeID != null) {
      return cachedDataTypeID;
    }
    
    // Try to find dataTypeID
    long dataTypeID = -1;
    Cursor cursor = dataTypeDbAdapter.fetchAll(dataTypeName, null);
    if (cursor.getCount() > 0) {
      cursor.moveToFirst();
      dataTypeID = cursor.getLong(cursor.getColumnIndex(DataTypeDbAdapter.KEY_DATATYPEID));
    }
    cursor.close();
    
    // Cache it if the id is valid
    if (dataTypeID > 0) {
      dataTypeIDMap.put(dataTypeName, dataTypeID);
    }
    
    return dataTypeID;
  }
}
