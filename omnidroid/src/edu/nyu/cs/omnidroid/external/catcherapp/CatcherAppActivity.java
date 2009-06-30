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
package edu.nyu.cs.omnidroid.external.catcherapp;

/**
 * The Catcher Application is presently in the application itself. However it can be pulled out of the application and treated as a standalone third party application. It needs to have the following structure:
 *
 *-	A Broadcast Receiver that catches the intents broadcasted by OmniDroid.
 *-	The Broadcast Receiver can then point to an activity that can perform any relevant operations on the intent.
 *-	It also needs to have a mechanism that allows it to query the content provider of OmniDroid
 *
 * Activity used to fetch data from the content provider based on the
 * URI's received.
 */
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.core.CP;

public class CatcherAppActivity extends Activity {
  private String uri;
  private Intent intent;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    intent = getIntent();
    uri = getURI(intent);
    displayAction(uri.toString());
    finish();
  }

  public String getURI(Intent intent1) {
    Bundle b = intent1.getExtras();
    Object c = b.get("uri");
    String uri = c.toString();
    return uri;
  }

  /**
   * Queries the OmniDroid Content Provider to retrieve the Action Data.
   */
  public void displayAction(String uri) {
    String str_uri = uri;
    String[] temp = null;
    temp = str_uri.split("/");
    String num = temp[temp.length - 1];
    String final_uri = str_uri.substring(0, str_uri.length() - num.length() - 1);
    int new_id = Integer.parseInt(num);

    Cursor cur = managedQuery(Uri.parse(final_uri), null, null, null, null);
    if (cur.moveToFirst()) {
      do {
        int id = Integer.parseInt(cur.getString(cur.getColumnIndex("_id")));
        if (new_id == id) {
          Toast.makeText(getBaseContext(),
          cur.getString(cur.getColumnIndex(CP.ACTION_DATA)), Toast.LENGTH_LONG).show();
        }
      } while (cur.moveToNext());
    }
  }
}
