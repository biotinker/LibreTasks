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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.OmLogger;

/**
 * Exception Handler UI. When an Exception is caught by our app it should send an intent for this
 * activity to catch it.
 * 
 * 
 */
public class ExceptionHandler extends Activity {

  /** Called when the activity is first created. */
  // TODO (acase): Create a user interface to handle events that are caught.
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(this.getLocalClassName(), "onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.default_linear);
  }

  public void onReceive(Context context, Intent intent) {
    Log.i("Omnidroid Exception Found!", intent.getAction());
    try {
      Toast.makeText(context, intent.getAction(), 5).show();

    } catch (Exception e) {
      Log.i("Exception in Intent", e.getLocalizedMessage());
      OmLogger.write(context, "Unable to execute required action");
    }
  }

}