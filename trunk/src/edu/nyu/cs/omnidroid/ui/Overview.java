package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Overview is the main UI Launcher for the OmniDroid Application. It presents all the current
 * OmniHandlers as well as a way to add/delete/edit them.
 * 
 * @author - acase
 * 
 */
public class Overview extends ListActivity {
  // Menu Options of the Context variety(Android menus require int)
  private static final int MENU_EDIT = 0;
  private static final int MENU_DELETE = 1;
  // Menu Options of the Standard variety (Android menus require int)
  private static final int MENU_ADD = 2;
  private static final int MENU_SETTINGS = 3;
  private static final int MENU_HELP = 4;
  private static final int MENU_TESTS = 5;
  private static final int MENU_ABOUT = 6;

  // Pull data from the UGParser
  UGParser ug;

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    // Debug.startMethodTracing("OmniDroid");
    // Create our Activity
    super.onCreate(savedInstanceState);
    update();
  }

  /**
   * Update the list of OmniHandlers as well as any other things that may need to get updated after
   * changes occur.
   */
  private void update() {
    // Get a list of our current OmniHandlers
    ug = new UGParser(getApplicationContext());
    ArrayList<HashMap<String, String>> userConfigRecords = ug.readRecords();
    ArrayList<String> items = new ArrayList<String>();

    // Add current OmniHandlers to our list
    for (HashMap<String, String> hm : userConfigRecords) {
      items.add(hm.get((String) UGParser.KEY_INSTANCE_NAME));
      
    }

    // ListView clv = new CheckedListView();
    // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
    // android.R.layout.simple_list_item_1, items);
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_multiple_choice, items);

    final ListView lv = getListView();

    // ListView lv = (ListView) findViewById(R.id.checkable_list);
    // lv.setAdapter(arrayAdapter);
    setListAdapter(arrayAdapter);
    registerForContextMenu(lv);
    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    lv.setItemsCanFocus(false);

    // Update checkboxes
    for (String omniHandlerName : items) {
      HashMap<String, String> s = ug.readRecord(omniHandlerName);
      String enabled = s.get(UGParser.KEY_ENABLE_INSTANCE);
      if (enabled.compareToIgnoreCase(UGParser.TRUE) == 0) {
        lv.setItemChecked(items.indexOf(omniHandlerName), true);
      } else {
        lv.setItemChecked(items.indexOf(omniHandlerName), false);
      }
    }
    
    // If we don't have any OmniHandlers, throw up our Help Dialog
    if (userConfigRecords.size() == 0) {
      welcome();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onDestroy()
   */
  public void onDestroy() {
    super.onDestroy();
    // Debug.stopMethodTracing();
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View,
   * android.view.ContextMenu.ContextMenuInfo)
   */
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    //menu.add(0, MENU_EDIT, 0, R.string.edit);
    menu.add(0, MENU_DELETE, 0, R.string.del);
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
   */
  public boolean onContextItemSelected(MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

    switch (item.getItemId()) {
    case MENU_EDIT:
      editHandler(info.id);
      return true;
    case MENU_DELETE:
      deleteHandler(info.id);
      return true;
    default:
      return super.onContextItemSelected(item);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int,
   * long)
   */
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    String instanceName = (String) l.getAdapter().getItem(position);
    HashMap<String, String> HM = ug.readRecord(instanceName);

    SparseBooleanArray checked = l.getCheckedItemPositions();
    if (checked.get(position)) {
      Toast.makeText(this.getBaseContext(), "Enabling " + instanceName, 5).show();
      HM.put(UGParser.KEY_ENABLE_INSTANCE, "true");
    } else {
      Toast.makeText(this.getBaseContext(), "Disabling " + instanceName, 5).show();
      HM.put(UGParser.KEY_ENABLE_INSTANCE, "false");
    }
    ug.updateRecord(HM);

    // Restart the service
    Intent i = new Intent();
    i.setAction("OmniRestart");
    sendBroadcast(i);
  }

  /**
   * @param id
   *          of the menu item
   */
  private void deleteHandler(long id) {
    // FIXME (acase): Delete from CP
    String instanceName = (String) this.getListAdapter().getItem((int) id);
    HashMap<String,String> instance = ug.readRecord(instanceName);

    // Delete from User Config
    ug.deleteRecord(instance);
    update();
    Toast.makeText(this.getBaseContext(), "Deleted " + instanceName, Toast.LENGTH_LONG).show();
  }

  /**
   * @param id
   *          of the menu item
   */
  private void editHandler(long id) {
    //String instanceName = (String) this.getListAdapter().getItem((int) id);
    //HashMap<String,String> instance = ug.readRecord(instanceName);
    //OmniHandler oh = new OmniHandler(instance);
    
    // TODO (acase): Call next activity
    Toast.makeText(this.getBaseContext(), "Edit OmniHandler Selected", 5).show();
  }

  /**
   * Creates the options menu items
   * 
   * @param menu
   *          - the options menu to create
   */
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, MENU_ADD, 0, R.string.add_omnihandler).setIcon(android.R.drawable.ic_menu_add);
    menu.add(0, MENU_SETTINGS, 0, R.string.settings)
        .setIcon(android.R.drawable.ic_menu_preferences);
    menu.add(0, MENU_HELP, 0, R.string.help).setIcon(android.R.drawable.ic_menu_help);
    menu.add(0, MENU_ABOUT, 0, R.string.about).setIcon(android.R.drawable.ic_menu_info_details);
    menu.add(0, MENU_TESTS, 0, R.string.tests).setIcon(android.R.drawable.ic_menu_manage);
    return true;
  }

  /**
   * Handles menu item selections
   */
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_ADD:
      addOmniHandler();
      return true;
    case MENU_SETTINGS:
      settings();
      return true;
    case MENU_HELP:
      help();
      return true;
    case MENU_ABOUT:
      about();
      return true;
    case MENU_TESTS:
      runTests();
      return true;
    }
    return false;
  }

  /**
   * Presents the TestApp activity to run any available tests.
   */
  private void runTests() {
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), edu.nyu.cs.omnidroid.tests.TestApp.class);
    startActivity(i);
  }

  /**
   * Add a new OmniHandler to OmniDroid
   */
  private void addOmniHandler() {
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), edu.nyu.cs.omnidroid.ui.Event.class);
    startActivityForResult(i, Constants.RESULT_ADD_OMNIHANDER);
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
   */
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
    case Constants.RESULT_ADD_OMNIHANDER:
      switch (resultCode) {
      case Constants.RESULT_SUCCESS:
        update();
        break;
      }
      break;
    }
  }

  /**
   * Call our Settings Activity
   */
  private void settings() {
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), edu.nyu.cs.omnidroid.ui.Settings.class);
    startActivityForResult(i, Constants.RESULT_EDIT_SETTINGS);
  }

  /**
   * Call our Welcome dialog
   */
  private void welcome() {
    Builder welcome = new AlertDialog.Builder(this);
    // TODO(acase): Move to some kind of resource
    // String welcome_msg = this.getResources().getString(R.string.welcome_overview);
    String welcome_msg = "OmniDroid provides a way to automate tasks on your device by "
        + "giving you an interface to customize and automate how applications interact "
        + "with each other.\n<br/>"
        + "<b>To get started, select <i>Add OmniHandler</i> from the <i>Menu</i></b>.\n<br/>"
        + "For more information select <i>Help</i> from the <i>Menu</i>.";
    welcome.setTitle(R.string.welcome);
    welcome.setIcon(R.drawable.icon);
    welcome.setMessage(Html.fromHtml(welcome_msg));
    welcome.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
      }
    });
    welcome.show();
  }

  /**
   * Call our Help dialog
   */
  private void help() {
    Builder help = new AlertDialog.Builder(this);
    // TODO(acase): Move to some kind of resource
    // String help_msg = this.getResources().getString(R.string.help_overview);
    String help_msg = "OmniDroid provides a set of OmniHandlers that "
        + "allow you to customize and automate how applications interact with each other.\n<br/>"
        + "This page provides a list of OmniHandlers that are "
        + "stored in OmniDroid and a checkbox to enable/disable them individually.\n<br/>"
        + "Possible Actions:\n<br/>"
        + "&nbsp;&nbsp;&nbsp;Add an OmniHandler by selecting the <i>Add OmniHandler</i> "
        + "option from the <i>Menu</i>.\n<br/>"
        + "&nbsp;&nbsp;&nbsp;Delete an OmniHandler by long-clicking it and selecting "
        + "the Delete option.\n<br/>"
        //+ "&nbsp;&nbsp;&nbsp;Edit an OmniHandler by long-clicking it and selecting "
        //+ "the Edit option.\n<br/>"
        + "&nbsp;&nbsp;&nbsp;Enable/Disable an OmniHandler by selecting its checkbox.\n<br/>"
        + "For more help, see our webpage: "
        + "&nbsp;&nbsp;&nbsp;<a href=\"http://omni-droid.com/help\">http://omni-droid.com/help</a>\n<br/>";
    help.setTitle(R.string.help);
    help.setIcon(android.R.drawable.ic_menu_help);
    // TODO(acase): Link this to the webbrowser
    // Spanned spanned = Html.fromHtml(help_msg);
    // spanned.toString()
    // linked = Linkify.addLinks(spanned, Linkify.WEB_URLS);
    // help.setMessage(Linkify.addLinks(Spannable(Html.fromHtml(help_msg)),Linkify.WEB_URLS));
    // help.setMessage(Linkify.addLinks(help_msg, Linkify.WEB_URLS));
    help.setMessage(Html.fromHtml(help_msg));
    help.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
      }
    });
    help.show();
  }

  /**
   * Call our About dialog
   */
  private void about() {
    Builder about = new AlertDialog.Builder(this);

    // TODO(acase): Move to some kind of resource
    String about_msg = "OmniDroid is brought to you in part by the letter G and the number 1.\n<br/>"
        + "Hacking Contributions:<br/>"
        + "&nbsp;&nbsp;&nbsp;Andrew Case<br/>"
        + "&nbsp;&nbsp;&nbsp;Sucharita Gaat<br/>"
        + "&nbsp;&nbsp;&nbsp;Rajiv Sharma<br/>"
        + "&nbsp;&nbsp;&nbsp;Pradeep Harish Varma<br/>"
        + "\n"
        + "Donations from:<br/>"
        + "&nbsp;&nbsp;&nbsp;Google Inc.<br/>"
        + "&nbsp;&nbsp;&nbsp;New York University<br/>"
        + "\n"
        + "License: Apache License (2.0)\n<br/>"
        + "Website: <a href=\"http://omni-droid.com\">http://omni-droid.com</a><br/>";
    String title = this.getResources().getString(R.string.about)
        + " " + this.getResources().getString(R.string.app_name)
        + " v" + this.getResources().getString(R.string.version);
    about.setTitle(title);
    about.setIcon(R.drawable.icon);
    about.setMessage(Html.fromHtml(about_msg));
    about.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
      }
    });
    about.show();
  }
}
