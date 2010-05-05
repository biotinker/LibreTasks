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

import java.util.HashMap;

import android.content.Intent;
import edu.nyu.cs.omnidroid.external.actions.OmniActionService;
import edu.nyu.cs.omnidroid.util.ExceptionMessageMap;
import edu.nyu.cs.omnidroid.util.OmnidroidException;

/**
 * Simple action that sets the brightness of the screen
 *
 */
public class SetScreenBrightnessAction extends OmniAction {
  //TODO(Roger):in the future store it in R to support internationalization.
  public static final String ACTION_NAME = "Change screen brightness";
  public static final String PARAM_BRIGHTNESS = "brightness";
  
  private Integer brightness;

  public SetScreenBrightnessAction(HashMap<String, String> parameters) throws OmnidroidException {
    super(OmniActionService.class.getName(), Action.BY_SERVICE);
    String brightnessString = parameters.get(PARAM_BRIGHTNESS);
    if (brightnessString == null) {
      //Action parameters not found error
      throw new OmnidroidException(120002, ExceptionMessageMap.getMessage(new Integer(120002)
      .toString()));
    }
    try {
      brightness = Integer.parseInt(brightnessString);
    } catch (NumberFormatException   e) {
      //Action parameters mal format.
      throw new OmnidroidException(120004, ExceptionMessageMap.getMessage(new Integer(120004)
      .toString()));
    }
    if (brightness == null) {
      //Action parameters not found error
      throw new OmnidroidException(120002, ExceptionMessageMap.getMessage(new Integer(120002)
      .toString()));
    }
  }

  @Override
  public Intent getIntent() {
    Intent intent = new Intent();
    intent.setClassName(OMNIDROID_PACKAGE_NAME, OmniActionService.class.getName());
    intent.putExtra(PARAM_BRIGHTNESS, brightness);
    intent.putExtra(OmniActionService.OPERATION_TYPE, OmniActionService.SET_SCREEN_BRIGHTNESS);
    return intent;
  }

}
