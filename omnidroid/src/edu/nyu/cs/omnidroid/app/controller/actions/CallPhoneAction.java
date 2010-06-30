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

import java.util.HashMap;

import edu.nyu.cs.omnidroid.app.controller.Action;
import edu.nyu.cs.omnidroid.app.controller.util.ExceptionMessageMap;
import edu.nyu.cs.omnidroid.app.controller.util.OmnidroidException;

import android.content.Intent;
import android.net.Uri;

/**
 * CallPhoneAction can return an Intent that can be fired to perform a phone call.
 **/
public class CallPhoneAction extends Action {

  /** Parameter names */
  //TODO(londinop): Decide on the best location for all app name and action name constants for consistency
  public static final String ACTION_NAME = "Dial Number";
  public static final String APP_NAME = "Phone";
  public static final String PARAM_PHONE_NO = "Phone Number";

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
    super(Intent.ACTION_CALL, Action.BY_ACTIVITY);
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
    // TODO(Rutvij): Fetch "tel:" from constants of another class (if any).
    Intent intent = new Intent(getActionName(), Uri.parse("tel:" + phoneNumber));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
