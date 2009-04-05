package edu.nyu.cs.omnidroid.bkgservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.util.OmLogger;

public class BCReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent)
	    {
		try{
		Toast.makeText(context,intent.getAction(),5).show();
	    
	    /* Call to Rajivs Code from here.*/
	   
		Log.i("Received Intent", intent.getAction());
		}catch(Exception e)
		{
			Log.i("Exception in Intent",e.getLocalizedMessage());
			OmLogger.write(context,"Unable to execute required action");
		}
		}
	    }
	