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
package edu.nyu.cs.omnidroid.app.controller.actions;

import edu.nyu.cs.omnidroid.app.controller.Action;

/**
 * This is to share common field for all Omnidroid supported Actions
 */
public abstract class OmniAction extends Action {
  
  public static final String APP_NAME = "Omnidroid";
  public static final String OMNIDROID_PACKAGE_NAME = "edu.nyu.cs.omnidroid.app";

  public OmniAction(String actionName, String executionMethod) {
    super(actionName, executionMethod);
  }

  @Override
  public String getAppName() {
    return APP_NAME;
  }
}
