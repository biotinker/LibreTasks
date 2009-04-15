package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import edu.nyu.cs.omnidroid.util.AGParser;

/**
 * Activity used to present a list of Applications which are registered in Omnidroid configuration.
 * The user can then select an application whose events we want to catch using our Omnihandler.
 * 
 * @author acase
 * 
 */
public class EventCatcher extends ListActivity {

  /**
   * Creates the activity
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    AGParser ag = new AGParser(getApplicationContext());
    super.onCreate(savedInstanceState);

    // TODO: Filter by only apps that contain events
    // Getting the Events from AppConfig
    ArrayList<String> appNames;
    appNames = ag.readLines(AGParser.KEY_APPLICATION);

    // Populate our layout with applications
    setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, appNames));
    getListView().setTextFilterEnabled(true);

    Log.i(this.getLocalClassName(), "onCreate exit");
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    TextView tv = (TextView) v;
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), edu.nyu.cs.omnidroid.ui.EventCatcherActions.class);
    i.putExtra(AGParser.KEY_APPLICATION, tv.getText());
    startActivity(i);
  }

}