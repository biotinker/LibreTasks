package com.test.sms;

import android.telephony.PhoneStateListener;

public class PhoneListener extends PhoneStateListener
{
	
	public void listen(PhoneStateListener listener, int events)
	{
		
	}
	/*TelephonyManager tm = 
		(TelephonyManager)Context.getSystemService(TELEPHONY_SERVICE); 
		tm.listen(this, PhoneListener.LISTEN_CALL_STATE); 
		
		public void onCallStateChanged(int state, String incomingNumber) 
		{ 
		    switch(state) 
		    { 
		          case TelephonyManager.CALL_STATE_RINGING: 
		              // Log.d(DEBUG, "PHONE IS RINGING"); 
		               break; 
		     } 
		}*/
}
