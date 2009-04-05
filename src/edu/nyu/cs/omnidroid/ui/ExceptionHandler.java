package edu.nyu.cs.omnidroid.ui;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.OmLogger;
import edu.nyu.cs.omnidroid.util.UserConfig;

/**
 * Test Application to Verify Configuration File Functionality
 * 
 */
public class ExceptionHandler extends Activity {

  //public class Overview extends Activity implements OnClickListener {
  private static final int MENU_ADD = 0;
  private static final int MENU_EDIT = 1;
  private static final int MENU_DELETE = 2;


  /** Called when the activity is first created. */
  @Override
  // FIXME (acase): We can't handle exceptions this way, we would need to have an exception
  //                catching service that all it does is listen for exceptions.  This really
  //                isn't very feasible.
  //public void onCreate(Bundle savedInstanceState) throws OmnidroidException {
  public void onCreate(Bundle savedInstanceState) {
    Log.i(this.getLocalClassName(), "onCreate");
    super.onCreate(savedInstanceState);

    setContentView(R.layout.test_config);
    //registerForContextMenu(this.findViewById(R.id.example1));
  }

  /*
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.add(0, MENU_EDIT, 0, "Edit");
    menu.add(0, MENU_DELETE, 0, "Delete");
  }
  */
  /*
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
  */
  
  /* Creates the menu items */
  /*
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, MENU_ADD, 0, "Add OmniHandler").setIcon(android.R.drawable.ic_menu_add);
    ;
    return true;
  }
  */
  
  /* Handles item selections */
  /*
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_ADD:
      AddOmniHandler();
      return true;
    }
    return false;
  }
   */
  /*
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
  }
   */


  /*
   * (non-Javadoc)
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  public void onClick(View v) {
  }

/*
  @Override
  public void onReceive(Context context, Intent intent)
      {
    try{
    Toast.makeText(context,intent.getAction(),5).show();
      
    Log.i("Received Intent", intent.getAction());
    }catch(Exception e)
    {
      Log.i("Exception in Intent",e.getLocalizedMessage());
      OmLogger.write(context,"Unable to execute required action");
    }
    }
      }
*/

}