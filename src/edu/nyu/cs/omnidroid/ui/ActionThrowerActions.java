package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Presents a list of possible actions that the selected <code>ActionThrower</code> could 
 * throw as it's action to perform for that OmniHandler.
 *
 * @author acase
 */
public class ActionThrowerActions extends ListActivity {
  private static AGParser ag;
  private String eventApp = null;
  private String eventName = null;
  private String throwerApp = null;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    // Android Boilerplate
    super.onCreate(savedInstanceState);

    // Initialize our AGParser
    ag = new AGParser(getApplicationContext());

    // See what application we want to handle events for from the
    // intent data passed to us.
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    if (extras != null) {
      eventApp = extras.getString(AGParser.KEY_APPLICATION);
      eventName = extras.getString(UGParser.KEY_EventApp);
      throwerApp = extras.getString(UGParser.KEY_ActionApp);
    } else {
      // TODO (acase): Throw exception
    }

    // Getting the Events from AppConfig
    ArrayList<HashMap<String, String>> eventList = ag.readEvents(throwerApp);
    Iterator<HashMap<String, String>> i1 = eventList.iterator();    
    ArrayList<String> values = new ArrayList<String>();
    while (i1.hasNext()) {
      // TODO (acase): We need a better way then accessing a hashmap
      HashMap<String, String> HM1 = i1.next();
      Toast.makeText(getBaseContext(), HM1.toString(), 5).show();
      values.addAll(HM1.values());
    }

    // Make sure we have an appropriate selection
    if (values == null)
    {
      // TODO (acase): Throw exception
    }

    // Build our list of Actions  
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, values);
    setListAdapter(arrayAdapter);
    getListView().setTextFilterEnabled(true);
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Intent i = new Intent();
    // TODO (acase): Choose Filter page
    i.setClass(this.getApplicationContext(), edu.nyu.cs.omnidroid.ui.ActionThrowerData.class);
    TextView tv = (TextView) v;
    // EventCatcherApp
    i.putExtra(AGParser.KEY_APPLICATION, eventApp);
    // EventCatcherAction
    i.putExtra(UGParser.KEY_EventName, eventName);
    // ActionThrowerApp
    i.putExtra(UGParser.KEY_ActionApp, throwerApp);
    // ActionThrowerAction
    i.putExtra(UGParser.KEY_ActionName, tv.getText());
    startActivity(i);
  }

}