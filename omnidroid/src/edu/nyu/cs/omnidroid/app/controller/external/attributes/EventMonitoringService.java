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
package edu.nyu.cs.omnidroid.app.controller.external.attributes;

import edu.nyu.cs.omnidroid.app.controller.util.Logger;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

/**
 * The service creates monitors for System Events, and could later support third party applications.
 */
public class EventMonitoringService extends Service {

  private final IBinder mBinder = new LocalBinder();
  private static final String TAG = EventMonitoringService.class.getSimpleName();
  
  private final SystemServiceEventMonitor MONITORS[] = {
      new PhoneStateMonitor(this),
      new LocationMonitor(this),
      new TimeMonitor(this),
  };
   
  public class LocalBinder extends Binder {
    EventMonitoringService getService() {
      return EventMonitoringService.this;
    }
  }

  public static void startService(Context context) {
    
    ComponentName service = context.startService(new Intent(context, EventMonitoringService.class));
    if (null == service) {
      Toast.makeText(context, "Failed to start Event Monitoring Service", Toast.LENGTH_LONG).show();
      Logger.i(TAG, "EventMonitoringService did not start.");
    } else {
      Logger.i(TAG, "Started EventMonitoringService.");
    }
  }

  public static void stopService(Context context) {
  
    if (context.stopService(new Intent(context, EventMonitoringService.class))) {
      Logger.w(TAG, "EventMonitoringService stopped");
    }
  }

  /**
   * Actions performed on service initialization. EventMonitoringService performs initialization of
   * each SystemServiceMonitor.
   */
  @Override
  public void onCreate() {
    for (SystemServiceEventMonitor monitor : MONITORS) {
      try {
        monitor.init();
        Logger.i(TAG, monitor.getMonitorName() + ": Start\n");
      } catch (Exception e) {
        Logger.w(TAG, monitor.getMonitorName()
            + " did not start.\nThe following error occurred: " + e + e.getMessage()
            + e.getStackTrace());
      }
    }
  }

  /**
   * Actions performed on service shut down. EventMonitoringService performs the shut down of each
   * SystemServiceMonitor.
   */
  @Override
  public void onDestroy() {
    for (SystemServiceEventMonitor monitor : MONITORS) {
      try {
        monitor.stop();
      } catch (Exception e) {
        Logger.w(TAG, monitor.getMonitorName()
            + " did not stop.\nThe following error occurred: " + e + e.getMessage()
            + e.getStackTrace());
      }
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }
}
