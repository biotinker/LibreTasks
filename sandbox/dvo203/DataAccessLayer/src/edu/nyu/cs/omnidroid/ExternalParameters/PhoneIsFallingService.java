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
package edu.nyu.cs.omnidroid.ExternalParameters;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Monitors accelerator state and broadcasting PhoneIsFalling Intent when phone is in free fall.
 */
public class PhoneIsFallingService extends Service {
  private static final String serviceName = "OmniDroidGravityService";
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
  /** Minimum time laps between notifications (in milliseconds). 
   * Default value is 5000 (5 minutes). 
   * */
  private static final long MIN_DELAY = 5000;

  public class LocalBinder extends Binder {
    PhoneIsFallingService getService() {
      return PhoneIsFallingService.this;
    }
  }

  @Override
  public void onCreate() {
    SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
    if (sm == null) {
      Log.i("PhoneIsFallingService", "Could not obtain SENSOR_SERVICE from the system.");
      return;
    }
    List<Sensor> sensorList = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);

    lastUpdate = NO_VALUE;

    if (sensorList.isEmpty() == false) {
      sm.registerListener(sensorEventListener, sensorList.get(0), SensorManager.SENSOR_DELAY_UI);
    } else {
      Log.i("PhoneIsFallingService", "No Accelerometer Found.");
      return;
    }
  }

  @Override
  public void onDestroy() {
    SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
    sm.unregisterListener(sensorEventListener);
  }

  private final IBinder mBinder = new LocalBinder();

  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
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
          /*
          lastUpdate = curTime;
          Intent intent = new Intent(PhoneIsFallingEvent.ACTION_NAME);
          intent.putExtra(PhoneIsFallingEvent.ATTRIBUTE_ACCELERATIONS, acceleration + "\n"
              + (ax - cx) + "\n" + (ay - cy) + "\n" + (az - cz));
          sendBroadcast(intent);
          */
        }
      }
    }
  };

  public String getName() {
    return serviceName;
  }
}
