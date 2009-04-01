package edu.nyu.cs.omnidroid.bkgservice;

import edu.nyu.cs.omnidroid.util.OmLogger;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Starter extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		if( "android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) 
		{
			ComponentName comp = new ComponentName(context.getPackageName(), BRService.class.getName());
			ComponentName service = context.startService(new Intent().setComponent(comp));
			Toast.makeText(context,intent.getAction(),5).show();
			if (null == service){   
				Log.i("Starter", "Could not start service " + comp.toString());   }
			    OmLogger.write(context,"Starter could not start Service");
			}
		}
}
