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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * <code>DualKeyHashMap</code> implements the <code>DualKeyMap</code> to provide a dual-key version
 * of <code>HashMap</code>.
 * </p>
 * 
 * <p>
 * It implements the <code>DualKeyMap.DualKey</code> interface to allow merging two keys as one, and
 * it maintain a <code>HashMap</code> instance internally by using instances of
 * <code>DualKeyMap.DualKey</code> as keys.
 * </p>
 * 
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * // To create a map
 * DualKeyHashMap&lt;String, Double, Integer&gt; map = new DualKeyHashMap&lt;String, Double, 
 * Integer&gt;();
 * 
 * // To put a mapping
 * map.put(&quot;Apple&quot;, 90.05, 100);
 * 
 * // To get a value associated with a key pair.
 * map.get(&quot;Apple&quot;,90.05)
 * </pre>
 * 
 * @param <K1>
 *          is the type of the first key
 * 
 * @param <K2>
 *          is the type of the second key
 * 
 * @param <V>
 *          is the type of the value
 * 
 * @see DualKeyMap
 */
public class DualKeyHashMap<K1, K2, V> implements DualKeyMap<K1, K2, V> {

  /* A HashMap instance is maintained internally. */
  private HashMap<DualKey<K1, K2>, V> hashMap;

  public DualKeyHashMap() {
    hashMap = new HashMap<DualKey<K1, K2>, V>();
  }

  public HashMap<DualKey<K1, K2>, V> getMap() {
    return hashMap;
  }

  public void clear() {
    hashMap.clear();
  }

  public boolean containsKey(K1 key1, K2 key2) {
    DualKey<K1, K2> dualKey = new DualKey<K1, K2>(key1, key2);
    return hashMap.containsKey(dualKey);
  }

  public boolean containsValue(V value) {
    return hashMap.containsValue(value);
  }

  @SuppressWarnings("unchecked")
  public Set entrySet() {
    return hashMap.entrySet();
  }

  public V get(K1 key1, K2 key2) {
    DualKey<K1, K2> dualKey = new DualKey<K1, K2>(key1, key2);
    return hashMap.get(dualKey);
  }

  public boolean isEmpty() {
    return hashMap.isEmpty();
  }

  public Set<DualKey<K1, K2>> keySet() {
    return hashMap.keySet();
  }

  public V put(K1 key1, K2 key2, V value) {
    DualKey<K1, K2> dualKey = new DualKey<K1, K2>(key1, key2);
    return hashMap.put(dualKey, value);
  }

  @SuppressWarnings("unchecked")
  public void putAll(DualKeyMap<K1, K2, V> m) {
    Map<DualKey<K1, K2>, V> map = (Map<DualKey<K1, K2>, V>) m.getMap();
    hashMap.putAll(map);
  }

  public V remove(K1 key1, K2 key2) {
    DualKey<K1, K2> dualKey = new DualKey<K1, K2>(key1, key2);
    return hashMap.remove(dualKey);
  }

  public int size() {
    return hashMap.size();
  }

  public Collection<V> values() {
    return hashMap.values();
  }

  class DualKey<T1, T2> implements DualKeyMap.DualKey<T1, T2> {

    /* Serialization ID */
    private static final long serialVersionUID = 1L;

    /* The hash code */
    private final int hashCode;

    /* Dual Keys */
    private final T1 key1;
    private final T2 key2;

    /* The main constructor that takes 2 keys */
    public DualKey(T1 key1, T2 key2) {
      super();
      if (key1 == null || key2 == null)
        throw new IllegalArgumentException("The keys must not be null");

      this.key1 = key1;
      this.key2 = key2;

      int total = 0;
      total ^= key1.hashCode();
      total ^= key2.hashCode();

      this.hashCode = total;
    }

    public T1 getKey1() {
      return key1;
    }

    public T2 getKey2() {
      return key2;
    }

    public int hashCode() {
      return hashCode;
    }

    /*
     * Two DualKey objects are equal only if their key1 and key2 are both equal
     */
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
      if (other == this)
        return true;
      if (other instanceof DualKey) {
        DualKey<T1, T2> otherDualKey = (DualKey<T1, T2>) other;
        return (otherDualKey.getKey1().equals(this.key1) && otherDualKey.getKey2()
            .equals(this.key2));
      }
      return false;
    }

    /* HashMap do no comparison, so just return 0 */
    public int compareTo(Object o) {
      return 0;
    }
  }

}