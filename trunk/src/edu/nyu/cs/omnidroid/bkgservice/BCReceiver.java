package edu.nyu.cs.omnidroid.bkgservice;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.util.OmLogger;
import edu.nyu.cs.omnidroid.util.UGParser;
import edu.nyu.cs.omnidroid.core.*;

public class BCReceiver extends BroadcastReceiver {
	Context context;
	@Override
	public void onReceive(Context context, Intent intent)
	    {this.context = context;
		//Toast.makeText(context,"Starting BR",Toast.LENGTH_LONG).show();
		try{
			
		Toast.makeText(context,intent.getAction(),5).show();
	    
	   	/*ProcessIntent p = new ProcessIntent(intent,context);
		String filter = p.matchEventName();
		String[] cols = p.getActionData();*/
		
		intent.setClass(context, edu.nyu.cs.omnidroid.core.DummyActivity.class);
		intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		Log.i("Received Intent", intent.getAction());
		}catch(Exception e)
		{
			Log.i("Exception in Intent",e.getLocalizedMessage());
			OmLogger.write(context,"Unable to execute required action");
		}
		}
	    }
	