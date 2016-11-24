/*  
 * Copyright (c) 2016  LibreTasks - https://github.com/biotinker/LibreTasks  
 *  
 *  This file is free software: you may copy, redistribute and/or modify it  
 *  under the terms of the GNU General Public License as published by the  
 *  Free Software Foundation, either version 3 of the License, or (at your  
 *  option) any later version.  
 *  
 *  This file is distributed in the hope that it will be useful, but  
 *  WITHOUT ANY WARRANTY; without even the implied warranty of  
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  
 *  General Public License for more details.  
 *  
 *  You should have received a copy of the GNU General Public License  
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 *  
 * This file incorporates work covered by the following copyright and  
 * permission notice:  
 /*******************************************************************************
 * Copyright 2009, 2010 Omnidroid - http://code.google.com/p/omnidroid 
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
package libretasks.app.controller.external.actions;

import java.util.ArrayList;

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
import libretasks.app.R;
import libretasks.app.controller.ResultProcessor;
import libretasks.app.controller.actions.SendSmsAction;

/**
 * This class automatically sends SMS when it receives SMS intent created by {@link SendSmsAction}
 * class.
 * 
 */
public class SMSService extends Service {
  private BroadcastReceiver smsResultReceiver;

  /**
   * attributes field names
   */
  public static final String INTENT_ACTION_SENT = "SMS_SENT";
  public static final String INTENT_ACTION_DELIVERED = "SMS_DELIVERED";

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
  public void onStart(final Intent intent, int startId) {
    super.onStart(intent, startId);

    Toast.makeText(this, "SMS Service Started", Toast.LENGTH_LONG).show();

    smsResultReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
        case Activity.RESULT_OK:
          ResultProcessor.process(context, intent, ResultProcessor.RESULT_SUCCESS,
              getString(R.string.sms_sent));
          break;
        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
          ResultProcessor.process(context, intent, ResultProcessor.RESULT_FAILURE_UNKNOWN,
              getString(R.string.sms_failed_generic_failure));
          break;
        case SmsManager.RESULT_ERROR_NO_SERVICE:
          ResultProcessor.process(context, intent, ResultProcessor.RESULT_FAILURE_SERVICE,
              getString(R.string.sms_failed_no_service));
          break;
        case SmsManager.RESULT_ERROR_NULL_PDU:
          ResultProcessor.process(context, intent, ResultProcessor.RESULT_FAILURE_UNKNOWN,
              getString(R.string.sms_failed_null_pdu));
          break;
        case SmsManager.RESULT_ERROR_RADIO_OFF:
          ResultProcessor.process(context, intent, ResultProcessor.RESULT_FAILURE_SERVICE,
              getString(R.string.sms_failed_radio_off));
          break;
        }
      }
    };
    registerReceiver(smsResultReceiver, new IntentFilter(INTENT_ACTION_SENT));

    final String phoneNumber = intent.getStringExtra(SendSmsAction.PARAM_PHONE_NO);
    final String textMessage = intent.getStringExtra(SendSmsAction.PARAM_SMS);
    final SMSService smsServiceInstance = this;

    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    tm.listen(new PhoneStateListener() {
      public void onCallStateChanged(int state, String incomingNumber) {
        // send SMS when phone is not ringing.
        if (state != TelephonyManager.CALL_STATE_RINGING) {
          final SmsManager sms = SmsManager.getDefault();

          /*
           * Split SMS message into chunks to avoid exceeding 160 characters, which will cause null
           * pointer exception (not documented in official API reference, but a lot of other people
           * experienced the same problem).
           */
          ArrayList<String> splitTextMessages = sms.divideMessage(textMessage);

          for (final String message : splitTextMessages) {
            Intent sentIntent = new Intent(intent);
            intent.setAction(INTENT_ACTION_SENT);
            intent.removeExtra(SendSmsAction.PARAM_SMS);
            intent.putExtra(SendSmsAction.PARAM_SMS, message);

            final PendingIntent sentPI = PendingIntent.getBroadcast(smsServiceInstance, 0,
                sentIntent, 0);
            
            Thread smsThread = new Thread(new Runnable() {
              public void run() {
                /*
                 * TODO (renctan): Consider using sendMultipartTextMessage() in the future. The
                 * reason why sendTextMessage() was used instead is because it appears that there is
                 * a bug in API 4 that will convert the SMS message into garbage characters in the
                 * receiving end.
                 */
                sms.sendTextMessage(phoneNumber, null, message, sentPI, null);
              }
            });

            smsThread.start();
          }
          
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
