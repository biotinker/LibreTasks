package edu.nyu.cs.omnidroid.bkgservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.nyu.cs.omnidroid.util.OmLogger;

public class BCReceiver extends BroadcastReceiver {
	
	//Context context;
	@Override
	public void onReceive(Context context, Intent intent)
	    {
		//this.context = context;
		//Toast.makeText(context,"Caught by Broadcast Receiver",Toast.LENGTH_LONG).show();
		try
			{
			intent.setClass(context, edu.nyu.cs.omnidroid.core.DummyActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//Toast.makeText(context,getLastCallLogEntry(context),Toast.LENGTH_LONG).show();
		context.startActivity(intent);
			
		Log.i("Received Intent", intent.getAction());
		}catch(Exception e)
		{
			Log.i("Exception in Intent",e.getLocalizedMessage());
			OmLogger.write(context,"Unable to execute required action");
		}
		}
	
/*
 * This doesn't belong in this class.
 * As a result, it's been commented out and slated for removal.
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
*/
/*  
 * This doesn't belong in this class.
 * As a result, it's been commented out and slated for removal.
	private String getLastCallLogEntry( Context context ) {
		  String[] projection = new String[] {
	    	BaseColumns._ID,
	    	CallLog.Calls.NUMBER,
			CallLog.Calls.TYPE
		  };
		  ContentResolver resolver = context.getContentResolver();
	      Cursor cur = resolver.query( 
						CallLog.Calls.CONTENT_URI,
	                    projection, 
	                    null,
						null,
	                    CallLog.Calls.DEFAULT_SORT_ORDER );
	      int numberColumn = cur.getColumnIndex( CallLog.Calls.NUMBER ); 
	      int typeColumn = cur.getColumnIndex( CallLog.Calls.TYPE );
		  if( !cur.moveToNext()) {
			cur.close();
			return "";
		  }
		  String number = cur.getString( numberColumn );
	      String type = cur.getString( typeColumn );
		  String dir = null;
		  try {
			int dircode = Integer.parseInt( type );
			switch( dircode ) {
			  case CallLog.Calls.OUTGOING_TYPE:
					dir = "OUTGOING";
					break;

			  case CallLog.Calls.INCOMING_TYPE:
					dir = "INCOMING";
					break;

			  case CallLog.Calls.MISSED_TYPE:
					dir = "MISSED";
					break;
			}
	      } catch( NumberFormatException ex ) {}
		  if( dir == null )
				dir = "Unknown, code: "+type;
		  cur.close();
		  return dir+","+number;
	    }
*/
}
	