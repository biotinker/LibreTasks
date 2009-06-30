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
/**
 * Catches the SMS_SEND intent broadcasted by OmniDroid.
 * Starts the SMSCatcherActivity. 
 */
package edu.nyu.cs.omnidroid.external.catcherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Registered with Android to catch "SMS_SEND" intents, this class launches SMSCatcherActivity to
 * send an SMS message as a user-programmed action.
 */
public class SMSCatcher extends BroadcastReceiver {
  private static final String SMS_SEND = "SMS_SEND";

  private static final String TAG = SMSCatcher.class.getCanonicalName();
  
  // Visible for testing.
  Toast actionToast;
  Toast smsToast;
  

  @Override
  public void onReceive(Context context, Intent intent) {
    actionToast = Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG);
    actionToast.show();
    if (intent.getAction().contains(SMS_SEND)) {
      smsToast = Toast.makeText(context, "Caught!", Toast.LENGTH_LONG);
      smsToast.show();
      try {
        intent.setClass(context, SMSCatcherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Log.i(TAG, "Received and acted on intent: " + intent.getAction());
      } catch (Exception e) {
        Log.w(TAG, e);
      }
    } else {
      Log.i(TAG, "Intent Not " + SMS_SEND + ", taking no action.");
    }
  }
}
