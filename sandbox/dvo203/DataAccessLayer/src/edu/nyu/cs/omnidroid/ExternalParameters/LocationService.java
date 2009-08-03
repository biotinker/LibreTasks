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

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.model.DataTypes.DataType;
import edu.nyu.cs.omnidroid.model.DataTypes.DataTypeValidationException;
import edu.nyu.cs.omnidroid.model.DataTypes.OmniArea;

public class LocationService extends ExternalAttribute {
  private static OmniArea lastLocation;
  // TODO (dvo203): Change interval to 5 minutes (300000)
  private static final long MIN_PROVIDER_UPDATE_INTERVAL = 30000; // 5 minutes (in milliseconds).
  private static final float MIN_PROVIDER_UPDATE_DISTANCE = 50; // 50 meters (in meters)
  private static final String serviceName = "LocationService";
  private static final String PROVIDER = LocationManager.NETWORK_PROVIDER;
  /**
   * Class for clients to access.  Because we know this service always
   * runs in the same process as its clients, we don't need to deal with
   * IPC.
   */
  public class LocalBinder extends Binder {
    LocationService getService() {
          return LocationService.this;
      }
  }

  @Override
  public void onCreate() {
    // TODO (dvo203): Remove Toast Message
    Toast.makeText(this, serviceName + "(" + PROVIDER + ") service started.", Toast.LENGTH_SHORT).show();
    
    lastLocation = null;
      LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
      lm.requestLocationUpdates(PROVIDER, MIN_PROVIDER_UPDATE_INTERVAL,
          MIN_PROVIDER_UPDATE_DISTANCE, locationListener);
  }

  @Override
  public void onDestroy() {
      LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
      lm.removeUpdates(locationListener);

      // TODO (dvo203): Remove Toast Message
      Toast.makeText(this, serviceName + "(" + PROVIDER + ") service started.", Toast.LENGTH_SHORT).show();
  }

  // This is the object that receives interactions from clients.  See
  // RemoteService for a more complete example.
  private final IBinder mBinder = new LocalBinder();

  @Override
  public IBinder onBind(Intent intent) {
    // TODO Auto-generated method stub
    return mBinder;
  }

  private final LocationListener locationListener = new LocationListener() {
    public void onLocationChanged(Location location) {
      try {
        lastLocation = new OmniArea(null, location.getLatitude(), location.getLongitude(), location
            .getAccuracy());
      } catch (DataTypeValidationException e) {
        lastLocation = null;
      }
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
  };

  public static DataType getAttributeValue() throws ExternalParameterAccessException {
    return lastLocation;
  }

  public static String getName() {
    return serviceName;
  }
}
