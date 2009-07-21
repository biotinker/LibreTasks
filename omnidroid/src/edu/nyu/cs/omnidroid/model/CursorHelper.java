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

import android.database.Cursor;

/**
 * This class provide with some helper method for manipulating cursor
 */
public class CursorHelper{
  
  /**
   * Helper method to get integer column value from cursor.
   * 
   * @param cursor
   *          is the cursor to get value from
   *          
   * @param columnName
   *          is name of the column to get
   *          
   * @return integer value of the specific column within the cursor
   */
  public static int getIntFromCursor(Cursor cursor, String columnName) {
    return cursor.getInt(cursor.getColumnIndex(columnName));
  }
  
  /**
   * Helper method to get integer column value from cursor.
   * 
   * @param cursor
   *          is the cursor to get value from
   *          
   * @param columnName
   *          is name of the column to get
   *          
   * @return integer value of the specific column within the cursor
   */
  public static String getStringFromCursor(Cursor cursor, String columnName) {
    return cursor.getString(cursor.getColumnIndex(columnName));
  }
  
}