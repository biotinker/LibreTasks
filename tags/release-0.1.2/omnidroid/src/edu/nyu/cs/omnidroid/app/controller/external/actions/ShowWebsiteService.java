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

import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.ResultProcessor;
import edu.nyu.cs.omnidroid.app.controller.actions.ShowWebsiteAction;
import edu.nyu.cs.omnidroid.app.controller.external.attributes.NetworkStateMonitor;
import edu.nyu.cs.omnidroid.app.controller.util.Logger;

/**
 * Display a website for a given url
 */
public class ShowWebsiteService extends Service {
  
  private static final String TAG = ShowWebsiteService.class.getSimpleName();
  
  @Override 
  public void onStart(Intent intent, int startId) {
    super.onStart(intent, startId);
    String url = null;
    try {
      url = intent.getStringExtra(ShowWebsiteAction.PARAM_WEB_URL);
    } catch (Exception e) {
      url = "";
      Logger.w(TAG, "no web url provided by user");
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_IRRECOVERABLE, 
          getString(R.string.website_action_failed_no_url));
    }
    Uri uri = Uri.parse(url);
    Intent newIntent = new Intent(Intent.ACTION_VIEW, uri);
    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    try {
      if (NetworkStateMonitor.isConnected()) {
        startActivity(newIntent);
      } else {
        ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_INTERNET,
            getString(R.string.website_action_failed_no_service));
      }
    } catch (ActivityNotFoundException e) {
      Logger.w(TAG, e.getClass().getSimpleName(), e );
      ResultProcessor.process(this, intent, ResultProcessor.RESULT_FAILURE_IRRECOVERABLE,
          getString(R.string.website_action_failed_bad_url));
      return;
    }
    ResultProcessor.process(this, intent, ResultProcessor.RESULT_SUCCESS, null);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  } 
}
