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
import edu.nyu.cs.omnidroid.core.CP;


public class SMSCatcherActivity extends Activity {
    /** Called when the activity is first created.*/ 
  private String uri;
  private String uri2;
  private String text;
  public String txtmsg;
  Intent intent;  
  
  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main2);
        this.intent=getIntent();
        uri = getURI(intent);
        uri2 = getURI2(intent);
        text= retrieveURI2(uri2.toString());
        
       displayAction(uri.toString(),text);
      // deleteSMS();
     this.finish();
  } 
       
       public String getURI(Intent intent1)
  {
    Bundle b = intent1.getExtras();
    Object c = b.get("uri");
    String uri=c.toString();
    return uri;
    
  }
       public String getURI2(Intent intent1)
       {
         Bundle b = intent1.getExtras();
         Object c = b.get("uri2");
         String uri22=c.toString();
         return uri22;
       }
       public String retrieveURI2(String uri2) 
       {
         String[] cols = null;
         String str_uri = uri2;
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
                  txtmsg = cur.getString(cur.getColumnIndex(CP.ACTION_DATA));
                  
                }
                           
                 
             } while (cur.moveToNext());

             }
           return txtmsg;
            
           }
       
       
       
       public void displayAction(String uri, String txtmsg) 
       {
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
    	           // String txtmsg = cur.getString(cur.getColumnIndex(CP.));
    	            Toast.makeText(
    	                getBaseContext(),
    	                /*cur.getString(cur.getColumnIndex(CP._ID)) + ","
    	                +*/ number, Toast.LENGTH_LONG).show();
    	            try
    	            {
    	            	sendSMS(number,txtmsg);
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
       
      public void deleteSMS()
      {
    	  try
 	     {
 	     getContentResolver().delete(Uri.parse("content://sms/conversations/1"), null, null);
          Toast.makeText(getBaseContext(),"Messages deleted",Toast.LENGTH_LONG).show();
 	     }catch(Exception e){}
      }
  }
    


