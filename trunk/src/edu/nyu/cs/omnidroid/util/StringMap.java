/**
 * 
 */
package edu.nyu.cs.omnidroid.util;

/**
 * @author acase
 *
 */
public class StringMap {
  private String[] strings = {"",""};
  
  public StringMap() {
  }

  public StringMap(String s1, String s2) {
    strings[0] = s1;
    strings[1] = s2;
  }

  public String get(int i) {
    return strings[i];
  }
  /**
   * @param actualEvent
   * @param displayEvent
   */
  public void set(String s1, String s2) {
    strings[0] = s1;
    strings[1] = s2;
  }
  public String toString() {
    return strings[1];
  }
  public String getKey() {
    return strings[0];
  }
}
