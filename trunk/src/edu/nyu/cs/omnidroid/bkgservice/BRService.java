package edu.nyu.cs.omnidroid.bkgservice;
import edu.nyu.cs.omnidroid.util.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
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
    public void onCreate() {
    	try
    	{
    	// To be deleted from this Code
    	final String TESTSTRING = new String("SMS,SMS_RECEIVED,Evan,SMS,SMS_SEND,URI"); 
        FileOutputStream fOut = openFileOutput("UserConfig.txt",MODE_WORLD_READABLE); 
        OutputStreamWriter osw = new OutputStreamWriter(fOut);  
        osw.write(TESTSTRING); 
        osw.flush(); 
        osw.close();
        
        FileInputStream FIn = openFileInput("UserConfig.txt"); 
        BufferedInputStream bis = new BufferedInputStream(FIn); 
        DataInputStream dis = new DataInputStream(bis);
        String line;
        
        while((line=dis.readLine())!=null)
        {                
        	String[] parts=line.split(",");
        	Log.i("error",parts[1].toString());
        	Ifilter.addAction(parts[1].toString());
        }
     	registerReceiver(BR, Ifilter);
     	OmLogger.write(this,"3.Success");
     	OmLogger.read(this);
     	
	}catch(FileNotFoundException fe)
	{
		Log.i("File Not Found",fe.getLocalizedMessage());
	}
    catch(Exception e)
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
		//unregisterReceiver(BR);
		super.onDestroy();
	}
}
