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
 * Copyright 2010 Omnidroid - http://code.google.com/p/omnidroid
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

import libretasks.app.controller.Event;

/**
 * This class represents an Event{@code Log}. Logs are displayed on the {@code ActivityLogs} for
 * users to see what is going on.
 */
public class EventLog extends Log {
  public static final String TAG = EventLog.class.getSimpleName();

  // Extended Log Constructs
  String appName;
  String eventName;
  String parameters;

  /**
   * @param context
   *          application context for the db connection
   * @param event
   *          to create an {@code Event} out of
   * 
   */
  public EventLog(Event event) {
    super();
    this.appName = event.getAppName();
    this.eventName = event.getEventName();
    this.parameters = event.getParameters();
    this.text = event.getEventName() + event.getParameters();
  }

  /**
   * Copy constructor
   * 
   * @param log
   *          EventLog to duplicate
   * 
   */
  public EventLog(EventLog log) {
    super(log);
    this.id = log.id;
    this.timestamp = log.timestamp;
    this.appName = log.appName;
    this.eventName = log.eventName;
    this.parameters = log.parameters;
  }

  /**
   * Create a Log item that stores relevant event log data.
   * 
   * @param id
   *          the database id for this log entry
   * @param timeStamp
   *          the time stamp of the action taken.
   * @param appName
   *          the name of the application for the event
   * @param actionName
   *          the name of the event for the event
   * @param parameters
   *          the parameters for the aevent
   * @param text
   *          a textual description of the Log
   */
  public EventLog(long id, long timestamp, String appName, String eventName,
      String parameters, String text) {
    super(id, timestamp, text);
    this.appName = appName;
    this.eventName = eventName;
    this.parameters = parameters;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getAppName() {
    return appName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getEventName() {
    return eventName;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  public String getParameters() {
    return parameters;
  }

  public String toString() {
    return "ID: " + id + "\n" + "Timestamp: " + timestamp + "\n" + "Application Name: " + appName
        + "\n" + "Event Name: " + eventName + "\nParameters: " + parameters + "\nText: " + text;
  }
}
