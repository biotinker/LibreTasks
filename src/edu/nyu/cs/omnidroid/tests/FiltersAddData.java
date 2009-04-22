package edu.nyu.cs.omnidroid.tests;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Activity used to present a list of filters to apply to this OmniHandler. Filters allow the user
 * to only apply this OmniHandler if the filter data matches the data in provided by the Event
 * that was caught.
 * 
 * @author acase
 * 
 */
public class FiltersAddData extends Activity implements OnClickListener {

  // Standard Menu options (Android menus require int, so no enums)
  private static final int MENU_HELP = 0;

  // User Selected Data
  String appName = null;
  String eventName = null;
  String filterType = null;
  Button saveBtn = null;
  EditText filterData = null;

  /*
   * (non-Javadoc)
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.filter_add_data);

    // See what application we want to handle events for from the
    // intent data passed to us.
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    appName = extras.getString(AGParser.KEY_APPLICATION);
    eventName = extras.getString(UGParser.KEY_EventName);
    filterType = extras.getString(UGParser.KEY_FilterType);

    // Connect to our UI layout
    saveBtn = (Button) findViewById(R.id.save);
    filterData = (EditText) findViewById(R.id.data);
    
    // Listen for the save button click
    saveBtn.setOnClickListener(this);

    Log.i(this.getLocalClassName(), "onCreate exit");
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

public void onClick(View v) {
  // Pass the data back to Filters
  //AutoCompleteTextView data = (AutoCompleteTextView) findViewById(R.id.data);
  EditText data = (EditText) findViewById(R.id.data);
  String filterData = data.getText().toString();
  
  Intent i = new Intent();
  i.setClass(this.getApplicationContext(), Filters.class);
  i.putExtra(AGParser.KEY_APPLICATION, appName);
  i.putExtra(UGParser.KEY_EventName, eventName);
  i.putExtra(UGParser.KEY_FilterType, filterType);
  i.putExtra(UGParser.KEY_FilterData, filterData);
  startActivity(i);
  finish();
}

}