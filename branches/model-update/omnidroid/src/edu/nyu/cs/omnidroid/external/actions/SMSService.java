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
package edu.nyu.cs.omnidroid.external.actions;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;

/**
 * This class automatically sends SMS when it receives SMS intent 
 * created by SendSmsAction class. 
 *
 */
public class SMSService extends Service {
    
	/**
	 * attributes field names
	 */
	private String phoneNumber = null;
	private String textMessage = null;
	
    @Override
    public IBinder onBind(Intent intent){
    	return null;
    }
/**
 * method to create SMS Service    
 */
    @Override
    public void onCreate(){
    	super.onCreate();
    }

/**
 * method to destroy SMS service.    
 */
    @Override
    public void onDestroy(){
    	super.onDestroy();
    }
    
    /**
	 * This method sends SMS by using an intent created by SendSmsAction class
	 * This method is automatically called by onCreate method when service starts
	 * @param parameter
     *         
     *          List of required parameters: <br>
     *          <ol>
     *          <li>Intent: intent created by SendSmsAction class 
     *          <li> startId : A unique integer representing this specific request to start
     *          </ol>  
	 */
    public void onStart(Intent intent, int startId){
    	super.onStart(intent, startId);
    	String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        phoneNumber = intent.getStringExtra("PhoneNumber");
        textMessage = intent.getStringExtra("Sms");
        
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        
        registerReceiver(new BroadcastReceiver() {
          @Override
          public void onReceive(Context arg0, Intent arg1) {
            Toast.makeText(getBaseContext(), "SMS SENT", Toast.LENGTH_SHORT).show();
          }
        }, new IntentFilter(SENT));

        registerReceiver(new BroadcastReceiver() {
          @Override
          public void onReceive(Context arg0, Intent arg1) {
            Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();

          }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, textMessage, sentPI, deliveredPI);
    }
}