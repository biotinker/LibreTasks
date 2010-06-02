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
package edu.nyu.cs.omnidroid.app.controller.bkgservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import edu.nyu.cs.omnidroid.app.controller.external.attributes.EventMonitoringService;

/**
 * This broadcast receiver detect intents including System Boot, OmniStart and OmniRestart to
 * complete necessary operations
 */
public class Starter extends BroadcastReceiver {

  public void onReceive(Context context, Intent intent) {
    
    if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
      // Start the background monitoring service.
      EventMonitoringService.startService(context);
    } else if ("OmniStart".equals(intent.getAction())) {
      // Do something when receiving 'OmniStart'

    } else if ("OmniRestart".equals(intent.getAction())) {
      // Do something when receiving 'OmniRestart'
      
    }
  }
}
