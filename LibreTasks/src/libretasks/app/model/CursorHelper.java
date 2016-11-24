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

import android.database.Cursor;

/**
 * This class provide with some helper method for manipulating cursor
 */
public class CursorHelper {

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
   * @return String value of the specific column within the cursor
   */
  public static String getStringFromCursor(Cursor cursor, String columnName) {
    return cursor.getString(cursor.getColumnIndex(columnName));
  }

  /**
   * Helper method to get long column value from cursor.
   * 
   * @param cursor
   *          is the cursor to get value from
   * 
   * @param columnName
   *          is name of the column to get
   * 
   * @return long value of the specific column within the cursor
   */
  public static long getLongFromCursor(Cursor cursor, String columnName) {
    return cursor.getLong(cursor.getColumnIndex(columnName));
  }

  /**
   * Helper method to get boolean value from a column
   * 
   * @param cursor
   *          is the cursor to get value from
   * 
   * @param columnName
   *          is name of the column to get
   * 
   * @return boolean value of the specific column within the cursor
   */
  public static boolean getBooleanFromCursor(Cursor cursor, String columnName) {
    return cursor.getInt(cursor.getColumnIndex(columnName)) == 1;
  }

}
