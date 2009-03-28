package edu.nyu.cs.omnidroid.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import edu.nyu.cs.omnidroid.R;

public class Overview extends Activity {
  private static final int MENU_ADD = 0;
  private static final int MENU_EDIT = 1;
  private static final int MENU_DELETE = 2;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(this.getLocalClassName(), "onCreate");
    super.onCreate(savedInstanceState);

    setContentView(R.layout.overview);
    registerForContextMenu(this.findViewById(R.id.example1));
  }
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.add(0, MENU_EDIT, 0, "Edit");
    menu.add(0, MENU_DELETE, 0, "Delete");
  }

  public boolean onContextItemSelected(MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    switch (item.getItemId()) {
    case MENU_EDIT:
      editOmniHandler(info.id);
      return true;
    case MENU_DELETE:
      deleteOmniHandler(info.id);
      return true;
    default:
      return super.onContextItemSelected(item);
    }
  }

  private void deleteOmniHandler(long id) {
    Toast.makeText(super.getApplicationContext(), "Delete OmniHandler Selected", 1);    
  }

  private void editOmniHandler(long id) {
    Toast.makeText(super.getApplicationContext(), "Edit OmniHandler Selected", 1);    
  }

  /* Creates the menu items */
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, MENU_ADD, 0, "Add OmniHandler").setIcon(android.R.drawable.ic_menu_add);
    ;
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
    startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
  }

  // TODO: Pull this from the UsrConfigFile
  static final String[] UserConfigActions = new String[] { "AutoReplyWhenSilent", "", "" };
  // TODO: Pull this from the UsrConfigFile
  static final boolean[] UserConfigActionsEnabled = { true, false, true, true };

}