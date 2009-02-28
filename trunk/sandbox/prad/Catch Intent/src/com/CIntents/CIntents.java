package com.CIntents;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class CIntents extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        
        setContentView(R.layout.main);
        }catch(Exception e)
        {
        	Log.i("Exception",e.getLocalizedMessage());
        }
        }
 @Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	//IntentFilter Ifilter=new IntentFilter();
    //Ifilter.addAction("android.intent.action.INSERT");
    //Ifilter.addAction("android.intent.action.INSERT");
    //BroadcastReceiver BR=new ContactAdded();
    //registerReceiver(BR, Ifilter);
}    
}
