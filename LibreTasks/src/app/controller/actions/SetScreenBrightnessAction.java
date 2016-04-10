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
 * Copyright 2009, 2010 Omnidroid - http://code.google.com/p/omnidroid
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
package libretasks.app.controller.actions;

import java.util.HashMap;

import android.content.Intent;
import libretasks.app.controller.Action;
import libretasks.app.controller.external.actions.OmniActionService;
import libretasks.app.controller.util.ExceptionMessageMap;
import libretasks.app.controller.util.OmnidroidException;

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
    intent.setClassName(LIBRETASKS_PACKAGE_NAME, OmniActionService.class.getName());
    intent.putExtra(PARAM_BRIGHTNESS, brightness);
    intent.putExtra(OmniActionService.OPERATION_TYPE, OmniActionService.SET_SCREEN_BRIGHTNESS);
    intent.putExtra(DATABASE_ID, databaseId);
    intent.putExtra(ACTION_TYPE, actionType);
    return intent;
  }

  @Override
  public String getDescription() {
    return APP_NAME + "-" + ACTION_NAME;
  }

}
