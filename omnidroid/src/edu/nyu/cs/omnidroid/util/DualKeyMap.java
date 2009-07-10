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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <code>DualKeyMap</code> provides a Map like interface that supports dual keys. A implementation
 * of this interface should be wrapped around a <code>Map</code> implementation like
 * <code>HashMap</code>.
 * 
 * An implementation of the sub-interface <code>DualKey</code> is needed to combine dual key
 * together and used as the key to the internal <code>Map</code> implementation.
 * 
 * @param <K1>
 *          is the type of the first key
 * @param <K2>
 *          is the type of the second key
 * @param <V>
 *          is the type of the third key
 * @see DualKeySortedMap
 * @see DualKeyHashMap
 */
public interface DualKeyMap<K1, K2, V> {

  /**
   * Associates the specified value with the specified key pair in this map If the map previously
   * contained a mapping for this key pair, the old value is replaced by the specified value.
   * 
   * @param key1
   *          is the first key
   * @param key2
   *          is the second key
   * @param value
   *          is the value to be associate with the key pair
   * @return previous value associated with this key pair if there is any.
   */
  V put(K1 key1, K2 key2, V value);

  /**
   * Return the value associated with the specified key pair.
   * 
   * @param key1
   *          is the first key
   * @param key2
   *          is the second key
   * @return is the value associated with this key pair or null if the key pair not exist.
   */
  V get(K1 key1, K2 key2);

  /**
   * Remove a value associated with the specified key pair.
   * 
   * @param key1
   *          is the first key
   * @param key2
   *          is the second key
   * @return the value removed from the map or null if the key pair not exist.
   */
  V remove(K1 key1, K2 key2);

  /**
   * Return whether the map contains an entry with the specified key pair
   * 
   * @param key1
   *          is the first key
   * @param key2
   *          is the second key
   * @return true if the key pair exist or false if it does not.
   */
  boolean containsKey(K1 key1, K2 key2);

  /**
   * Return whether the map contain an entry with the specified value
   * 
   * @param value
   *          is the value to be searched
   * @return true if the value exist or false if it does not.
   */
  boolean containsValue(V value);

  /**
   * Check the size of the map
   * 
   * @return the size of the map
   */
  int size();

  /**
   * Check if the map is empty
   * 
   * @return true if the map contains no entry, or false if it is not empty.
   */
  boolean isEmpty();

  /**
   * Dump all of the content of another DualKeyMap into this one. The type parameter of the map of
   * be dumped need to match this one
   * 
   * @param m
   *          is the DualKeyMap object needed to be dumped to the current one
   */
  void putAll(DualKeyMap<K1, K2, V> m);

  /**
   * Remove all entries from the current map
   */
  void clear();

  /**
   * Return a set view that contains all of the key. These keys should be instances of a class that
   * implements the DualKeyMap.DualKey interface
   * 
   * @return a set of the DualKey of this map
   */
  public Set<? extends DualKey<K1, K2>> keySet();

  /**
   * Return a collection view that contains all of the values.
   * 
   * @return a set of all values associated with this map
   */
  public Collection<V> values();

  /**
   * Return a set view of all mapping contained in this map
   * 
   * @return a set of all mapping
   */
  public Set<? extends Map.Entry<DualKey<K1, K2>, V>> entrySet();

  /**
   * A implementation of this map should be wrapped around a class implements Map This method should
   * just return that map object for convenience uses. If this interface is implemented without
   * wrapping a Map class (which could happen), this methods should just return null
   * 
   * @return the internal map object wrapped internally, or null if this interface is not
   *         implemented around a map class.
   */
  public Map<? extends DualKey<K1, K2>, V> getMap();

  /**
   * A dual-key that combined two key object into a distinct key, which should be used as the key of
   * the internal map object.
   * 
   * @param <T1>
   *          is the type of the first key
   * @param <T2>
   *          is the type of the second key
   */
  interface DualKey<T1, T2> extends Comparable<Object> {

    /**
     * @return the first key
     */
    T1 getKey1();

    /**
     * @return the second key
     */
    T2 getKey2();

    /**
     * @return the hash code of this object
     */
    int hashCode();

    /**
     * Compare the key pair with another key pair.
     * 
     * @param other
     *          is the key pair to be compared with this key pair
     * @return true if the other object is considered as the same as this one, or false other wise.
     */
    boolean equals(Object other);
  }
}