package edu.nyu.cs.omnidroid.bkgservice;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.nyu.cs.omnidroid.util.OmLogger;

/*Broadcast Receiver to detect system bootup*/
public class Starter extends BroadcastReceiver {
    
	public void onReceive(Context context, Intent intent) {
		/*Check to see if system is booted up*/
		if( "android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) 
		{
			ComponentName comp = new ComponentName(context.getPackageName(), BRService.class.getName());
			ComponentName service = context.startService(new Intent().setComponent(comp));
			
		    if (null == service){   
				Log.i("Starter", "Could not start service " + comp.toString());   }
			    OmLogger.write(context,"Starter could not start Service");
			}
			
		}
	}
	