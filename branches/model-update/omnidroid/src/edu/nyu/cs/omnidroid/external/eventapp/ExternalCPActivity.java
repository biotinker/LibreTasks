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
package edu.nyu.cs.omnidroid.external.eventapp;

import edu.nyu.cs.omnidroid.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity used to create the UI screen for the External Event Application.
 * 
 * 
 */
public class ExternalCPActivity extends Activity {
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.external_event_cp);
    // UI buttons and text boxes

    Button Store;
    final EditText text;
    final EditText ID;
    final EditText Sname;
    final EditText Sphno;
    Button Retrieve;
    Button GetAll;

    Store = (Button) findViewById(R.id.external_event_cp_store);
    Sname = (EditText) findViewById(R.id.external_event_cp_sname);
    Sphno = (EditText) findViewById(R.id.external_event_cp_sphno);
    text = (EditText) findViewById(R.id.external_event_cp_text);
    Retrieve = (Button) findViewById(R.id.external_event_cp_retrieve);
    ID = (EditText) findViewById(R.id.external_event_cp_id);
    GetAll = (Button) findViewById(R.id.external_event_cp_getAll);
    // Event listener for the Store button. Stores the value in the external event application
    // content provider.
    Store.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        String text1 = text.getText().toString();
        String sname = Sname.getText().toString();
        String sphno = Sphno.getText().toString();

        if (text1.length() > 0 && sname.length() > 0 && sphno.length() > 0) {
          ContentValues values = new ContentValues();
          values.put("s_name", sname);
          values.put("s_ph_no", sphno);
          values.put("text", text1);
          Uri uri = getContentResolver().insert(
              Uri.parse("content://" + ExternalCP.CP_Name + "/CP"), values);
          Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
          Log.d("Insert Complete", "This is a log");
          Toast.makeText(getBaseContext(), "Good Job", Toast.LENGTH_SHORT).show();

          // SENDING THE INTENT
          Intent intent = new Intent("SMS_SENT");
          intent.putExtra("uri", uri.toString());
          sendBroadcast(intent);

        } else

          Toast.makeText(getBaseContext(), "No Value", Toast.LENGTH_SHORT).show();

      }
    });
    // Listener for the Retrieve button. Returns a record from the content provider based on the ID.
    Retrieve.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        String id = ID.getText().toString();
        if (id.length() > 0) {
          Uri OmniURI = Uri.parse("content://edu.nyu.cs.omnidroid.external.eventapp.cp/CP");
          Cursor c = managedQuery(OmniURI, null, null, null, null);
          int new_id = Integer.parseInt(id);

          if (c.moveToPosition(new_id)) {
            Toast.makeText(
                getBaseContext(),
                c.getString(c.getColumnIndex(ExternalCP._ID)) + ", "
                    + c.getString(c.getColumnIndex(ExternalCP.S_NAME)) + ", "
                    + c.getString(c.getColumnIndex(ExternalCP.S_PH_NO)) + ", "
                    + c.getString(c.getColumnIndex(ExternalCP.TEXT)), Toast.LENGTH_LONG).show();
          }
          Toast.makeText(getBaseContext(), "Good Job 2", Toast.LENGTH_SHORT).show();
        } else

          Toast.makeText(getBaseContext(), "No Value", Toast.LENGTH_SHORT).show();

      }
    });
    // Listener for the Get All button. Toasts all the records in the content provider.
    GetAll.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Uri OmniURI = Uri.parse("content://edu.nyu.cs.omnidroid.external.eventapp.cp/CP");
        Cursor c = managedQuery(OmniURI, null, null, null, null);
        if (c.moveToFirst()) {
          do {
            Toast.makeText(
                getBaseContext(),
                c.getString(c.getColumnIndex(ExternalCP._ID)) + ", "
                    + c.getString(c.getColumnIndex(ExternalCP.S_NAME)) + ", "
                    + c.getString(c.getColumnIndex(ExternalCP.S_PH_NO)) + ", "
                    + c.getString(c.getColumnIndex(ExternalCP.TEXT)), Toast.LENGTH_LONG).show();
          } while (c.moveToNext());
        }

      }

    });

  }

}