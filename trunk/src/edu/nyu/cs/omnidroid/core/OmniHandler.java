package edu.nyu.cs.omnidroid.core;

import java.util.HashMap;

// TODO (acase): Replace the HashMap used in UGParser with this

/**
 * <code>OmniHandler</code>s provide the mapping and configuration that OmniDroid uses to connect
 * <code>EventCatcher</code>s to <code>ActionThrower</code>s.
 * 
 * @author acase
 */
public class OmniHandler {
  public static final String KEY_FilterType = "FilterType";
  public static final String KEY_FilterData = "FilterData";
  public static final String KEY_ActionApp = "ActionApp";
  public static final String KEY_ActionName = "ActionName";
  public static final String KEY_ActionData = "ActionData";

  // One per OmniHandler
  private boolean isEnabled = true;
  private String instanceName = "";
  private String eventApp = "";
  private String eventName = "";

  // Multiple per OmniHandler
  private HashMap<String, String> filterType;
  private HashMap<String, String> filterData;
  private HashMap<String, String> actionApp;
  private HashMap<String, String> actionName;
  private HashMap<String, String> actionData;

  public OmniHandler() {
    // TODO: Initialize
  }

  public OmniHandler(OmniHandler oh) {
    // TODO: Copy Constructor
  }

  public void setOmniHandler(String m_isEnabled, String m_instanceName, String m_eventApp,
      String m_eventName, HashMap<String, String> m_filterType,
      HashMap<String, String> m_filterData, HashMap<String, String> m_actionApp,
      HashMap<String, String> m_actionName, HashMap<String, String> m_actionData) {
    // TODO: Initialize
  }

  public void setInstance(String s) {
    instanceName = s;
  }

  public String getInstance() {
    return instanceName;
  }

  /**
   * @param isEnabled
   *          the isEnabled to set
   */
  public void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
  }

  /**
   * @return the isEnabled
   */
  public boolean isEnabled() {
    return isEnabled;
  }

  /**
   * @param eventApp
   *          the eventApp to set
   */
  public void setEventApp(String eventApp) {
    this.eventApp = eventApp;
  }

  /**
   * @return the eventApp
   */
  public String getEventApp() {
    return eventApp;
  }

  /**
   * @param eventName
   *          the eventName to set
   */
  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  /**
   * @return the eventName
   */
  public String getEventName() {
    return eventName;
  }

  /**
   * @param filterType
   *          the filterType to set
   */
  public void setFilterType(HashMap<String, String> filterType) {
    this.filterType = filterType;
  }

  /**
   * @return the filterType
   */
  public HashMap<String, String> getFilterType() {
    return filterType;
  }

  /**
   * @param filterData
   *          the filterData to set
   */
  public void setFilterData(HashMap<String, String> filterData) {
    this.filterData = filterData;
  }

  /**
   * @return the filterData
   */
  public HashMap<String, String> getFilterData() {
    return filterData;
  }

  /**
   * @param actionApp
   *          the actionApp to set
   */
  public void setActionApp(HashMap<String, String> actionApp) {
    this.actionApp = actionApp;
  }

  /**
   * @return the actionApp
   */
  public HashMap<String, String> getActionApp() {
    return actionApp;
  }

  /**
   * @param actionName
   *          the actionName to set
   */
  public void setActionName(HashMap<String, String> actionName) {
    this.actionName = actionName;
  }

  /**
   * @return the actionName
   */
  public HashMap<String, String> getActionName() {
    return actionName;
  }

  /**
   * @param actionData
   *          the actionData to set
   */
  public void setActionData(HashMap<String, String> actionData) {
    this.actionData = actionData;
  }

  /**
   * @return the actionData
   */
  public HashMap<String, String> getActionData() {
    return actionData;
  }

}