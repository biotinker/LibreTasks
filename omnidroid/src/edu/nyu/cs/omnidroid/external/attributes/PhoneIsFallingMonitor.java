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
package edu.nyu.cs.omnidroid.external.attributes;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import edu.nyu.cs.omnidroid.core.PhoneIsFallingEvent;

/**
 * Monitors accelerator state and broadcasting PhoneIsFalling Intent when phone is in free fall.
 */
public class PhoneIsFallingMonitor implements SystemServiceEventMonitor {
  private static final String SYSTEM_SERVICE_NAME = "SENSOR_SERVICE";
  private static final String MONITOR_NAME = "PhoneIsFallingMonitor";
  /** Normal operational range around NORMAL_VALUE. */
  private static final double NORMAL_RANGE = 8.5;
  /** Average normal acceleration. */
  private static final double NORMAL_VALUE = SensorManager.GRAVITY_EARTH;
  /** Falling for lesser values. */
  private static final double MIN_ACCELERATION = NORMAL_VALUE - NORMAL_RANGE;
  /** Flying for higher values. */
  private static final double MAX_ACCELERATION = NORMAL_VALUE + NORMAL_RANGE;
  /** Time (in milliseconds) of last notification sent. */
  private static long lastUpdate;
  private static final long NO_VALUE = -1;
  /**
   * Minimum time interval between notifications (in milliseconds). 
   * Default value is 5000 (5 minutes).
   * */
  private static final long MIN_DELAY = 5000;

  private Context context;

  public PhoneIsFallingMonitor(Context context) {
    this.context = context;
  }

  public void init() {
    /*
     * getSystemService(SENSOR_SERVICE) does not work correctly on emulator. Therefore the monitor
     * will not initialize if EXECUTING_ON_EMULATOR flag is true.
     */
    if (EventMonitoringService.EXECUTING_ON_EMULATOR) {
      return;
    }
    
    SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    if (sm == null) {
      Log.i(MONITOR_NAME, "Could not obtain SENSOR_SERVICE from the system.");
      return;
    }
    List<Sensor> sensorList = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);

    lastUpdate = NO_VALUE;

    if (sensorList.isEmpty() == false) {
      sm.registerListener(sensorEventListener, sensorList.get(0), SensorManager.SENSOR_DELAY_UI);
    } else {
      Log.i(MONITOR_NAME, "No Accelerometer Found.");
      return;
    }
  }

  public void stop() {
    /* 
     * getSystemService(SENSOR_SERVICE) does not work correctly on emulator. The refore the monitor 
     * will not initialize if EXECUTING_ON_EMULATOR flag is true. 
     */ 
    if(EventMonitoringService.EXECUTING_ON_EMULATOR) return; 
      
    SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);  
      
    sm.unregisterListener(sensorEventListener);
  }

  private final SensorEventListener sensorEventListener = new SensorEventListener() {
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {

      // Acceleration by axis
      double ax = event.values[SensorManager.DATA_X];
      double ay = event.values[SensorManager.DATA_Y];
      double az = event.values[SensorManager.DATA_Z];
      // Offset (calibration) values by axis.
      double cx = 0;
      double cy = 0;
      double cz = 0;

      // Total Acceleration
      double acceleration = Math.sqrt(Math.pow(ax - cx, 2) + Math.pow(ay - cy, 2)
          + Math.pow(az - cz, 2));

      // Only broadcasts if the Phone exceeded minimum acceleration.
      if (acceleration < MIN_ACCELERATION) {
        long curTime = System.currentTimeMillis();
        if (lastUpdate == NO_VALUE || (curTime - lastUpdate) > MIN_DELAY) {
          lastUpdate = curTime;
          Intent intent = new Intent(PhoneIsFallingEvent.ACTION_NAME);
          intent.putExtra(PhoneIsFallingEvent.ATTRIBUTE_ACCELERATIONS, acceleration + "\n"
              + (ax - cx) + "\n" + (ay - cy) + "\n" + (az - cz));
          context.sendBroadcast(intent);
        }
      }
    }
  };

  public String getMonitorName() {
    return MONITOR_NAME;
  }

  public String getSystemServiceName() {
    return SYSTEM_SERVICE_NAME;
  }
}
