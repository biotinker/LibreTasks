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

/**
 * SendSmsAction class creates SMS intent that is used to send SMS
 *
 */
public class SendSmsAction extends Action {
	
	/**
	 * attributes field names
	 */
    public static final String PARAM_PHONE_NO = "PhoneNumber";
    public static final String PARAM_SMS = "Sms";
    public static final String SMS_INTENT = "omnidroid.intent.action.SMS_SEND";
   
    private String phoneNumber = null;
    private String sms = null;
    
	/**
	 * Creates SendSmsAction with two parameters that is used by SMS intent to send SMS
	 * @param parameter
     *          A list of parameters require to send SMS <br>
     *          List of required parameters: <br>
     *          <ol>
     *          <li>Phone Number:
     *          <ul>
     *          <li>Key - SendSmsAction.ATTR_PHONE_NO
     *          <li>Value - A valid phone number String.
     *          </ul>
     *          <li> SMS:
     *          <ul>
     *          <li>key - SendSmsAction.ATTR_SMS
     *          <li>value - Text Message
     *          </ul>
     *          </ol>  
	 */
	public SendSmsAction(HashMap<String, String> parameters) {
		super(SendSmsAction.SMS_INTENT, Action.BY_SERVICE);
		phoneNumber = parameters.get(PARAM_PHONE_NO);
		sms = parameters.get(PARAM_SMS);
	}

	/**
	 * @return an intent that is use to send SMS
	 * 
	 */
	@Override
	public Intent getIntent() {
		Intent intent = new Intent(getActionName());
		intent.putExtra(edu.nyu.cs.omnidroid.core.SendSmsAction.PARAM_PHONE_NO, phoneNumber);
		intent.putExtra(edu.nyu.cs.omnidroid.core.SendSmsAction.PARAM_SMS, sms);
		return intent;
	
	}

}
