package com.CIntents;

import android.content.BroadcastReceiver;
import android.content.Intent;
//import android.util.Log;
import android.widget.Toast;
import android.content.Context;

public class ContactAdded extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent)
	    {
	        Toast.makeText(context, "help", Toast.LENGTH_SHORT).show();
	    }
}
	/*public void onReceive(Context context, Intent intent)
    {
	 try{
     Log.i("Received Intent", intent.getAction());
     Toast toast = Toast.makeText(context,23, Toast.LENGTH_LONG);
    		 toast.show();

     
     }
     catch(Exception e){
     Log.i("Exception in Intent",e.getLocalizedMessage());
     }
     }*/
    