package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.util.AGParser;

/**
 * Presents a list of possible actions that the selected <code>EventCatcher</code> could have
 * performed that we want to hook an OmniHandler onto.
 * 
 */
public class EventCatcherActions extends ListActivity {
  private static AGParser ag;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    // Android Boilerplate
    super.onCreate(savedInstanceState);

    // Initialize our AGParser
    ag = new AGParser(getApplicationContext());

    // See what application we want to handle events for from the
    // intent data passed to us.
    String appName = null;
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    if (extras != null) {
      appName = extras.getString(AGParser.KEY_APPLICATION);
    } else {
      // TODO (acase): Throw exception
    }

    

    // Getting the Events from AppConfig
    ArrayList<HashMap<String, String>> eventList = ag.readEvents(appName);
    Iterator<HashMap<String, String>> i1 = eventList.iterator();
    
    ArrayList<String> values = new ArrayList<String>();
    while (i1.hasNext()) {
      HashMap<String, String> HM1 = i1.next();
      Toast.makeText(getBaseContext(), HM1.toString(), 5).show();
      // TODO (acase): We need a better way then accessing a hashmap
      values.addAll(HM1.values());
      //values.add(HM1.get("SMS_Received"));
      //values.add(HM1.get("SMS_Sent"));

    }
    if (values == null)
    {
      // TODO (acase): Throw exception
    }
    Iterator<String> iter = values.iterator();
    while (iter.hasNext()) {
      String eventName = iter.next();
      Toast.makeText(getBaseContext(), "List includes = " + eventName, 4).show();
    }
/*
    setListAdapter(new ArrayAdapter<HashMap<String, String>>(this,
        android.R.layout.simple_list_item_1, eventList));
*/
    ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, values);
    setListAdapter(arrayAdapter);
    getListView().setTextFilterEnabled(true);

    Log.i(this.getLocalClassName(), "onCreate exit");
  }

}