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
package core;

import java.util.HashMap;

import android.content.Intent;
import edu.nyu.cs.omnidroid.util.ExceptionMessageMap;
import edu.nyu.cs.omnidroid.util.OmnidroidException;

/**
 * SendSmsAction class creates SMS intent that is used to send SMS
 * 
 */
public class SendSmsAction extends Action {

  /**
   * attributes field names
   */
  public static final String ACTION_NAME = "SMS Send";
  public static final String APP_NAME = "SMS";
  public static final String PARAM_PHONE_NO = "Phone Number";
  public static final String PARAM_SMS = "Text Message";
  public static final String SMS_INTENT = "omnidroid.intent.action.SMS_SEND";
  private static final String PARAMS[] = {PARAM_PHONE_NO, PARAM_SMS};
  private static final int PARAM_PHONE_NO_INDEX = 0;
  private static final int PARAM_SMS_INDEX = 1;

  private String VALUES[];

  /**
   * Creates SendSmsAction with two parameters that is used by SMS intent to send SMS
   * 
   * @param parameter
   *          Hashmap parameters required to send SMS <br>
   *          <ol>
   *          <li>Phone Number:
   *          <ul>
   *          <li>Key - SendSmsAction.PARAM_PHONE_NO
   *          <li>Value - A valid phone number String.
   *          </ul>
   *          <li>SMS:
   *          <ul>
   *          <li>key - SendSmsAction.PARAM_SMS
   *          <li>value - Text Message
   *          </ul>
   *          </ol>
   */
  public SendSmsAction(HashMap<String, String> parameters) throws OmnidroidException {
    super(SendSmsAction.SMS_INTENT, Action.BY_SERVICE);
    VALUES = new String[PARAMS.length];
    VALUES[PARAM_PHONE_NO_INDEX] = parameters.get(PARAM_PHONE_NO);
    VALUES[PARAM_SMS_INDEX] = parameters.get(PARAM_SMS);
    if (VALUES[PARAM_PHONE_NO_INDEX] == null || VALUES[PARAM_SMS_INDEX] == null) {
      throw new OmnidroidException(120002, ExceptionMessageMap.getMessage(new Integer(120002)
          .toString()));
    }
  }

  /**
   * @return an intent that is use to send SMS
   * 
   */
  @Override
  public Intent getIntent() {
    Intent intent = new Intent(getActionName());
    intent.putExtra(PARAM_PHONE_NO, VALUES[PARAM_PHONE_NO_INDEX]);
    intent.putExtra(PARAM_SMS, VALUES[PARAM_SMS_INDEX]);
    return intent;

  }

  @Override
  public int getNumberOfParameters() {
    return PARAMS.length;
  }

  @Override
  public String getParameter(int index) throws IndexOutOfBoundsException {
    return VALUES[index];
  }

  @Override
  public void setParameter(int index, String value) throws IndexOutOfBoundsException {
    VALUES[index] = value; 
  }

}
