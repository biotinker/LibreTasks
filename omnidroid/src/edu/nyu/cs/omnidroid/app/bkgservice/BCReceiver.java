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
package edu.nyu.cs.omnidroid.app.bkgservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.nyu.cs.omnidroid.app.util.OmLogger;

/**
 * The Broadcast receiver receives any intent that is broadcast either by the system or by any other
 * application. If it is a system broadcast, the intent checks whether the receiver has the
 * permission to receive the specific intent, in the applications Manifest.xml file.
 */
public class BCReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    try {
      intent.setClass(context, edu.nyu.cs.omnidroid.app.core.HandlerService.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startService(intent);
      Log.i("Received Intent", intent.getAction());
    } catch (Exception e) {
      Log.i("Exception in Intent", e.getLocalizedMessage());
      OmLogger.write(context, "Unable to execute required action");
    }
  }
}
