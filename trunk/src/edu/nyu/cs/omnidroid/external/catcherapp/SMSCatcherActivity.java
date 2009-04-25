package edu.nyu.cs.omnidroid.external.catcherapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.core.CP;
public class SMSCatcherActivity extends Activity {
    /** Called when the activity is first created.*/ 
  private String uri;
  Intent intent;  
  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main2);
        this.intent=getIntent();
        uri = getURI(intent);
       
       displayAction(uri.toString());
     this.finish();
  } 
       
       public String getURI(Intent intent1)
  {
    Bundle b = intent1.getExtras();
    Object c = b.get("uri");
    String uri=c.toString();
    return uri;
    
  }
       
       public void displayAction(String uri) {
    	   String[] cols = null;
    	   String str_uri = uri;
    	     String[] temp = null;
    	     temp = str_uri.split("/");
    	     String num = temp[temp.length - 1];
    	     String final_uri=str_uri.substring(0, str_uri.length()-num.length()-1);
    	     int new_id = Integer.parseInt(num);
    	     
    	     Cursor cur = managedQuery(Uri.parse(final_uri), null, null, null, null);
    	     if (cur.moveToFirst()) {

    	       
    	       do {

    	         int id = Integer.parseInt(cur.getString(cur.getColumnIndex("_id")));

    	           
    	          if (new_id==id)
    	          {
    	            String number = cur.getString(cur.getColumnIndex(CP.ACTION_DATA));
    	            Toast.makeText(
    	                getBaseContext(),
    	                /*cur.getString(cur.getColumnIndex(CP._ID)) + ","
    	                +*/ number, Toast.LENGTH_LONG).show();
    	            try
    	            {
    	            	sendSMS(number,"DND");
    	            }catch(Exception e){e.toString();}
    	          }
    	                     
    	           
    	       } while (cur.moveToNext());

    	       }
    	     
    	      
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
    


