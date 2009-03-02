package com.CIntents;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
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
	
	protected void onPause() {
		// TODO Auto-generated method stub
		unregisterReceiver(BR);	
		super.onPause();
}

@Override
	protected void onResume() {
		try
	{
	super.onResume();
	/*Ifilter.addAction("ACTION_TIME_TICK");
	Ifilter.addAction("ACTION_TIME_CHANGED");
	Ifilter.addAction("ACTION_TIMEZONE_CHANGED");
	Ifilter.addAction("ACTION_PACKAGE_ADDED");
	Ifilter.addAction("ACTION_DELETE");
	*/
	
	    final String TESTSTRING = new String("ACTION_TIME_TICK"); 
        FileOutputStream fOut = openFileOutput("UserConfig.txt",MODE_WORLD_READABLE); 
        OutputStreamWriter osw = new OutputStreamWriter(fOut);  
        osw.write(TESTSTRING); 
        osw.flush(); 
        osw.close();

        /*FileInputStream fIn = openFileInput("samplefile.txt"); 
        InputStreamReader isr = new InputStreamReader(fIn); 
        char[] inputBuffer = new char[]; 
        isr.read(inputBuffer); 
        String line = new String(inputBuffer); 
        */
        
        FileInputStream FIn = openFileInput("UserConfig.txt"); 
        BufferedInputStream bis = new BufferedInputStream(FIn); 
        DataInputStream dis = new DataInputStream(bis);
        String line;
        
        while((line=dis.readLine())!=null)
        {                
        Ifilter.addAction(line);
        }
     	registerReceiver(BR, Ifilter);
	
	}catch(Exception e)
	{
		Log.i("Error",e.getLocalizedMessage());
	}
	}
}	






