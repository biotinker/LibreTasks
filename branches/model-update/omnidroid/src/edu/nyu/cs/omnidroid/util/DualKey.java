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
package edu.nyu.cs.omnidroid.util;

/**
 * This class can be used to hold two object to form a HashMap key.
 * 
 * @param <T1>
 *          is the type of first object
 * 
 * @param <T2>
 *          is the type of second object
 */
public class DualKey<T1, T2> {

  /* The hash code */
  private final int hashCode;

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

    int total = 0;
    total ^= key1.hashCode();
    total ^= key2.hashCode();

    this.hashCode = total;
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
    return hashCode;
  }

  @Override
  public boolean equals(Object other) {

    if (other == this) {
      return true;
    }

    if (!(other instanceof DualKey)) {
      return false;
    }
    
    DualKey<?, ?> otherDualKey = (DualKey<?, ?>) other;
    
    // Two DualKey objects are equal only if their key1 and key2 are both equal
    return (otherDualKey.getKey1().equals(this.key1) && otherDualKey.getKey2().equals(this.key2));
  }
}