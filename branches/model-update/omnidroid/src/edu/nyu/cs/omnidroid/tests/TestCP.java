/*******************************************************************************
 * Copyright 2009 OmniDroid - http://code.google.com/p/omnidroid 
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 *******************************************************************************/
package edu.nyu.cs.omnidroid.tests;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.core.CP;

/**
 * Activity used to test the contents of the OmniDroid Content Provider.
 *
 *
 */
public class TestCP extends Activity {
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.test_cp2);

    // Button and text boxes displayed on the UI.
    Button Store;
    final EditText dataTextField;
    final EditText idTextField;
    final EditText nameTextField;
    Button retrieveButton;
    Button getAllButton;

    Store = (Button) findViewById(R.id.test_cp2_store);
    nameTextField = (EditText) findViewById(R.id.test_cp2_iname);
    dataTextField = (EditText) findViewById(R.id.test_cp2_adata);
    retrieveButton = (Button) findViewById(R.id.test_cp2_retrieve);
    idTextField = (EditText) findViewById(R.id.test_cp2_id);
    getAllButton = (Button) findViewById(R.id.test_cp2_getAll);

    // Event listener for the Store button. Stores the values in the content provider
    Store.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        String iname = nameTextField.getText().toString();
        String adata = dataTextField.getText().toString();

        if (iname.length() > 0 && adata.length() > 0) {
          ContentValues values = new ContentValues();
          values.put("i_name", iname);
          values.put("a_data", adata);
          Uri uri = getContentResolver().insert(
              Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"), values);
          Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
          Log.d("Insert Complete", "This is a log");
          Toast.makeText(getBaseContext(), "Good Job", Toast.LENGTH_SHORT).show();

           //SENDING THE INTENT Intent intent = new Intent(text1); sendBroadcast(intent);
        } else {
          Toast.makeText(getBaseContext(), "No Value", Toast.LENGTH_SHORT).show();
        }
      }
    });
    // Event listener for the Retrieve button. Gets the values associated particular ID.
    retrieveButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        String id = idTextField.getText().toString();
        if (id.length() > 0) {
          Uri OmniURI = Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP");
          Cursor c = managedQuery(OmniURI, null, null, null, null);
          int new_id = Integer.parseInt(id);

          if (c.moveToPosition(new_id)) {
            Toast.makeText(
                getBaseContext(),
                c.getString(c.getColumnIndex(CP._ID)) + ", "
                    + c.getString(c.getColumnIndex(CP.INSTANCE_NAME)) + ", "
                    + c.getString(c.getColumnIndex(CP.ACTION_DATA)), Toast.LENGTH_LONG).show();
          }
          Toast.makeText(getBaseContext(), "Good Job 2", Toast.LENGTH_SHORT).show();
        } else

          Toast.makeText(getBaseContext(), "No Value", Toast.LENGTH_SHORT).show();

      }
    });
    // Event listener for the Get All button. Toasts all the records in the content provider.
    getAllButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        // Uri OmniURI = Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP/2");

        // ArrayList<String> al=cp.displayRecords(OmniURI.toString());

        Uri OmniURI = Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP");
        Cursor c = managedQuery(OmniURI, null, null, null, null);
        if (c.moveToFirst()) {
          do {
            Toast.makeText(
                getBaseContext(),
                c.getString(c.getColumnIndex(CP._ID)) + ", "
                    + c.getString(c.getColumnIndex(CP.INSTANCE_NAME)) + ", "
                    + c.getString(c.getColumnIndex(CP.ACTION_DATA)), Toast.LENGTH_LONG).show();
          } while (c.moveToNext());
        }

      }

    });

  }

}