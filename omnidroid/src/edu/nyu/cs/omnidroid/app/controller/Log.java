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
package edu.nyu.cs.omnidroid.app.controller;

/**
 * A base class to store Logs in.
 * 
 */
public class Log {
  private final String tag;
  private final long id;
  private final String text;

  public Log() {
    tag = null;
    id = -1;
    text = null;
  }

  /**
   * @param tag
   *          - Tag to determine where the log originated
   * @param text
   *          - Text for the log
   */
  public Log(String tag, String text) {
    this.tag = tag;
    this.id = -1;
    this.text = text;
  }

  /**
   * @param tag
   *          - Tag to determine where the log originated
   * @param id
   *          - A unique identifier to store with this log. Must be greater than 0.
   * @param text
   *          - Text for the log
   */
  public Log(String tag, long id, String text) {
    this.tag = tag;
    if (id > 0) {
      this.id = id;
    } else {
      this.id = -1;
    }
    this.text = text;
  }

  public String getTag() {
    return tag;
  }

  public long getID() {
    return id;
  }

  public String getText() {
    return text;
  }
}