/*******************************************************************************
 * Copyright 2010 OmniDroid - http://code.google.com/p/omnidroid
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

import java.util.HashMap;

import android.content.Intent;
import edu.nyu.cs.omnidroid.app.controller.Action;
import edu.nyu.cs.omnidroid.app.controller.external.actions.OmniActionService;
import edu.nyu.cs.omnidroid.app.controller.util.OmnidroidException;

/**
 * Simple action that turns wifi on
 *
 */
public class TurnOnWifiAction extends OmniAction {

  public static final String ACTION_NAME = "Turn On Wifi";

  public TurnOnWifiAction(HashMap<String, String> parameters) throws OmnidroidException {
    super(OmniActionService.class.getName(), Action.BY_SERVICE);
  }

  @Override
  public Intent getIntent() {
    Intent intent = new Intent();
    intent.setClassName(OMNIDROID_PACKAGE_NAME, OmniActionService.class.getName());
    intent.putExtra(OmniActionService.OPERATION_TYPE, OmniActionService.TURN_ON_WIFI_ACTION);
    intent.putExtra(NOTIFICATION, notificationIsOn);
    return intent;
  }
  
  @Override
  public String getDescription() {
	  return APP_NAME + "-" + ACTION_NAME;
  }

}