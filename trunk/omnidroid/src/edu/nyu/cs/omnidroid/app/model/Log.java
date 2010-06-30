/*******************************************************************************
 * Copyright 2009, 2010 OmniDroid - http://code.google.com/p/omnidroid
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
package edu.nyu.cs.omnidroid.app.model;

import android.content.Context;

/**
 * A abstract base class that represents a Log data structure.
 * 
 */
public abstract class Log {
  // Database storage accessor variables
  protected Context context;
  
  // Log data structures
  protected Long id = null;
  protected Long timestamp = null;
  protected String text;

  public Log() {
    // Do nothing
  }

  public Log(Log log) {
    this.id = log.id;
    this.timestamp = log.timestamp;
    this.text = log.text;
  }
  
  /**
   * @param id
   *          - A unique identifier to store with this log. Must be greater than 0.
   * @param timestamp
   *          - Time that this Log originated.
   * @param text
   *          - Text for the log.
   */
  public Log(Context context, long id, long timestamp, String text) {
    this.context = context;
    this.id = id;
    this.timestamp = timestamp;
    this.text = text;
  }

  public long getID() {
    return id;
  }
  
  public void setID(long id) {
    this.id = id;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
  
  /**
   * Store this Log in our database.
   */
  abstract public long insert();
}