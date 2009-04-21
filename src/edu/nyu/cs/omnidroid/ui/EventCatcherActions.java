package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.StringMap;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Presents a list of possible actions that the selected <code>EventCatcher</code> could have
 * performed that we want to hook an OmniHandler onto.
 * 
 * @author acase
 */
public class EventCatcherActions extends ListActivity {
  private static AGParser ag;
  private String appName = null;

  // Standard Menu options (Android menus require int, so no enums)
  private static final int MENU_HELP = 0;
private static final int ADD_RESULT = 1;

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
    appName = extras.getString(AGParser.KEY_APPLICATION);

    // Getting the Events from AppConfig
    ArrayList<HashMap<String, String>> eventList = ag.readEvents(appName);
    Iterator<HashMap<String, String>> i1 = eventList.iterator();
    //ArrayList<StringMap> eventList = ag.readEvents(appName);
    //Iterator<StringMap> i1 = eventList.iterator();
    
    //ArrayList<String> values = new ArrayList<String>();
    ArrayList<TextView> values = new ArrayList<TextView>();
    ArrayList<StringMap> stringvalues = new ArrayList<StringMap>();
    while (i1.hasNext()) {
      // FIXME(acase): Add key also
      
      HashMap<String, String> HM1 = i1.next();
      HM1.toString();
      String splits[] = HM1.toString().split(",");
      for (int cnt=0; cnt < splits.length; cnt++) {
        String pairs[] = splits[cnt].split("=");
        String key = pairs[0].replaceFirst("\\{","");
        String entry = pairs[1].replaceFirst("\\}","");
        //TextView tv = new TextView(this);
        //tv.setText(entry);
        //tv.setTag(key);
        //values.add(tv);
        stringvalues.add(new StringMap(key,entry));
      }
      //key = splits[0];
      //value = splits[1];
      
      //StringMap<String, String> HM1 = i1.next();
      //Toast.makeText(getBaseContext(), HM1.toString(), 5).show();
      // TODO (acase): We need a better way then accessing a hashmap
      //TextView tv = new TextView(this);
      //tv.setText(value[cnt]);
      //tv.setTag(key[cnt]);
      //values.add(HM1.values());
      //values.add(tv);
    }

    ArrayAdapter<StringMap> arrayAdapter = new ArrayAdapter<StringMap>(this,
        android.R.layout.simple_list_item_1, stringvalues);
    setListAdapter(arrayAdapter);
    getListView().setTextFilterEnabled(true);

    Log.i(this.getLocalClassName(), "onCreate exit");
  }

  /*
   * (non-Javadoc)
   * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
   */
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    StringMap sm = (StringMap)l.getAdapter().getItem(position);
    
    Intent i = new Intent();
    // TODO(acase): Use Filters
    //i.setClass(this.getApplicationContext(), Filters.class);
    i.setClass(this.getApplicationContext(), ActionThrower.class);
    String eName = sm.getKey();
    i.putExtra(AGParser.KEY_APPLICATION, appName);
    i.putExtra(UGParser.KEY_EventName, eName);
    startActivityForResult(i, ADD_RESULT);
  }

  /*
   * (non-Javadoc)
   * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
   */
  protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ADD_RESULT:
			setResult(resultCode, data);
			  finish();	  
          break;
		}
  }

  /**
   * Creates the options menu items
   * 
   * @param menu
   *            - the options menu to create
   */
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, MENU_HELP, 0, R.string.help).setIcon(
        android.R.drawable.ic_menu_help);
    return true;
  }

  /**
   * Handles menu item selections
   */
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_HELP:
      Help();
      return true;
    }
    return false;
  }

  /**
   * Call our Help dialog
   */
  private void Help() {
    // TODO (acase): Create a help dialog for this activity
  }

}