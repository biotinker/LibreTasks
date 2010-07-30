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
package edu.nyu.cs.omnidroid.app.controller.external.actions;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.app.R;
import edu.nyu.cs.omnidroid.app.controller.ResultProcessor;
import edu.nyu.cs.omnidroid.app.controller.actions.SendSmsAction;

/**
 * This class automatically sends SMS when it receives SMS intent created by {@link SendSmsAction}
 * class.
 * 
 */
public class SMSService extends Service {

  private PendingIntent sentPI;
  private PendingIntent deliveredPI;
  
  BroadcastReceiver smsResultReceiver;

  /**
   * attributes field names
   */
  public static final String SENT = "SMS_SENT";
  public static final String DELIVERED = "SMS_DELIVERED";


  private String phoneNumber;
  private String textMessage;

  private Intent actionIntent;
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
    actionIntent = intent;

    sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
    deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

    phoneNumber = intent.getStringExtra(SendSmsAction.PARAM_PHONE_NO);
    textMessage = intent.getStringExtra(SendSmsAction.PARAM_SMS);

    Toast.makeText(this, "SMS Service Started", Toast.LENGTH_LONG).show();

    smsResultReceiver = new  BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
        case Activity.RESULT_OK:
          ResultProcessor.process(context, actionIntent, ResultProcessor.RESULT_SUCCESS,
              getString(R.string.sms_sent));
          break;
        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
          ResultProcessor.process(context, actionIntent, ResultProcessor.RESULT_FAILURE_UNKNOWN, 
              getString(R.string.sms_failed_generic_failure));
          break;
        case SmsManager.RESULT_ERROR_NO_SERVICE:
          ResultProcessor.process(context, actionIntent, ResultProcessor.RESULT_FAILURE_SERVICE,
              getString(R.string.sms_failed_no_service));
          break;
        case SmsManager.RESULT_ERROR_NULL_PDU:
          ResultProcessor.process(context, actionIntent, ResultProcessor.RESULT_FAILURE_UNKNOWN, 
              getString(R.string.sms_failed_null_pdu));
          break;
        case SmsManager.RESULT_ERROR_RADIO_OFF:
          ResultProcessor.process(context, actionIntent, ResultProcessor.RESULT_FAILURE_SERVICE, 
              getString(R.string.sms_failed_radio_off));
          break;
        }
      }
    };
    registerReceiver(smsResultReceiver, new IntentFilter(SENT));

    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    tm.listen(new PhoneStateListener() {
      public void onCallStateChanged(int state, String incomingNumber) {
        // send SMS when phone is not ringing.
        if (state != TelephonyManager.CALL_STATE_RINGING) {
          SmsManager sms = SmsManager.getDefault();
          sms.sendTextMessage(phoneNumber, null, textMessage, sentPI, deliveredPI);
          // Update the Android SMS application
          ContentValues values = new ContentValues();
          values.put("address", phoneNumber);
          values.put("body", textMessage);
          getContentResolver().insert(Uri.parse("content://sms/sent"), values);
          // Stop listening to events.
          ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).listen(this,
              PhoneStateListener.LISTEN_NONE);
          stopSelf();
        }
      }
    }, PhoneStateListener.LISTEN_CALL_STATE);
  }
  
  @Override
  public void onDestroy() {
    super.onDestroy();
    unregisterReceiver(smsResultReceiver);
  }

}