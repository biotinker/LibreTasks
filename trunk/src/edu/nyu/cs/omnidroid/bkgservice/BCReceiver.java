package edu.nyu.cs.omnidroid.bkgservice;

import java.util.logging.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BCReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent)
	    {
	    Toast.makeText(context,intent.getAction(),5).show();
	    Toast.makeText(context, "yahoo", 5).show();
	    try{
		Log.i("Received Intent", intent.getAction());
		}catch(Exception e)
		{
			Log.i("Exception in Intent",e.getLocalizedMessage());
		}
		}
	    }
	