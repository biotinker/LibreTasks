/**
 * 
 */
package edu.nyu.cs.omnidroid.external.catcherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.util.OmLogger;

/**
 * @author Rajiv
 * 
 */

public class SMSListener extends BroadcastReceiver {
  Context context;
  @Override
  public void onReceive(Context context, Intent intent) {
    this.context = context;
    Toast.makeText(context, intent.getAction(), 5).show();
    if (intent.getAction().equalsIgnoreCase("SMS_SENT")) {
      // Bundle bundle = intent.getExtras();
      try{
      intent.setClass(context, edu.nyu.cs.omnidroid.external.catcherapp.CatcherAppActivity.class);
      intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
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
      
      
      


