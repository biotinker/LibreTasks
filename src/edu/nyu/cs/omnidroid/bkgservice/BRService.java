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
package edu.nyu.cs.omnidroid.bkgservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.util.OmLogger;
import edu.nyu.cs.omnidroid.util.UGParser;

// TODO (pradeep): Document this class
public class BRService extends Service {
  // Debug constant
  private static final String TAG = "BRService";

  // Broadcast variables
  IntentFilter Ifilter = new IntentFilter();
  BroadcastReceiver BR = new BCReceiver();

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Service#onBind(android.content.Intent)
   */
  @Override
  public IBinder onBind(Intent arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Service#onCreate()
   */
  @Override
  public void onCreate() {
    try {

      // Initializing the UGParser. To be deleted from this Code after Andrews Module
      UGParser ug = new UGParser(getApplicationContext());

      /* Check the User Config to start OmniDroid */
      // String Enabled = ug.readLine("Enabled");
      // if (Enabled.equalsIgnoreCase("True")) {
      Log.i(TAG, "Starting OmniDroid");

      // Get the User Instances in an Arraylist from the User Config
      ArrayList<HashMap<String, String>> UCRecords = ug.readRecords();
      Iterator<HashMap<String, String>> i = UCRecords.iterator();
      while (i.hasNext()) {
        HashMap<String, String> HM1 = i.next();
        // Configure the Intent Filter with the Events if Instance in enabled
        if (HM1.get("EnableInstance").equalsIgnoreCase("True")) {
          Ifilter.addAction(HM1.get("EventName"));
          Toast.makeText(getBaseContext(), "REGISTERING: " + HM1.get("EventName"),
              Toast.LENGTH_SHORT).show();

        }
      }
      registerReceiver(BR, Ifilter);
    } catch (Exception e) {
      Log.i("BRService", e.getLocalizedMessage());
      Log.i("BRService", e.toString());
      OmLogger.write(getApplicationContext(), "Not able to Enable/Diable Omnidroid");
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.content.ContextWrapper#stopService(android.content.Intent)
   */
  @Override
  public boolean stopService(Intent name) {
    Log.i(TAG, "Stopping OmniDroid");
    return super.stopService(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Service#onDestroy()
   */
  @Override
  public void onDestroy() {
    Log.i(TAG, "Destroying OmniDroid");
    super.onDestroy();

  }
}
