package edu.nyu.cs.omnidroid.ui;

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
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * This class will present an interface for the user to input data that they want the
 * <code>ActionThrowerActions</code> to access.
 * 
 * @author acase
 */
public class ActionThrowerData extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(this.getLocalClassName(), "onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.action_thrower_data);

    // Get Application passed
    Intent i = getIntent();
    final String actionName = i.getStringExtra(UGParser.KEY_InstanceName);
    //final String actionName = "SMS";
    
    Button save;
    final EditText data;

    save = (Button) findViewById(R.id.save);
    data = (EditText) findViewById(R.id.data);

    save.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        String appData = data.getText().toString();
        if (appData.length() > 0) {
          ContentValues values = new ContentValues();
          values.put("actionName", actionName);
          values.put("appData", appData);
          Uri uri = getContentResolver().insert(
              Uri.parse("content://edu.nyu.cs.omnidroid.core.cp/CP"), values);
          //Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();

          // Send the Intent to store the data
          Intent intent = new Intent();
          intent.putExtra("uri", uri.toString());
          sendBroadcast(intent);
        } else {
          Toast.makeText(getBaseContext(), "No Value", Toast.LENGTH_SHORT).show();
        }
      }
    });
   }
}