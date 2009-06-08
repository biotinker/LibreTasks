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

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.ui.Overview;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Test Application to Verify Configuration File Functionality
 * 
 */
public class TestUserConfig extends Activity {

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    populateUserConfig();
  }

  public TestUserConfig() {
    initConfig();
  }

  public void TestPut() {
    /*
     * UGParser.getInstance().put(UGParser.KEY_EventApp, "TestApp");
     * UGParser.getInstance().put(UGParser.KEY_EventName, "TestEventName");
     * UGParser.getInstance().put(UGParser.KEY_FilterType, "TestFilterType");
     * UGParser.getInstance().put(UGParser.KEY_FilterData, "TestFilterData");
     * UGParser.getInstance().put(UGParser.ACTION_APP_BASE, "TestActionAppBase");
     * UGParser.getInstance().put(UGParser.ACTION_APP_DATA_BASE, "TestActionAppDataBase");
     */
  }

  public void TestGet() {
    /*
     * Log.i(UGParser.KEY_EventApp, UGParser.getInstance().getValue(UGParser.KEY_EventApp));
     * Log.i(UGParser.KEY_EventName, UGParser.getInstance().getValue(UGParser.KEY_EventName));
     * Log.i(UGParser.KEY_FilterType, UGParser.getInstance().getValue(UGParser.KEY_FilterType));
     * Log.i(UGParser.KEY_FilterData, UGParser.getInstance().getValue(UGParser.KEY_FilterData));
     */
  }

  /**
   * Initialize the server configuration objects
   */
  private static void initConfig() {
    /*
     * try { UGParser.getInstance().init(userConfigFile); } catch (IOException e) { // TODO (acase):
     * Use OmnidroidExceptions System.err.println("Unable to load user config properties file.");
     * System.err.println("  " + e.getMessage()); }
     */
  }

  public void populateUserConfig() {
    UGParser ug = new UGParser(this.getApplicationContext());
    ug.deleteAll();

    HashMap<String, String> HM = new HashMap<String, String>();
    HM.put(UGParser.KEY_INSTANCE_NAME, "AutoReply DND");
    HM.put(UGParser.KEY_EVENT_APP, "SMS");
    HM.put(UGParser.KEY_EVENT_TYPE, "SMS_RECEIVED");
    HM.put(UGParser.KEY_FILTER_TYPE, "");
    HM.put(UGParser.KEY_FILTER_DATA, "");
    HM.put(UGParser.KEY_ACTION_APP, "SMS");
    HM.put(UGParser.KEY_ACTION_TYPE, "SMS_SEND");
    HM.put(UGParser.KEY_ENABLE_INSTANCE, "True");
    HM.put(UGParser.KEY_ACTION_DATA1, "SENDER PHONE NUMBER");
    HM.put(UGParser.KEY_ACTION_DATA2, "TEXT");
    ug.writeRecord(HM);

    Intent i = new Intent();
    i.setAction("OmniRestart");
    sendBroadcast(i);

    Toast
        .makeText(getApplicationContext(), "Populated a test user config file", Toast.LENGTH_SHORT)
        .show();
    i = new Intent();
    i.setClass(this, Overview.class);
    startActivity(i);
    this.finish();
  }

}