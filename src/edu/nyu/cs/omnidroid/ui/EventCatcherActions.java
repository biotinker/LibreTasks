package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;
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
  private static final String TAG = EventCatcherActions.class.getSimpleName();
  private static AGParser ag;

  // TODO: Pull this from the AppConfig
  // TODO: Filter by only apps that contain actions
  private static final String[] EVENTS = new String[] { "Email Received", "Email was Deleted",
      "Email was Moved" };

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, EVENTS));
    getListView().setTextFilterEnabled(true);

    String appName = null;
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    if (extras != null) {
      appName = extras.getString(EventCatcher.KEY_APPLICATION);
    } else {
       //TODO (acase): Throw exception
    }


    // Set the layout for this activity. You can find it in res/layout/
    ArrayList<HashMap<String, String>> eventNames;
    eventNames = ag.readEvents(appName);

    //Getting the Events from AppConfig
        ArrayList<HashMap<String, String>> eArrayList = ag.readEvents("SMS");
        Iterator<HashMap<String, String>> i1 = eArrayList.iterator();
        while (i1.hasNext()) {
          HashMap<String, String> HM1 = i1.next();
          Toast.makeText(getBaseContext(), HM1.toString(), 5).show();
        }
    
    setListAdapter(new ArrayAdapter<HashMap<String,String>>(this,
        android.R.layout.simple_list_item_1, eventNames));
    getListView().setTextFilterEnabled(true);

    Log.i(this.getLocalClassName(), "onCreate exit");
  }

}