package edu.nyu.cs.omnidroid.ui;

import java.util.HashMap;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * This class will present an interface for the user to input data that they want the
 * <code>ActionThrowerActions</code> to access.
 * 
 * @author acase
 */
public class ActionThrowerData extends Activity {
  private String eventApp;
  private String eventName;
  private String throwerApp;
  private String throwerName;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(this.getLocalClassName(), "onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.action_thrower_data);

    // Get Application passed
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    if (extras != null) {
      eventApp = extras.getString(AGParser.KEY_APPLICATION);
      eventName = extras.getString(UGParser.KEY_EventApp);
      throwerApp = extras.getString(UGParser.KEY_ActionApp);
      throwerName = extras.getString(UGParser.KEY_InstanceName);
    } else {
      // TODO (acase): Throw exception
    }
    // final String actionName = "SMS";

    Button save;
    final EditText appData;
    final EditText instanceName;

    save = (Button) findViewById(R.id.save);
    appData = (EditText) findViewById(R.id.data);
    instanceName = (EditText) findViewById(R.id.Iname);

    save.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        String iName = instanceName.getText().toString();
        String aData = appData.getText().toString();
        // TODO (acase): Check for errors
        // TODO (acase): Pass to next page of UI
        // TODO (acase): Add it to the config
        if (iName.length() > 0 && aData.length() > 0) {
          ContentValues values = new ContentValues();
          values.put("i_name", iName);
          values.put("a_data", aData);
          Uri uri = getContentResolver().insert(
              Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"), values);
          HashMap<String, String> HM = new HashMap<String, String>();
          HM.put("InstanceName", aData);
          HM.put("EventName", eventName);  // TODO: null
          HM.put("EventApp", eventApp);
          // TODO: get these from the user
          HM.put("FilterType", "S_PhoneNum");
          HM.put("FilterData", "212-555-1234");
          HM.put("ActionName", throwerName);  // TODO: null
          HM.put("ActionApp", throwerApp);  // TODO: null
          HM.put("ActionData", uri.toString());
          HM.put("EnableInstance", "True");

          // Initialize our AGParser
          UGParser ug = new UGParser(getApplicationContext());
          ug.writeRecord(HM);

          Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
          Log.d("Insert Complete", "This is a log");
          Toast.makeText(getBaseContext(), "Good Job", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(getBaseContext(), "No Value", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }
}