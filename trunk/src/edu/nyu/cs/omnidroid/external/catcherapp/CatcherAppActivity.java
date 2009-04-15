/*package edu.nyu.cs.omnidroid.external.catcherapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
public class CatcherAppActivity extends Activity {
    /** Called when the activity is first created. 
    @Override
    public void onCreate(Bundle savedInstanceState) {
      Button SendSMS;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SendSMS = (Button) findViewById(R.id.SendSMS);
                
       SendSMS.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          String phoneNo = "5556";
          String message = Message.getText().toString();
         if (phoneNo.length() > 0 && message.length() > 0)
          sendSMS(phoneNo, message);
         else
          Toast.makeText(getBaseContext(), "Both fields are required.", Toast.LENGTH_SHORT).show();
            }
          });
           
        }
        private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
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
          sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        }

      }



*/  