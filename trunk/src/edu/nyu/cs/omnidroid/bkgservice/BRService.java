package edu.nyu.cs.omnidroid.bkgservice;
import edu.nyu.cs.omnidroid.util.*;
import java.util.ArrayList;
import java.util.HashMap;
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
    	ug.delete_all(getApplicationContext());
    	ug.write(getApplicationContext(), "Enabled", "True");
    		HashMap<String,String> HM=new HashMap<String,String>();
    	    HM.put("EventApp","SMS");
			HM.put("EventName","SMS_RECEIVED");
			HM.put("FilterType","S_PhoneNum");
			HM.put("FilterData","212-555-1234");
			HM.put("ActionName","SEND_EMAIL");
			HM.put("ActionApp","EMAIL");
			HM.put("ActionData","ContentProvider_URI");
			HM.put("EnableInstance","True");
    	ug.writeRecord(getApplicationContext(),HM);
        
    	//Code starts here
    	ArrayList<HashMap<String,String>> UCRecords=ug.readRecord(getApplicationContext(), "SMS_RECEIVED");
    	Iterator<HashMap<String,String>> i=UCRecords.iterator();
    	while(i.hasNext())
    	{
        HashMap<String,String> HM1=i.next();
        //Configure the Intent Filter with the Events
        if(HM1.get("EnableInstance").equalsIgnoreCase("True"))
    	Ifilter.addAction(HM1.get("EventName"));
    	}
    	
        /*Check the User Config to start OmniDroid*/
        String Enabled=ug.readLine(getBaseContext(), "Enabled"); 
        if(Enabled.equalsIgnoreCase("True"))
        {
        registerReceiver(BR, Ifilter);
        }
        else
        {
        Toast.makeText(getBaseContext(),"Stopping OmniDroid",5).show();
        unregisterReceiver(BR);
        }
        
     	  	
     	
    	}catch(Exception e)   	   
    	{
		Log.i("BRService",e.getLocalizedMessage());
		Log.i("BRService",e.toString());
		OmLogger.write(getApplicationContext(),"Not able to Enable/Diable Omnidroid");
		//Logger.write("Unable to start BroadcastReceiver");
    	}
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
}
