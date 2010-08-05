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
package edu.nyu.cs.omnidroid.app.controller.external.attributes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import edu.nyu.cs.omnidroid.app.controller.events.InternetAvailableEvent;
import edu.nyu.cs.omnidroid.app.controller.util.Logger;

/**
 * monitors Network state and broadcasts InternetAvailableEvent 
 * which is received by BCReceiver
 */
public class NetworkStateMonitor extends BroadcastReceiver implements SystemServiceEventMonitor {
  
  private static final String SYSTEM_SERVICE_NAME = "NETWORK_SERVICE";
  private static final String MONITOR_NAME = NetworkStateMonitor.class.getSimpleName();
  
  private final static  String TAG = NetworkStateMonitor.class.getSimpleName();
  
  private static volatile boolean wifiConnected = false;
  private static volatile boolean dataConnected = false;
  
  private Context context;  
  
  @Override
  public void onReceive(Context context, Intent intent) {
    
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    
    if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {      
      wifiConnected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
          .isConnected();
      if (!dataConnected && wifiConnected) {
        Intent newIntent = new Intent(InternetAvailableEvent.ACTION_NAME);
        context.sendBroadcast(newIntent);
        Logger.i(TAG, "wifi connected");
      }
    }

  }
  public NetworkStateMonitor(Context context) {
    this.context = context;
  }

  public String getMonitorName() {
    return MONITOR_NAME;
  }

  public String getSystemServiceName() {
    return SYSTEM_SERVICE_NAME;
  }
  
  public void init() {
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    tm.listen(phoneStateListener, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
    IntentFilter intentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    context.registerReceiver(this, intentFilter);
  }
  
  private final PhoneStateListener phoneStateListener = new PhoneStateListener() {
    @Override
    public void onDataConnectionStateChanged(int state){
      if (state == TelephonyManager.DATA_CONNECTED) {
        if (!wifiConnected) {
          Intent intent = new Intent(InternetAvailableEvent.ACTION_NAME);
          context.sendBroadcast(intent);
        }
        // If phone was using wifi then it's not a new InternetAvailable event
        dataConnected = true;
      } else {
        dataConnected = false;
      }
    }
  };

  public void stop() {
    context.unregisterReceiver(this);
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    tm.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
  }
  
  public static boolean isConnected() {
    return dataConnected || wifiConnected; 
  }
}