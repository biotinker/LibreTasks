/*******************************************************************************
 * Copyright 2010 Omnidroid - http://code.google.com/p/omnidroid 
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
package edu.nyu.cs.omnidroid.app.controller.external.actions;

import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.ResultProcessor;
import edu.nyu.cs.omnidroid.app.controller.actions.CallPhoneAction;
import edu.nyu.cs.omnidroid.app.controller.external.attributes.PhoneStateMonitor;
import edu.nyu.cs.omnidroid.app.controller.util.Logger;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
/**
 * once started, this service gets phoneNumber from intent, makes phone call and 
 * processes its result 
 */
public class PhoneCallService extends Service {
  
  private static final String TAG = PhoneCallService.class.getSimpleName(); 
  @Override
  public void onStart(Intent intent, int startId) {
    super.onStart(intent, startId);    
    String phoneNumber = intent.getStringExtra(CallPhoneAction.PARAM_PHONE_NO);
    Intent newIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
    if (PhoneStateMonitor.isServiceAvailable()) {
      try {
        startActivity(newIntent);
      } catch (Exception e) {
        Logger.e(TAG, e.getMessage(), e);
      }
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_SUCCESS,
          getString(R.string.phone_call));
    } else {
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_SERVICE,
          getString(R.string.phone_call_failed));
    }    
  }
  @Override
  public IBinder onBind(Intent arg0) {
    return null;
  }
}