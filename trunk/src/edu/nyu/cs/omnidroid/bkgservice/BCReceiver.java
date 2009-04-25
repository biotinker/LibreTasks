package edu.nyu.cs.omnidroid.bkgservice;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.util.OmLogger;
import edu.nyu.cs.omnidroid.util.UGParser;
import edu.nyu.cs.omnidroid.core.*;

public class BCReceiver extends BroadcastReceiver {
	
	//Context context;
	@Override
	public void onReceive(Context context, Intent intent)
	    {
		//this.context = context;
		//Toast.makeText(context,"Caught by Broadcast Receiver",Toast.LENGTH_LONG).show();
		try{
			if (intent.getAction().contains("SMS_RECEIVED"))
			{
				readSMS(context,intent);
			}
			else
			{
			intent.setClass(context, edu.nyu.cs.omnidroid.core.DummyActivity.class);
			intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
			}
		Log.i("Received Intent", intent.getAction());
		}catch(Exception e)
		{
			Log.i("Exception in Intent",e.getLocalizedMessage());
			OmLogger.write(context,"Unable to execute required action");
		}
		}
	
	public void readSMS(Context context, Intent intent)
	{
		Bundle bundle = intent.getExtras();
	    Toast.makeText(context, intent.getAction() , Toast.LENGTH_SHORT).show();
	    SmsMessage[] msgs = null;
	    String str = "";
	    if (bundle != null) {
	      Object[] pdus = (Object[])bundle.get("pdus");
	      msgs = new SmsMessage[pdus.length];
	      for (int i = 0; i < msgs.length; i++) {
	        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
	        str += "SMS from " + msgs[i].getOriginatingAddress();
	        str += " :";
	        str += msgs[i].getMessageBody().toString();
	        str += "\n";
	      }
	      Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

	    }
	    }
}
	