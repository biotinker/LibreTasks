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
package libretasks.app.controller.util;

/**
 * This class can be used to hold two object to form a HashMap key.
 * TODO(ehotou) To replace StringMap throughout the application with this more generic version.
 * 
 * @param <T1>
 *          is the type of first object
 * 
 * @param <T2>
 *          is the type of second object
 */
public class DualKey<T1, T2> {

  /* Dual Keys */
  private final T1 key1;
  private final T2 key2;

  /* The main constructor that takes 2 keys */
  public DualKey(T1 key1, T2 key2) {
    super();
    
    if (key1 == null || key2 == null) {
      throw new IllegalArgumentException("The keys must not be null");
    }

    this.key1 = key1;
    this.key2 = key2;
  }

  /**
   * @return the first key
   */
  public T1 getKey1() {
    return key1;
  }

  /**
   * @return the second key
   */
  public T2 getKey2() {
    return key2;
  }

  @Override
  public int hashCode() {
    int total = 17;
    total = 31 * total + key1.hashCode();
    total = 31 * total + key2.hashCode();
    return total;
  }

  @Override
  public boolean equals(Object other) {

    if (other == this) {
      return true;
    }

    if (!(other instanceof DualKey<?,?>)) {
      return false;
    }
    
    DualKey<?, ?> otherDualKey = (DualKey<?, ?>) other;
    
    // Two DualKey objects are equal only if their key1 and key2 are both equal
    return (otherDualKey.getKey1().equals(this.key1) && otherDualKey.getKey2().equals(this.key2));
  }
}
