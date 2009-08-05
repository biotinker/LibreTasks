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
import android.os.Binder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Monitors phone state and broadcasts Phone Ringing Intent when phone rings.
 */
public class PhoneRingingService extends Service {
  private static final String serviceName = "OmniDroidPhoneService";

  private class LocalBinder extends Binder {
    PhoneRingingService getService() {
      return PhoneRingingService.this;
    }
  }

  @Override
  public void onCreate() {
    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    if (tm == null) {
      Log.i("PhoneRingingService", "Could not obtain TELEPHONY_SERVICE from the system.");
      return;
    }
    tm.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
  }

  @Override
  public void onDestroy() {
    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    tm.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
  }

  private final IBinder mBinder = new LocalBinder();

  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  private final PhoneStateListener phoneStateListener = new PhoneStateListener() {
    public void onCallStateChanged(int state, String incomingNumber) {
      // Only broadcasts if the Phone state changed to ringing.
      if (state == TelephonyManager.CALL_STATE_RINGING) {
        /*
        Intent intent = new Intent(PhoneRingingEvent.ACTION_NAME);
        intent.putExtra(PhoneRingingEvent.ATTRIBUTE_PHONE_NUMBER, incomingNumber);
        sendBroadcast(intent);
        */
      }
    }
  };

  public String getName() {
    return serviceName;
  }
}
