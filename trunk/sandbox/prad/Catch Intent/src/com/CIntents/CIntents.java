package com.CIntents;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CIntents extends Activity {
    /** Called when the activity is first created. */
	IntentFilter Ifilter=new IntentFilter();
    BroadcastReceiver BR=new ContactAdded();
 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        	setContentView(R.layout.main);
        	Button button = (Button)findViewById(R.id.BR);
            button.setOnClickListener(BRListener);
       
        }catch(Exception e)
        {
        	Log.i("Exception",e.getLocalizedMessage());
        }
        }
	private OnClickListener BRListener = new OnClickListener() {
	       public void onClick(View v) {
	       	Intent intent = new Intent("ACTION_TIME_TICK");
	       	//intent.setAction("ACTION_TIME_TICK");
	       	sendBroadcast(intent);
	       }
	       };
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unregisterReceiver(BR);	
		super.onPause();
}

@Override
protected void onResume() {
	// TODO Auto-generated method stub
	try
	{
	super.onResume();
	/*BufferedWriter buf = new BufferedWriter(new FileWriter("UserConfig.txt"));
	buf.write("ACTION_TIME_TICK");
	buf.write("ACTION_TIME_CHANGED");
    
	BufferedReader bufr=new BufferedReader(new FileReader("UserConfig.txt"));
	
	String strLine;
	while ((strLine = bufr.readLine()) != null)   {
	      Ifilter.addAction(strLine);
	    }
	
	*/
	/*Ifilter.addAction("ACTION_TIME_TICK");
	Ifilter.addAction("ACTION_TIME_CHANGED");
	Ifilter.addAction("ACTION_TIMEZONE_CHANGED");
	Ifilter.addAction("ACTION_PACKAGE_ADDED");
	Ifilter.addAction("ACTION_DELETE");
	*/
	
	     final String TESTSTRING = new String("ACTION_TIME_TICK"); 
         
        FileOutputStream fOut = openFileOutput("samplefile.txt",MODE_WORLD_READABLE); 
        OutputStreamWriter osw = new OutputStreamWriter(fOut);  
        osw.write(TESTSTRING); 
        osw.flush(); 
        osw.close();

        FileInputStream fIn = openFileInput("samplefile.txt"); 
        InputStreamReader isr = new InputStreamReader(fIn); 
        char[] inputBuffer = new char[TESTSTRING.length()]; 
        isr.read(inputBuffer); 
        String readString = new String(inputBuffer); 
        boolean isTheSame = TESTSTRING.equals(readString); 
       
        Log.i("File Reading stuff", "success = " + isTheSame); 
	
        Ifilter.addAction(readString);
    	
     	registerReceiver(BR, Ifilter);
	
	}catch(Exception e)
	{
		Log.i("Error",e.getLocalizedMessage());
	}
	}
}	






