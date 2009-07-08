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
import android.net.Uri;

/**
 * CallPhoneAction can return an Intent that can be fired to perform a phone call.
 **/
public class CallPhoneAction extends Action {

  /** Parameter names */
  public static final String PARAM_PHONE_NO = "PhoneNumber";

  /** The phone number to call */
  private String phoneNumber = null;

  /**
   * Constructs a phone call action with required parameters which can provide an intent that will
   * call a phone number when fired.
   * 
   * @param parameters
   *          A list of parameters required to perform a call phone action. <br>
   *          List of required parameters: <br>
   *          <ol>
   *          <li>Phone Number:
   *          <ul>
   *          <li>Key - CallPhoneAction.PARAM_PHONE_NO
   *          <li>Value - A valid phone number String.
   *          </ul>
   *          </ol>
   */
  public CallPhoneAction(HashMap<String, String> parameters) {
    super(Intent.ACTION_CALL, Action.BY_ACTIVITY);
    phoneNumber = parameters.get(PARAM_PHONE_NO);
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
    return intent;
  }

}
