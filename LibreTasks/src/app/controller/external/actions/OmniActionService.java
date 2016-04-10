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
import libretasks.app.R;
import libretasks.app.controller.ResultProcessor;
import libretasks.app.controller.actions.SetScreenBrightnessAction;
import libretasks.app.controller.actions.ShowAlertAction;
import libretasks.app.controller.actions.ShowNotificationAction;
import libretasks.app.view.simple.UtilUI;

/**
 * This service is for Omnidroid to launch simple actions like: show a message, put some message on
 * notification bar,etc. More specifically providing execution for actions that is inappropriate 
 * for an activity to be created.
 */
public class OmniActionService extends Service {
  
  //operation supported by this service
  public static final String OPERATION_TYPE = "OPERATION_TYPE";
  public static final int NO_ACTION = -1;
  public static final int SHOW_ALERT_ACTION = 1;
  public static final int SHOW_NOTIFICATION_ACTION = 2;
  public static final int TURN_OFF_WIFI_ACTION = 3;
  public static final int TURN_ON_WIFI_ACTION = 4;
  public static final int SET_SCREEN_BRIGHTNESS = 5;
  public static final int SET_PHONE_LOUD = 6;
  public static final int SET_PHONE_SILENT = 7;
  public static final int SET_PHONE_VIBRATE = 8;
  
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
    case SHOW_ALERT_ACTION :
      showAlert(intent);
      break;
    case SHOW_NOTIFICATION_ACTION :
      showNotification(intent);
      break;
    case TURN_OFF_WIFI_ACTION :
      turnOffWifi();
      break;
    case TURN_ON_WIFI_ACTION :
      turnOnWifi();
      break;
    case SET_SCREEN_BRIGHTNESS :
      setScreenBrightness(intent);
      break;
    case SET_PHONE_LOUD :
      setPhoneLoud();
      break;
    case SET_PHONE_SILENT :
      setPhoneSilent();
      break;
    case SET_PHONE_VIBRATE :
      setPhoneVibrate();
      break;
    default:
      Log.e("OmniActionService", "No such operation supported as: " + operationType);
    }
  }
  
  /**
   * set the phone to loud
   */
  private void setPhoneLoud() {
    AudioManager audioManager =(AudioManager) getSystemService(Context.AUDIO_SERVICE);
    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    int ringstream = AudioManager.STREAM_RING;
    int ringmaxvolume = audioManager.getStreamMaxVolume(ringstream);
    audioManager.setStreamVolume(ringstream, ringmaxvolume, AudioManager.FLAG_SHOW_UI);
    ResultProcessor.process(this, intent, ResultProcessor.RESULT_SUCCESS,
        getString(R.string.phone_set_loud));
   }
  
  /**
   * set the phone to silent
   */
  private void setPhoneSilent() {
    AudioManager audioManager =(AudioManager) getSystemService(Context.AUDIO_SERVICE);
    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    ResultProcessor.process(this, intent, ResultProcessor.RESULT_SUCCESS,
        getString(R.string.phone_set_silent));
   }
  
  /**
   * set the phone to vibrate
   */
  private void setPhoneVibrate() {
    AudioManager audioManager =(AudioManager) getSystemService(Context.AUDIO_SERVICE);
    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    ResultProcessor.process(this, intent, ResultProcessor.RESULT_SUCCESS,
        getString(R.string.phone_set_on_vibrate));
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
   * Show a notification on the notification bar.
   * @param intent provide the message to show
   */
  private void showNotification(Intent intent) {
    String title = intent.getStringExtra(ShowNotificationAction.PARAM_TITLE);
    String message = intent.getStringExtra(ShowNotificationAction.PARAM_ALERT_MESSAGE);
    UtilUI.showNotification(this, UtilUI.NOTIFICATION_ACTION, title, message);
    ResultProcessor.process(this, intent, ResultProcessor.RESULT_SUCCESS, null);
  }

  /**
   * Shows a alert window to uer.
   * @param intent provide the message to show
   */
  private void showAlert(Intent intent) {
    String message = intent.getStringExtra(ShowAlertAction.PARAM_ALERT_MESSAGE);
    if (message == null) {
      Log.w("showAlert", "No user message provided");
      message = getString(R.string.action_default_message);
    }
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    ResultProcessor.process(this, intent, ResultProcessor.RESULT_SUCCESS, null);        
  }
  
  /**
   * Set the brightness of the screen, the value is between 0 and 255. {@link SCREEN_BRIGHTNESS}
   * @param brightness
   */
  private void setScreenBrightness(Intent intent) {
    int brightness = intent.getIntExtra(SetScreenBrightnessAction.PARAM_BRIGHTNESS, 200);
    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
    ResultProcessor.process(this, intent, ResultProcessor.RESULT_SUCCESS, null);
  }
  

}
