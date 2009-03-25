package edu.nyu.cs.omnidroid.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import edu.nyu.cs.omnidroid.R;

public class OverviewActivity extends Activity {
  private static final int MENU_ADD = 0;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(this.getLocalClassName(), "onCreate");
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.overview_activity);
  }

  public void onCreateContextMenu() {
    // TODO: Create context menu's for the buttons
  }

  /* Creates the menu items */
  public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(0, MENU_ADD, 0, "Add OmniHandler");
      return true;
  }

  /* Handles item selections */
  public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case MENU_ADD:
          RunEventAppListActivity();
          return true;
      }
      return false;
  }
  

  private void RunEventAppListActivity() {
//    EventAppListActivity applist = new EventAppListActivity();
//    applist.addContentView(view, params)
    // TODO: We shouldn't be calling intents for this, there
    //       has to be a way to just start up the next activity page
    startActivity(new Intent(Intent.ACTION_EDIT));
  }

  // TODO: Pull this from the UsrConfigFile
  static final String[] UserConfigActions = new String[] {
    "AutoReplyWhenSilent", "", "" };
  // TODO: Pull this from the UsrConfigFile
  static final boolean[] UserConfigActionsEnabled = {
    true, false, true, true };
  
}