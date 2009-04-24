/**
 * 
 */
package edu.nyu.cs.omnidroid.util;

/**
 * @author acase
 *
 */
public class StringMap {
  public static final int KEY = 0;
  public static final int VALUE = 1;

  private String[] strings = {"",""};
  
  public StringMap() {
  }

  public StringMap(String s1, String s2) {
    strings[KEY] = s1;
    strings[VALUE] = s2;
  }

  public String get(int i) {
    return strings[i];
  }
  /**
   * @param actualEvent
   * @param displayEvent
   */
  public void set(String s1, String s2) {
    strings[KEY] = s1;
    strings[VALUE] = s2;
  }
  public String toString() {
    return strings[1];
  }
  public String getKey() {
    return strings[KEY];
  }
  public String getValue() {
    return strings[VALUE];
  }
}
