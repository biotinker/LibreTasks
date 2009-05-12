/**
 * Catches the SMS_SEND intent broadcasted by OmniDroid.
 * Starts the SMSCatcherActivity. 
 */
package edu.nyu.cs.omnidroid.external.catcherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Sucharita
 * 
 */

public class SMSCatcher extends BroadcastReceiver {
  Context context;
  @Override
  public void onReceive(Context context, Intent intent) {
    this.context = context;
    Toast.makeText(context, intent.getAction(), 5).show();
    if (intent.getAction().contains("SMS_SEND")) {
      try{
    	  Toast.makeText(context, "Caught!", Toast.LENGTH_LONG).show();
      intent.setClass(context, edu.nyu.cs.omnidroid.external.catcherapp.SMSCatcherActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
      Log.i("Received Intent", intent.getAction());
    }catch(Exception e)
    {
    Log.i("Exception in Intent",e.getLocalizedMessage());
    
    }
    
      
    }
    else
    {
      Log.i("Intent Not Received", "Fail");
    }
    }
  
  
}
      
      
      


