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
package edu.nyu.cs.omnidroid.core;

/**
 * This class represents an action that will be fired by OmniDroid if an event passes a rule's
 * filters
 */
public class Action {
  // TODO(londinop): replace with Rutvij's Action class (with Javadocs)
  public final String actionName;
  public final String parameterName;
  public final String parameterData;

  public Action(String actionName, String parameterName, String parameterData) {
    this.actionName = actionName;
    this.parameterName = parameterName;
    this.parameterData = parameterData;
  }
}
