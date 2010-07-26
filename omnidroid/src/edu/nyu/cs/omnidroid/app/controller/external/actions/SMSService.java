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
import edu.nyu.cs.omnidroid.app.controller.Action;
import edu.nyu.cs.omnidroid.app.controller.ResultProcessor;
import edu.nyu.cs.omnidroid.app.controller.actions.SendSmsAction;
import edu.nyu.cs.omnidroid.app.controller.actions.ShowNotificationAction;

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
  private boolean notificationIsOn;

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
    notificationIsOn = intent.getBooleanExtra(Action.NOTIFICATION, false);

    Toast.makeText(this, "SMS Service Started", Toast.LENGTH_LONG).show();

    smsResultReceiver = new  BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
        case Activity.RESULT_OK:
          showNotification(getString(R.string.sms_sent));
          ResultProcessor.process(context, intent, ResultProcessor.RESULT_SUCCESS);
          break;
        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
          showNotification(getString(R.string.sms_not_sent) + getString(R.string.separator_comma)
              + getString(R.string.generic_failure));
          ResultProcessor.process(context, intent, ResultProcessor.RESULT_FAILURE_UNKNOWN);
          break;
        case SmsManager.RESULT_ERROR_NO_SERVICE:
          showNotification(getString(R.string.sms_not_sent) + getString(R.string.separator_comma)
              + getString(R.string.no_service));
          ResultProcessor.process(context, intent, ResultProcessor.RESULT_FAILURE_SERVICE);
          break;
        case SmsManager.RESULT_ERROR_NULL_PDU:
          showNotification(getString(R.string.sms_not_sent) + getString(R.string.separator_comma)
              + getString(R.string.null_pdu));
          ResultProcessor.process(context, intent, ResultProcessor.RESULT_FAILURE_UNKNOWN);
          break;
        case SmsManager.RESULT_ERROR_RADIO_OFF:
          showNotification(getString(R.string.sms_not_sent) + getString(R.string.separator_comma)
              + getString(R.string.radio_off));
          ResultProcessor.process(context, intent, ResultProcessor.RESULT_FAILURE_SERVICE);
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
  private  void showNotification(String message) {
    if (notificationIsOn) {
      Intent intent = new Intent();
      intent.setClassName(ShowNotificationAction.OMNIDROID_PACKAGE_NAME, OmniActionService.class.getName());
      intent.putExtra(OmniActionService.OPERATION_TYPE, OmniActionService.SHOW_NOTIFICATION_ACTION);
      intent.putExtra(ShowNotificationAction.PARAM_TITLE, getString(R.string.omnidroid));
      intent.putExtra(ShowNotificationAction.PARAM_ALERT_MESSAGE, message);
      startService(intent);
    } else {
      Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
  }
  
  @Override
  public void onDestroy() {
    super.onDestroy();
    unregisterReceiver(smsResultReceiver);
  }

}