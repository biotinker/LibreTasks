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
import android.view.KeyEvent;
import android.os.IBinder;
import android.os.SystemClock;
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
public class MediaActionService extends Service {
  
  //operation supported by this service
  public static final String OPERATION_TYPE = "OPERATION_TYPE";
  public static final int NO_ACTION = -1;
  public static final int PAUSE_MEDIA_ACTION = 1;
  public static final int PLAY_MEDIA_ACTION = 2;
  
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
    case PAUSE_MEDIA_ACTION :
      pauseMedia();
      break;
    case PLAY_MEDIA_ACTION :
      playMedia();
      break;
    default:
      Log.e("OmniActionSercive", "No such operation supported as: " + operationType);
    }
  }
  
  /**
   * Pause media
   */
  private void pauseMedia() {
    AudioManager audioManager =(AudioManager) getSystemService(Context.AUDIO_SERVICE);
    long eventtime = SystemClock.uptimeMillis() - 1;
	KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE, 0);
	KeyEvent upEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PAUSE, 0);
	audioManager.dispatchMediaKeyEvent(downEvent);
	audioManager.dispatchMediaKeyEvent(upEvent);
   }
  
  /**
   * set the phone to silent
   */
  private void playMedia() {
    AudioManager audioManager =(AudioManager) getSystemService(Context.AUDIO_SERVICE);
    long eventtime = SystemClock.uptimeMillis() - 1;
	KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY, 0);
	KeyEvent upEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY, 0);
	audioManager.dispatchMediaKeyEvent(downEvent);
	audioManager.dispatchMediaKeyEvent(upEvent);
   }
  

}
