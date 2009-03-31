package edu.nyu.cs.omnidroid.tests;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.UserConfig;

/**
 * Test Application to Verify Configuration File Functionality
 * 
 */
public class TestConfigFile extends Activity {
//public class Overview extends Activity implements OnClickListener {
  private static final int MENU_ADD = 0;
  private static final int MENU_EDIT = 1;
  private static final int MENU_DELETE = 2;

  // Configuration file locations for the TM/RM.
  private static String userConfigFile;

  // TODO: Pull this from the UsrConfigFile
  static final String[] UserConfigActions = new String[] { "AutoReplyWhenSilent", "", "" };
  // TODO: Pull this from the UsrConfigFile
  static final boolean[] UserConfigActionsEnabled = { true, false, true, true };

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(this.getLocalClassName(), "onCreate");
    super.onCreate(savedInstanceState);

    setContentView(R.layout.test_config);
    registerForContextMenu(this.findViewById(R.id.example1));
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
    initConfig();
    /*
    final String TESTSTRING = new String("SMS,SMS_RECEIVED,Evan,SMS,SMS_SEND,URI"); 
    FileOutputStream fOut = openFileOutput("UserConfig.txt",MODE_WORLD_READABLE); 
    OutputStreamWriter osw = new OutputStreamWriter(fOut);  
    osw.write(TESTSTRING); 
    osw.flush(); 
    osw.close();
    
    FileInputStream FIn = openFileInput("UserConfig.txt"); 
    BufferedInputStream bis = new BufferedInputStream(FIn); 
    DataInputStream dis = new DataInputStream(bis);
    String line;
    
    while((line=dis.readLine())!=null)
    {                
      String[] parts=line.split(",");
      Log.i("error",parts[1].toString());
      Ifilter.addAction(parts[1].toString());
    }
*/
  }

  /**
   * Initialize the server configuration objects
   */
  private static void initConfig() {
    try {
      UserConfig.getInstance().init(userConfigFile);
    } catch (IOException e) {
      // TODO (acase): Use OmnidroidExceptions
      System.err.println("Unable to load user config properties file.");
      System.err.println("  " + e.getMessage());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  public void onClick(View v) {
    // TODO:
    //Toast.makeText(this.getBaseContext(), "OmniHandler Selected", 5).show();
    //startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
  }

}