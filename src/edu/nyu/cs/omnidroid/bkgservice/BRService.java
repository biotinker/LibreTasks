package edu.nyu.cs.omnidroid.bkgservice;
import edu.nyu.cs.omnidroid.util.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class BRService extends Service{
    /* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	IntentFilter Ifilter=new IntentFilter();
    BroadcastReceiver BR=new BCReceiver();
    @Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate() 
    {
    	try
    	{
    	// To be deleted from this Code
    	UGParser ug=new UGParser();
    	ug.write(getBaseContext(), "Enabled", "True");
    	ug.write(getBaseContext(), "EventName", "SMS_RECEIVED");
        
    	String Enabled=ug.readLine(getBaseContext(), "Enabled"); 
    	ArrayList<String> Events=ug.readLines(getBaseContext(), "EventName");   	
        String Event;
        
    	Iterator<String> i=Events.iterator();
        while(i.hasNext())
    	{
        Event=i.next();
    	Toast.makeText(getBaseContext(),Event,5).show();
    	Ifilter.addAction(Event);
    	
    	}
    	
    	if(Enabled.equalsIgnoreCase("True"))
        {
        registerReceiver(BR, Ifilter);
        Toast.makeText(getBaseContext(),Enabled,5).show();
        }
        else
        {
        	Toast.makeText(getBaseContext(),":(",5).show();
        this.onDestroy();
        }
        
     	OmLogger.write(this,"3.Success");
     	OmLogger.read(this);
     	
     	
    	}catch(Exception e)   	   
    	{
		Log.i("BRService",e.getLocalizedMessage());
		Log.i("BRService",e.toString());
		
		//Logger.write("Unable to start BroadcastReceiver");
    	}
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(BR);
		super.onDestroy();
		
	}
}
