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

import libretasks.app.controller.Action;
import libretasks.app.controller.util.ExceptionMessageMap;
import libretasks.app.controller.util.OmnidroidException;
import android.content.Intent;

/**
 * CallPhoneAction can return an Intent that can be fired to perform a phone call.
 **/
public class CallPhoneAction extends Action {

  /** Parameter names */
  //TODO(londinop): Decide on the best location for all app name and action name constants for consistency
  public static final String ACTION_NAME = "Dial Number";
  public static final String APP_NAME = "Phone";
  public static final String PARAM_PHONE_NO = "Phone Number";
  public static final String PHONE_CALL_INTENT = "omnidroid.intent.action.PHONE_CALL";
  
  /** The phone number to call */
  private String phoneNumber = null;

  /**
   * Constructs a phone call action with required parameters which can provide an intent that will
   * call a phone number when fired.
   * 
   * @param parameters
   *          A map of parameters required to perform a call phone action. <br>
   *          Required parameters: <br>
   *          <ol>
   *          <li>Phone Number:
   *          <ul>
   *          <li>Key - CallPhoneAction.PARAM_PHONE_NO
   *          <li>Value - A valid phone number String.
   *          </ul>
   *          </ol>
   * @throws OmnidroidException
   *           if Action Parameter "Phone Number" is not found
   */
  public CallPhoneAction(HashMap<String, String> parameters) throws OmnidroidException {
    super(PHONE_CALL_INTENT, Action.BY_SERVICE);
    phoneNumber = parameters.get(PARAM_PHONE_NO);
    if (phoneNumber == null) {
      throw new OmnidroidException(120002, ExceptionMessageMap.getMessage(new Integer(120002)
          .toString()));
    }
  }

  /**
   * Returns an intent that can be fired to perform a phone call action.
   * 
   * @return An intent that calls a phone number when fired.
   */
  @Override
  public Intent getIntent() {
    Intent intent = new Intent(getActionName());
    intent.putExtra(PARAM_PHONE_NO, phoneNumber);
    intent.putExtra(DATABASE_ID, databaseId);
    intent.putExtra(ACTION_TYPE, actionType);
    intent.putExtra(NOTIFICATION, showNotification);
    return intent;
  }

  @Override
  public String getDescription() {
    return APP_NAME + "-" + ACTION_NAME;
  }

  public String getAppName() {
    return APP_NAME;
  }
}
