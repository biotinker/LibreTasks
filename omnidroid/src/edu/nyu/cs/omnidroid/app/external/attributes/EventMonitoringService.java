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
package edu.nyu.cs.omnidroid.app.external.attributes;

import java.lang.reflect.Constructor;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * The service creates monitors for System Events, and could later support third party applications.
 */
public class EventMonitoringService extends Service {

  // TODO (dvo203): This value should be checked at runtime, to determine the environment.
  public static final boolean EXECUTING_ON_EMULATOR = true;

  private static final String monitorPackage = "edu.nyu.cs.omnidroid.external.attributes";

  private final IBinder mBinder = new LocalBinder();

  /*
   * TODO (dvo203): This should eventually be stored in the DB (currently not available). It would
   * also allows us to support third party events, like Facebook update.
   */
  /** List of monitors to run */
  private static String MONITORS[] = {
    monitorPackage + "." + "PhoneRingingMonitor", 
    monitorPackage + "." + "LocationMonitor",
    monitorPackage + "." + "TimeMonitor" };

  public class LocalBinder extends Binder {
    EventMonitoringService getService() {
      return EventMonitoringService.this;
    }
  }

  public static void startService(Context context) {
    ComponentName service = context.startService(new Intent(context, EventMonitoringService.class));
    if (null == service) {
      Toast.makeText(context, "Failed to start Event Monitoring Service", Toast.LENGTH_LONG).show();
      Log.i("EventMonitoringService", "EventMonitoringService did not start.");
    }
    // TODO (dvo203): Log the service start
  }

  public static void stopService(Context context) {
    context.stopService(new Intent(context, EventMonitoringService.class));
  }

  /**
   * Actions performed on service initialization. EventMonitoringService performs initialization of
   * each SystemServiceMonitor.
   */
  @Override
  public void onCreate() {
    Class<?> theClass = null;
    Class<?>[] constructorParameters = new Class[1];
    Constructor<?> classConstructor;

    // For each monitor, create an instance and start (onCreate) the monitoring.
    for (int i = 0; i < MONITORS.length; i++) {
      try {
        theClass = Class.forName(MONITORS[i]);
        constructorParameters[0] = Class.forName("android.content.Context");
        classConstructor = theClass.getConstructor(constructorParameters);
        SystemServiceEventMonitor monitor = (SystemServiceEventMonitor) classConstructor
            .newInstance(this);
        monitor.init();
      } catch (Exception e) {
        Log.w("EventMonitoringService", MONITORS[i]
            + " did not start.\nThe following error occurred: " + e + e.getMessage()
            + e.getStackTrace());
      }
      // TODO (dvo203): Log the event
    }
  }

  /**
   * Actions performed on service shut down. EventMonitoringService performs the shut down of each
   * SystemServiceMonitor.
   */
  @Override
  public void onDestroy() {
    Class<?> theClass = null;
    Class<?>[] constructorParameters = new Class[1];
    Constructor<?> classConstructor;

    // For each monitor, stop (onDestroy) the monitoring.
    for (int i = 0; i < MONITORS.length - 1; i++) {
      try {
        theClass = Class.forName(MONITORS[i]);
        constructorParameters[0] = Class.forName("android.content.Context");
        classConstructor = theClass.getConstructor(constructorParameters);
        SystemServiceEventMonitor monitor = (SystemServiceEventMonitor) classConstructor
            .newInstance(this);
        monitor.stop();
      } catch (Exception e) {
        Log.w("EventMonitoringService", MONITORS[i]
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
