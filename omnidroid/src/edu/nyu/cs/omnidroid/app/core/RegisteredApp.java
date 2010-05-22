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
package edu.nyu.cs.omnidroid.app.core;

import java.util.HashMap;

// TODO (acase): Replace the HashMap used in AGParser with this

/**
 * <code>RegisteredApp</code>s are applications that OmniDroid can interact with. These
 * appplications are stored using the AGParser into a backend configuration file. This is an
 * accessor method that provides an interface into each application's configuration.
 * 
 */
public class RegisteredApp {
  public static final String KEY_APPLICATION = "Application";
  public static final String KEY_EventName = "EventName";
  public static final String KEY_Filters = "Filters";
  public static final String KEY_ActionName = "ActionName";
  public static final String KEY_URIFields = "URIFields";
  public static final String KEY_ContentMap = "ContentMap";

  // One per RegisteredApp
  private String appName;

  // Multiple per RegisteredApp
  private HashMap<String, String> events;
  private HashMap<String, String> actions;
  private HashMap<String, String> contentMaps;
  private HashMap<String, String> filters;
  private HashMap<String, String> uris;

  public RegisteredApp() {
    // TODO: Initialize
  }

  public RegisteredApp(RegisteredApp oh) {
    // TODO: Copy Constructor
  }

  public void setRegisteredApp(String m_isEnabled, String m_instanceName, String m_eventApp,
      String m_eventName, HashMap<String, String> m_filterType,
      HashMap<String, String> m_filterData, HashMap<String, String> m_actionApp,
      HashMap<String, String> m_actionName, HashMap<String, String> m_actionData) {
    // TODO: Initialize
  }

  /**
   * @param appName
   *          the appName to set
   */
  public void setAppName(String appName) {
    this.appName = appName;
  }

  /**
   * @return the appName
   */
  public String getAppName() {
    return appName;
  }

  /**
   * @param events
   *          the events to set
   */
  public void setEvents(HashMap<String, String> events) {
    this.events = events;
  }

  /**
   * @return the events
   */
  public HashMap<String, String> getEvents() {
    return events;
  }

  /**
   * @param actions
   *          the actions to set
   */
  public void setActions(HashMap<String, String> actions) {
    this.actions = actions;
  }

  /**
   * @return the actions
   */
  public HashMap<String, String> getActions() {
    return actions;
  }

  /**
   * @param contentMaps
   *          the contentMaps to set
   */
  public void setContentMaps(HashMap<String, String> contentMaps) {
    this.contentMaps = contentMaps;
  }

  /**
   * @return the contentMaps
   */
  public HashMap<String, String> getContentMaps() {
    return contentMaps;
  }

  /**
   * @param filters
   *          the filters to set
   */
  public void setFilters(HashMap<String, String> filters) {
    this.filters = filters;
  }

  /**
   * @return the filters
   */
  public HashMap<String, String> getFilters() {
    return filters;
  }

  /**
   * @param uris
   *          the uris to set
   */
  public void setUris(HashMap<String, String> uris) {
    this.uris = uris;
  }

  /**
   * @return the uris
   */
  public HashMap<String, String> getUris() {
    return uris;
  }
}