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
import libretasks.app.controller.util.ExceptionMessageMap;
import libretasks.app.controller.util.OmnidroidException;

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

  private String phoneNumber;
  private String sms;

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
    phoneNumber = parameters.get(PARAM_PHONE_NO);
    sms = parameters.get(PARAM_SMS);
    if (phoneNumber == null || sms == null) {
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
    intent.putExtra(PARAM_PHONE_NO, phoneNumber);
    intent.putExtra(PARAM_SMS, sms);
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
