package edu.nyu.cs.omnidroid.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Overview is an Android Activity that is the main UI Launcher for the OmniDroid Application.
 * 
 */
public class Overview extends Activity implements OnClickListener {
  private static final int MENU_ADD = 0;
  private static final int MENU_EDIT = 1;
  private static final int MENU_DELETE = 2;

  static private UGParser ug = new UGParser();

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(this.getLocalClassName(), "onCreate");
    super.onCreate(savedInstanceState);

    setContentView(R.layout.overview);

    // TODO: Connect buttons to the proper activities
    TableLayout table_layout = (TableLayout) findViewById(R.id.TableLayout01);

    // Add in each OmniHandler

    Collection<HashMap<String, String>> userConfigRecords = ug.readRecords(getApplicationContext());
    Iterator<HashMap<String, String>> i = userConfigRecords.iterator();
    while (i.hasNext()) {
      HashMap<String, String> HM1 = i.next();
      Log.i(this.getLocalClassName().toString(), "Found record");

      // Add a new row
      Log.i(this.getLocalClassName().toString(), "Adding a row");
      TableRow table_row = new TableRow(this.getApplicationContext());
      LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
      table_row.setLayoutParams(params);
      table_layout.addView(table_row);


      // Add a button to the row
      Log.i(this.getLocalClassName().toString(), "Adding a button");
      Button button = new Button(table_row.getContext());
      button.setLayoutParams(params);
      button.setClickable(true);
      button.setCursorVisible(true);
      table_row.addView(button);

      // Add an enable/disable checkbox to the row
      Log.i(this.getLocalClassName().toString(), "Adding a checkbox");
      CheckBox checkbox = new CheckBox(this.getApplicationContext());
      checkbox.setGravity(Gravity.RIGHT);
      if (HM1.get("EnableInstance").equalsIgnoreCase("True")) {
        Log.i(this.getLocalClassName().toString(), "Settign enabled");
        //checkbox.setEnabled(true);
      } else {
        Log.i(this.getLocalClassName().toString(), "Settign disabled");
        //checkbox.setEnabled(false);
      }
      table_row.addView(checkbox);

      Log.i(this.getLocalClassName().toString(), "Next.");
      // Add a context menu for the row
      registerForContextMenu(button);

      // Go to the next row
    }

  }

  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.add(0, MENU_EDIT, 0, "Edit");
    menu.add(0, MENU_DELETE, 0, "Delete");
  }

  public boolean onContextItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_EDIT:
      // TODO:
      Toast.makeText(this.getBaseContext(), "Edit OmniHandler Selected", 5).show();
      return true;
    case MENU_DELETE:
      // TODO:
      Toast.makeText(this.getBaseContext(), "Delete OmniHandler Selected", 5).show();
      return true;
    default:
      return super.onContextItemSelected(item);
    }
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
      AddOmniHandler();
      return true;
    }
    return false;
  }

  private void AddOmniHandler() {
    startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  public void onClick(View v) {
    // TODO:
    // Toast.makeText(this.getBaseContext(), "OmniHandler Selected", 5).show();
    // startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
  }

}