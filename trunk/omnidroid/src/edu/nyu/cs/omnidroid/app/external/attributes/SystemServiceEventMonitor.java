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
package edu.nyu.cs.omnidroid.app.external.attributes;

public interface SystemServiceEventMonitor {
  
  /** System service name to be passed to getSystemService() */
  public String getSystemServiceName();
  
  /** Actions performed to initialize EventMonitor service. */
  public void init();

  /** Actions performed to finalize EventMonitor service. */
  public void stop();

  /** EventMonitor service name */
  public String getMonitorName();
}
