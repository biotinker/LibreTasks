package edu.nyu.cs.omnidroid.bkgservice;

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
import edu.nyu.cs.omnidroid.util.OmLogger;
import edu.nyu.cs.omnidroid.util.UGParser;

// TODO (pradeep): Document this class
public class BRService extends Service {
  /*
   * (non-Javadoc)
   * 
   * @see android.app.Service#onBind(android.content.Intent)
   */
  IntentFilter Ifilter = new IntentFilter();
  BroadcastReceiver BR = new BCReceiver();

  @Override
  public IBinder onBind(Intent arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  /** Called when the activity is first created. */
  @Override
  public void onCreate() {
    try {

      // Initializing the UGParser. To be deleted from this Code after Andrews Module
      UGParser ug = new UGParser(getApplicationContext());

      /* Check the User Config to start OmniDroid */
      //String Enabled = ug.readLine("Enabled");
      //if (Enabled.equalsIgnoreCase("True")) {
        Toast.makeText(getBaseContext(), "Starting OmniDroid", 5).show();

        // Get the User Instances in an Arraylist from the User Config
        ArrayList<HashMap<String, String>> UCRecords = ug.readRecords();
        Iterator<HashMap<String, String>> i = UCRecords.iterator();
        while (i.hasNext()) {
          HashMap<String, String> HM1 = i.next();
          // Configure the Intent Filter with the Events if Instance in enabled
          if (HM1.get("EnableInstance").equalsIgnoreCase("True"))
            {
            Ifilter.addAction(HM1.get("EventName"));
            Toast.makeText(getBaseContext(), "REGISTERING: "+HM1.get("EventName"), 5).show();
            
            }
        }
        registerReceiver(BR, Ifilter);
      //} else {
        //Toast.makeText(getBaseContext(), "Stopping OmniDroid", 5).show();
        //unregisterReceiver(BR);
      //}
    } catch (Exception e) {
      Log.i("BRService", e.getLocalizedMessage());
      Log.i("BRService", e.toString());
      OmLogger.write(getApplicationContext(), "Not able to Enable/Diable Omnidroid");
      // Logger.write("Unable to start BroadcastReceiver");
    }
  }

  @Override
public boolean stopService(Intent name) {
    Toast.makeText(getBaseContext(), "Stopping OmniDroid", 5).show();
	return super.stopService(name);
}

/*
   * (non-Javadoc)
   * 
   * @see android.app.Service#onDestroy()
   */
  @Override
  public void onDestroy() {
    Toast.makeText(getBaseContext(), "Stopping OmniDroid", 5).show();
    super.onDestroy();

  }
}
