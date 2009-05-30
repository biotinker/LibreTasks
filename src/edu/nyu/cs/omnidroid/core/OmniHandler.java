package edu.nyu.cs.omnidroid.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <code>OmniHandler</code>s provide the mapping and configuration that OmniDroid uses to connect
 * <code>EventCatcher</code>s to <code>ActionThrower</code>s.
 * 
 * @author acase
 */
public class OmniHandler {
  // Strings that are stored in Config Files
  public static final String KEY_EVENT_APP = "EventApp";
  public static final String KEY_EVENT_TYPE = "EventType";
  public static final String KEY_FILTER_TYPE = "FilterType";
  public static final String KEY_FILTER_DATA = "FilterData";
  public static final String KEY_ACTION_APP = "ActionApp";
  public static final String KEY_ACTION_TYPE = "ActionType";
  public static final String KEY_ACTION_DATA1 = "ActionData1";
  public static final String KEY_ACTION_DATA2 = "ActionData2";

  // One per OmniHandler
  private boolean isEnabled = true;
  private String omniHandlerName = "";
  private String eventApp = "";
  private String eventType = "";

  // Multiple per OmniHandler
  private List<String> filterType;
  private List<String> filterData;
  private List<String> actionApp;
  private List<String> actionType;
  private List<String> actionData;

  public OmniHandler() {
    filterType = new ArrayList<String>();
    filterData = new ArrayList<String>();
    actionApp = new ArrayList<String>();
    actionType = new ArrayList<String>();
    actionData = new ArrayList<String>();
  }

  public OmniHandler(OmniHandler oh) {
    isEnabled = oh.isEnabled;
    omniHandlerName = oh.omniHandlerName;
    eventApp = oh.eventApp;
    eventType = oh.eventType;
    filterType = oh.filterType;
    filterData = oh.filterData;
    actionApp = oh.actionApp;
    actionType = oh.actionType;
    actionData = oh.actionData;
  }

  public OmniHandler(HashMap<String, String> hm) {
    // TODO (acase): Replace the HashMap used in UGParser with this
  }

  /**
   * @param name
   *          set the name for this omniHandler
   */
  public void setName(String name) {
    omniHandlerName = name;
  }

  /**
   * @return the name for this omnihandler
   */
  public String getName() {
    return omniHandlerName;
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
  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  /**
   * @return the eventName
   */
  public String getEventType() {
    return eventType;
  }

  /**
   * @param filterType
   *          the filterType to set
   */
  public void setFilterType(int location, String filterType) {
    this.filterType.set(location, filterType);
  }

  /**
   * @return the filterType
   */
  public String getFilterType(int location) {
    return filterType.get(location);
  }

  /**
   * @param filterData
   *          the filterData to set
   */
  public void setFilterData(int location, String filterData) {
    this.filterData.set(location, filterData);
  }

  /**
   * @return the filterData
   */
  public String getFilterData(int location) {
    return filterData.get(location);
  }

  /**
   * @param actionApp
   *          the actionApp to set
   */
  public void setActionApp(int location, String actionApp) {
    this.actionApp.set(location, actionApp);
  }

  /**
   * @return the actionApp
   */
  public String getActionApp(int location) {
    return actionApp.get(location);
  }

  /**
   * @param actionName
   *          the actionName to set
   */
  public void setActionType(int location, String actionType) {
    this.actionType.set(location, actionType);
  }

  /**
   * @return the actionName
   */
  public String getActionType(int location) {
    return actionType.get(location);
  }

  /**
   * @param actionData
   *          the actionData to set
   */
  public void setActionData(int location, String actionData) {
    this.actionData.set(location, actionData);
  }

  /**
   * @return the actionData
   */
  public String getActionData(int location) {
    return actionData.get(location);
  }
}