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
package edu.nyu.cs.omnidroid.app.external.actions;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.app.core.SendSmsAction;

/**
 * This class automatically sends SMS when it receives SMS intent created by {@link SendSmsAction}
 * class.
 * 
 */
public class SMSService extends Service {

  private PendingIntent sentPI;
  private PendingIntent deliveredPI;

  /**
   * attributes field names
   */
  private String phoneNumber;
  private String textMessage;
  public static final String SENT = "SMS_SENT";
  public static final String DELIVERED = "SMS_DELIVERED";

  /**
   * @return null because client can't bind to this service
   */
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  /**
   * This method sends SMS by using an intent created by SendSmsAction class This method is
   * automatically called by onCreate method when service starts
   * 
   * @param parameter
   * 
   *          List of required parameters: <br>
   *          <ol>
   *          <li>Intent: intent created by SendSmsAction class
   *          <li>startId : A unique integer representing this specific request to start
   *          </ol>
   */
  public void onStart(Intent intent, int startId) {
    super.onStart(intent, startId);

    sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
    deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

    phoneNumber = intent.getStringExtra(SendSmsAction.PARAM_PHONE_NO);
    textMessage = intent.getStringExtra(SendSmsAction.PARAM_SMS);

    Toast.makeText(this, "SMS Service Started", Toast.LENGTH_LONG).show();

    registerReceiver(new BroadcastReceiver() {
      @Override
      public void onReceive(Context arg0, Intent arg1) {
        switch (getResultCode()) {
        case Activity.RESULT_OK:
          Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
          break;
        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
          Toast.makeText(getBaseContext(), "SMS not sent - Generic failure", Toast.LENGTH_SHORT)
              .show();
          break;
        case SmsManager.RESULT_ERROR_NO_SERVICE:
          Toast.makeText(getBaseContext(), "SMS not sent - No service", Toast.LENGTH_SHORT).show();
          break;
        case SmsManager.RESULT_ERROR_NULL_PDU:
          Toast.makeText(getBaseContext(), "SMS not sent - Null PDU", Toast.LENGTH_SHORT).show();
          break;
        case SmsManager.RESULT_ERROR_RADIO_OFF:
          Toast.makeText(getBaseContext(), "SMS not sent - Radio off", Toast.LENGTH_SHORT).show();
          break;
        }
      }
    }, new IntentFilter(SENT));

    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    tm.listen(new PhoneStateListener() {
      public void onCallStateChanged(int state, String incomingNumber) {
        // Only send SMS if the Phone state is not ringing.
        if (state != TelephonyManager.CALL_STATE_RINGING) {
          SmsManager sms = SmsManager.getDefault();
          sms.sendTextMessage(phoneNumber, null, textMessage, sentPI, deliveredPI);
          // Stop listening to events.
          ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).listen(this,
              PhoneStateListener.LISTEN_NONE);
        }
      }
    }, PhoneStateListener.LISTEN_CALL_STATE);
  }
}