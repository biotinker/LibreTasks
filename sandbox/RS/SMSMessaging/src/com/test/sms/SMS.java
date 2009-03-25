package com.test.sms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMS extends Activity {
	Button SendSMS;
	EditText PhoneNo;
	EditText Message;
	Button Reset;
	Button Save;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SendSMS=(Button) findViewById(R.id.SendSMS);
        Reset= (Button) findViewById(R.id.Reset);
        Save=(Button) findViewById(R.id.Save);
        PhoneNo=(EditText) findViewById(R.id.PhoneNo);
        Message=(EditText) findViewById(R.id.Message);
    
    SendSMS.setOnClickListener(new View.OnClickListener()
    {
    	public void onClick(View v)
    	{
    		String phoneNo = PhoneNo.getText().toString();
            String message = Message.getText().toString();                 
            if (phoneNo.length()>0 && message.length()>0)                
                sendSMS(phoneNo, message);                
            else
          Toast.makeText(getBaseContext(),"Both fields are required.",Toast.LENGTH_SHORT).show();
        }
    });  
    Reset.setOnClickListener(new View.OnClickListener()
    {
    	@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//String message=Message.getText().toString();
			//CLEAR the MESSAGE IN THE MESSAGE BOX
    	Toast.makeText(getBaseContext(),"Coming Soon :-)",Toast.LENGTH_SHORT).show();
		}
    });
    Save.setOnClickListener(new View.OnClickListener()
    {
    	@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//String message=Message.getText().toString();
			//SAVE THE MESSAGE IN OUR CONTENT PROVIDER FOR LATER USE
    		Toast.makeText(getBaseContext(),"Coming Soon :-)",Toast.LENGTH_SHORT).show();
		}
    });
    }

   
    
    private void sendSMS(String phoneNumber, String message)
    {        
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
 
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,new Intent(SENT), 0);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,new Intent(DELIVERED), 0);
 
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) 
            {
            	Toast.makeText(getBaseContext(), "SMS SENT",Toast.LENGTH_SHORT).show();
            }
        }, new IntentFilter(SENT));
 
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) 
            {
                Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();

            }
        }, new IntentFilter(DELIVERED));   
        
 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
    }
		
		
	}    
