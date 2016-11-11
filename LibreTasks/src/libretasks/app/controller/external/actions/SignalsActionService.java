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
 * Copyright 2009 Omnidroid - http://code.google.com/p/omnidroid 
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
package libretasks.app.controller.external.actions;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;
import libretasks.app.R;
import libretasks.app.controller.ResultProcessor;
import libretasks.app.controller.actions.TurnOffWifiAction;
import libretasks.app.controller.actions.TurnOnWifiAction;
import libretasks.app.controller.actions.TurnOffBluetoothAction;
import libretasks.app.controller.actions.TurnOnBluetoothAction;
import libretasks.app.view.simple.UtilUI;

/**
 * This service is for Omnidroid to launch simple actions like: show a message, put some message on
 * notification bar,etc. More specifically providing execution for actions that is inappropriate 
 * for an activity to be created.
 */
public class SignalsActionService extends Service {
  
  //operation supported by this service
  public static final String OPERATION_TYPE = "OPERATION_TYPE";
  public static final int NO_ACTION = -1;
  public static final int TURN_ON_BLUETOOTH_ACTION = 1;
  public static final int TURN_OFF_BLUETOOTH_ACTION = 2;
  public static final int TURN_OFF_WIFI_ACTION = 3;
  public static final int TURN_ON_WIFI_ACTION = 4;
  public static final int POWER_OFF_DEVICE = 5 ;
  
  private Intent intent;

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
  
  @Override
  public void onStart(Intent intent, int startId) {
    super.onStart(intent, startId);
    this.intent = intent;
    int operationType = intent.getIntExtra(OPERATION_TYPE, NO_ACTION);
    switch (operationType) {
    case TURN_ON_BLUETOOTH_ACTION :
      turnOnBluetooth();
      break;
    case TURN_OFF_BLUETOOTH_ACTION :
      turnOffBluetooth();
      break;
    case TURN_OFF_WIFI_ACTION :
      turnOffWifi();
      break;
    case TURN_ON_WIFI_ACTION :
      turnOnWifi();
      break;
    case POWER_OFF_DEVICE :
        powerOff();
        break;
    default:
      Log.e("LibreTasks: Signals Action Service", "No such operation supported as: " + operationType);
    }
  }
  
  /**
   * turn off the wifi.
   */
  private void turnOffWifi() {
    WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    wifiManager.setWifiEnabled(false);
    ResultProcessor.process(this, intent, ResultProcessor.RESULT_SUCCESS,
        getString(R.string.wifi_turned_off));
  }
  
  /**
   * turn on the wifi. 
   */
  private void turnOnWifi() {
    WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    wifiManager.setWifiEnabled(true);
    ResultProcessor.process(this, intent, ResultProcessor.RESULT_SUCCESS,
        getString(R.string.wifi_turned_on));
  }
  
  /**
   * turn off the bluetooth.
   */
  private void turnOffBluetooth() {
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();    
	if (mBluetoothAdapter.isEnabled()) {
		mBluetoothAdapter.disable(); 
	}
    ResultProcessor.process(this, intent, ResultProcessor.RESULT_SUCCESS,
        getString(R.string.bluetooth_turned_off));
  }
  
  /**
   * turn on the bluetooth. 
   */
  private void turnOnBluetooth() {
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();    
	if (!mBluetoothAdapter.isEnabled()) {
		mBluetoothAdapter.enable(); 
	}
    ResultProcessor.process(this, intent, ResultProcessor.RESULT_SUCCESS,
        getString(R.string.bluetooth_turned_on));
  }

  /**
   * power off the device.
   * NEED ROOT
   */
  private void powerOff() {
    try {
      Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot -p"});
      proc.waitFor();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
