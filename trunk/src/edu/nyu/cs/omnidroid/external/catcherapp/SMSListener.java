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
 * 
 */
package edu.nyu.cs.omnidroid.external.catcherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * This receiver catches the intent thrown by the OmniDroid Application.
 * 
 * 
 */

public class SMSListener extends BroadcastReceiver {
  Context context;

  @Override
  public void onReceive(Context context, Intent intent) {
    this.context = context;
    Toast.makeText(context, intent.getAction(), 5).show();
    if (intent.getAction().equalsIgnoreCase("Display_Contact")) {
      // Bundle bundle = intent.getExtras();
      try {
        intent.setClass(context, edu.nyu.cs.omnidroid.external.catcherapp.CatcherAppActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Log.i("Received Intent", intent.getAction());
      } catch (Exception e) {
        Log.i("Exception in Intent", e.getLocalizedMessage());

      }

    } else {
      Log.i("Intent Not Received", "Fail");
    }
  }
}
