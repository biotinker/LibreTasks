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
import android.util.Log;
import edu.nyu.cs.omnidroid.model.DataTypes.DataTypeValidationException;
import edu.nyu.cs.omnidroid.model.DataTypes.OmniArea;

/**
 * The class is responsible for communication with the Location Service. It provides access to
 * Location Services External Attribute, as well as Initiates Location Change Intents.
 */
public class LocationService extends Service implements ExternalAttribute {
  private static OmniArea lastLocation;
  // TODO (dvo203): Change interval to 5 minutes (300000)
  /** Minimum frequency in updates(in milliseconds). Default value is 300000 (5 minutes). */
  private static final long MIN_PROVIDER_UPDATE_INTERVAL = 30000;
  // TODO (dvo203): Change distance to 50 meters (50)
  /** Minimum change in location(in meters). Default value is 50 meters. */
  private static final float MIN_PROVIDER_UPDATE_DISTANCE = 5;
  private static final String PROVIDER = LocationManager.GPS_PROVIDER;
  private static final String serviceName = "OmniDroidLocationService";

  public class LocalBinder extends Binder {
    LocationService getService() {
      return LocationService.this;
    }
  }

  @Override
  public void onCreate() {
    lastLocation = null;
    LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
    if (lm == null) {
      Log.i("LocationService", "Could not obtain LOCATION_SERVICE from the system.");
      return;
    }
    lm.requestLocationUpdates(PROVIDER, MIN_PROVIDER_UPDATE_INTERVAL, MIN_PROVIDER_UPDATE_DISTANCE,
        locationListener);
  }

  @Override
  public void onDestroy() {
    LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
    lm.removeUpdates(locationListener);
  }

  private final IBinder mBinder = new LocalBinder();

  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  private final LocationListener locationListener = new LocationListener() {
    public void onLocationChanged(Location location) {
      OmniArea newLocation;
      try {
        newLocation = new OmniArea(null, location.getLatitude(), location.getLongitude(), location
            .getAccuracy());
      } catch (DataTypeValidationException e) {
        newLocation = null;

      }

      if (newLocation != null && lastLocation != newLocation) {
        // Create intent
/*
        Intent intent = new Intent(LocationChangedEvent.ACTION_NAME);
        String temp = newLocation.toString();
        intent.putExtra(LocationChangedEvent.ATTRIBUTE_CURRENT_LOCATION, temp);
        sendBroadcast(intent);
*/
      }
    }

    /** Required to implement. Do nothing. */
    public void onProviderDisabled(String provider) {
    }

    /** Required to implement. Do nothing. */
    public void onProviderEnabled(String provider) {
    }

    /** Required to implement. Do nothing. */
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
  };

  /**
   * Returns current location.
   * 
   * @return OmniArea object that has longitude, latitude, and accuracy (proximityDistance)
   * @throws ExternalAttributeAccessException
   *           throws an exception if attribute is unavailable.
   */
  public OmniArea getAttributeValue() throws ExternalAttributeAccessException {
    if (lastLocation == null) {
      Log.i("LocationService", "Could not obtain Current Location from the system.");
      throw new ExternalAttributeAccessException("Location Service is not available.");
    }
    return lastLocation;
  }

  public String getName() {
    return serviceName;
  }
}
